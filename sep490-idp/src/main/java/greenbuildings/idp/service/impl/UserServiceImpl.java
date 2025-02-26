package greenbuildings.idp.service.impl;

import commons.springfw.impl.mappers.CommonMapper;
import commons.springfw.impl.utils.SecurityUtils;
import greenbuildings.commons.api.SagaManager;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.events.PendingEnterpriseRegisterEvent;
import greenbuildings.commons.api.exceptions.BusinessErrorParam;
import greenbuildings.commons.api.exceptions.BusinessErrorResponse;
import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.commons.api.exceptions.TechnicalException;
import greenbuildings.commons.api.security.UserRole;
import greenbuildings.commons.api.security.UserScope;
import greenbuildings.commons.api.utils.CommonUtils;
import greenbuildings.idp.dto.SignupDTO;
import greenbuildings.idp.dto.SignupResult;
import greenbuildings.idp.dto.UserCriteriaDTO;
import greenbuildings.idp.entity.UserEntity;
import greenbuildings.idp.producers.IdPEventProducer;
import greenbuildings.idp.repository.UserRepository;
import greenbuildings.idp.repository.UserRepositoryAdapter;
import greenbuildings.idp.service.UserService;
import greenbuildings.idp.utils.IEmailUtil;
import greenbuildings.idp.utils.IMessageUtil;
import greenbuildings.idp.utils.SEPMailMessage;
import greenbuildings.idp.validation.Validator;
import greenbuildings.idp.validators.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(rollbackFor = Throwable.class)
public class UserServiceImpl extends SagaManager implements UserService {
    
    private final UserRepository userRepo;
    private final UserRepositoryAdapter userRepositoryAdapter;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;
    @Qualifier("signupValidator")
    private final Validator<SignupDTO> validator;
    private final IMessageUtil messageUtil;
    private final IEmailUtil emailUtil;
    @Value("${spring.application.homepage}")
    private String homepage;
    private final IdPEventProducer kafkaAdapter;
    
    @Override
    public SignupResult signup(SignupDTO signupDTO, Model model) {
        SignupResult result = validateSignupDTO(signupDTO);
        var future = new CompletableFuture<>();
        var correlationId = UUID.randomUUID().toString();
        getPendingSagaResponses().put(correlationId, future);
        
        if (!result.isSuccess()) {
            return result;
        }
        var user = createEnterpriseOwner(signupDTO);
        
        // PENDING
        kafkaAdapter.publishEnterpriseOwnerRegisterEvent(correlationId,
                                                         PendingEnterpriseRegisterEvent
                                                                 .builder()
                                                                 .email(signupDTO.getEmail())
                                                                 .name(signupDTO.getEnterpriseName())
                                                                 .hotline(signupDTO.getEnterpriseHotline())
                                                                 .build());
        try { // Wait synchronously for response
            var enterpriseId = UUID.fromString(future.get(TRANSACTION_TIMEOUT, TimeUnit.SECONDS).toString());// Timeout in case response is lost
            user.getEnterprise().setEnterprise(enterpriseId);
            userRepo.save(user); // COMPLETE
        } catch (TimeoutException e) {
            throw new TechnicalException("Request timeout", e);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof BusinessException businessException) {
                throw businessException;
            }
            throw new TechnicalException("Error while waiting for response", e);
        } catch (InterruptedException e) {
            /* Clean up whatever needs to be handled before interrupting  */
            log.warn("Interrupted!", e);
            Thread.currentThread().interrupt();
        } finally {
            getPendingSagaResponses().remove(correlationId);
        }
        
        result.setSuccess(true);
        result.setSuccessMessage("signup.notification");
        result.setRedirectUrl("redirect:/login?message=" + result.getSuccessMessage());
        return result;
    }
    
    @Override
    public void commitEnterpriseOwnerCreation(UUID enterpriseId, String correlationId) {
        var future = getPendingSagaResponses().remove(correlationId);
        if (Objects.nonNull(future)) {
            future.complete(enterpriseId);
        }
    }
    
    @Override
    public void rollbackEnterpriseOwnerCreation(BusinessErrorResponse error) {
        var correlationId = error.correlationId();
        var future = getPendingSagaResponses().remove(correlationId);
        if (Objects.nonNull(future)) {
            future.completeExceptionally(new BusinessException(error.field(), error.i18nKey(), error.args()));
        }
    }
    
    private SignupResult validateSignupDTO(SignupDTO signupDTO) {
        SignupResult result = new SignupResult();
        result.setRedirectUrl("redirect:/login");
        result.setSuccess(true);
        
        validator.validate(signupDTO);
        if (!signupDTO.isValidated()) {
            result.setSuccess(false);
            result.setErrorMessage(signupDTO.getFirstErrorMsg().orElse(null));
            result.setRedirectUrl("signup");
        }
        
        return result;
    }
    
    
    private UserEntity createEnterpriseOwner(SignupDTO signupDTO) {
        return UserEntity.register(
                signupDTO.getEmail(),
                false,
                UserRole.ENTERPRISE_OWNER,
                UserScope.ENTERPRISE,
                "",
                "",
                "",
                false,
                passwordEncoder.encode(signupDTO.getPassword()));
    }
    
    @Override
    public Page<UserEntity> search(SearchCriteriaDTO<UserCriteriaDTO> searchCriteria) {
        UUID enterpriseId = SecurityUtils.getCurrentUserEnterpriseId().orElseThrow();
        var userIDs = userRepo.findByName(
                searchCriteria.criteria().criteria(),
                enterpriseId,
                CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort()));
        var results = userRepo
                .findByIDsWithPermissions(userIDs.toSet())
                .stream()
                .collect(Collectors.toMap(UserEntity::getId, Function.identity()));
        return userIDs.map(results::get);
    }
    
    @Override
    public void deleteUsers(Set<UUID> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            throw new BusinessException("userIds", "user.delete.no.ids", Collections.emptyList());
        }
        var users = userRepositoryAdapter.findByIDs(userIds);
        if (users.size() != userIds.size()) {
            userIds.removeAll(users.stream().map(UserEntity::getId).collect(Collectors.toSet()));
            throw new BusinessException("userIds", "user.delete.not.found",
                                        List.of(new BusinessErrorParam("ids", userIds)));
        }
        // Stream approach to check if any user has the ENTERPRISE_OWNER role
        if (users.stream().anyMatch(user -> user.getEnterprise().getRole() == UserRole.ENTERPRISE_OWNER)) {
            throw new BusinessException("userIds", "user.cannot.delete.owner");
        }
        for (UserEntity user : users) {
            user.setDeleted(true);
        }
        userRepo.saveAll(users);
    }
    
    @Override
    public void createOrUpdateEnterpriseUser(UserEntity user) {
        userValidator.validateEnterpriseOwnerManageEmployees(user);
        this.performCreateUserAction(user);
        userRepo.save(user);
    }
    
    private void performCreateUserAction(UserEntity user) {
        // Perform create action when create new
        if (Objects.isNull(user.getId())) {
            var password = CommonUtils.alphaNumericString(12);
            user.setPassword(passwordEncoder.encode(password));
            
            var message = sendPasswordToUserByEmail(user.getEmail(), password);
            emailUtil.sendMail(message);
        }
    }
    
    private SEPMailMessage sendPasswordToUserByEmail(String email, String password) {
        SEPMailMessage message = new SEPMailMessage();
        
        message.setTemplateName("new-user-notify.ftl");
        message.setTo(email);
        message.setSubject(messageUtil.getMessage("newUser.mail.title"));
        
        message.addTemplateModel("userEmail", email);
        message.addTemplateModel("password", password);
        message.addTemplateModel("homepage", homepage);
        
        return message;
    }
    
    @Override
    public UserEntity getEnterpriseUserDetail(UUID id) {
        return userRepo.findByIdWithBuildingPermissions(id).orElseThrow();
    }
    
    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }
    
    @Override
    public void update(UserEntity user) {
        userRepo.save(user);
    }
    
}
