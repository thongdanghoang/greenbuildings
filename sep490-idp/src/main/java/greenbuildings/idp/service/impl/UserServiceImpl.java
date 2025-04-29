package greenbuildings.idp.service.impl;

import commons.springfw.impl.mappers.CommonMapper;
import commons.springfw.impl.securities.UserContextData;
import commons.springfw.impl.utils.SecurityUtils;
import greenbuildings.commons.api.SagaManager;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.exceptions.BusinessErrorParam;
import greenbuildings.commons.api.exceptions.BusinessErrorResponse;
import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.commons.api.exceptions.TechnicalException;
import greenbuildings.commons.api.security.UserRole;
import greenbuildings.commons.api.security.UserScope;
import greenbuildings.commons.api.utils.CommonUtils;
import greenbuildings.idp.dto.RegisterEnterpriseDTO;
import greenbuildings.idp.dto.SignupDTO;
import greenbuildings.idp.dto.SignupResult;
import greenbuildings.idp.dto.UserCriteriaDTO;
import greenbuildings.idp.dto.ValidateOTPRequest;
import greenbuildings.idp.entity.UserEntity;
import greenbuildings.idp.entity.UserOTP;
import greenbuildings.idp.interceptors.EnableHibernateFilter;
import greenbuildings.idp.producers.IdPEventProducer;
import greenbuildings.idp.repository.UserOTPRepository;
import greenbuildings.idp.repository.UserRepository;
import greenbuildings.idp.service.UserService;
import greenbuildings.idp.utils.IEmailUtil;
import greenbuildings.idp.utils.IMessageUtil;
import greenbuildings.idp.utils.SEPMailMessage;
import greenbuildings.idp.validators.UserValidator;
import greenbuildings.idp.validators.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
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
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;
    @Qualifier("signupValidator")
    private final Validator<SignupDTO> validator;
    private final IMessageUtil messageUtil;
    private final IEmailUtil emailUtil;
    private final UserOTPRepository otpRepo;
    @Value("${spring.application.homepage}")
    private String homepage;
    private final IdPEventProducer kafkaAdapter;
    
    @Override
    public SignupResult signup(SignupDTO signupDTO, Model model) {
        SignupResult result = validateSignupDTO(signupDTO);
        
        if (!result.isSuccess()) {
            return result;
        }
        var user = createBasicUser(signupDTO);
        userRepo.save(user);
        
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
    
    
    private UserEntity createBasicUser(SignupDTO signupDTO) {
        return UserEntity.register(
                signupDTO.getEmail(),
                false,
                UserRole.BASIC_USER,
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
    public Page<UserEntity> getUserByBuilding(UUID buildingId, Pageable pageable) {
        //UUID enterpriseId = SecurityUtils.getCurrentUserEnterpriseId().orElseThrow();
        return userRepo.findUserAndBuilding(buildingId, pageable);
    }
    
    @Override
    public void createNewEnterprise(UserContextData userContextData, RegisterEnterpriseDTO registerEnterprise) {
        var user = userRepo.findById(userContextData.getId()).orElseThrow();
        if (user.getRole() != UserRole.BASIC_USER || user.getEnterpriseId() != null) {
            throw new BusinessException("user", "validation.business.createEnterprise.alreadyExists");
        }
        if (registerEnterprise.role() != UserRole.ENTERPRISE_OWNER && registerEnterprise.role() != UserRole.TENANT) {
            throw new TechnicalException("Invalid role for enterprise owner");
        }
        var future = new CompletableFuture<>();
        var correlationId = UUID.randomUUID().toString();
        getPendingSagaResponses().put(correlationId, future);
        
        // PENDING
        kafkaAdapter.publishEnterpriseOwnerRegisterEvent(correlationId, registerEnterprise);
        try { // Wait synchronously for response
            var enterpriseId = UUID.fromString(future.get(TRANSACTION_TIMEOUT, TimeUnit.SECONDS).toString());// Timeout in case response is lost
            user.setEnterpriseId(enterpriseId);
            user.setRole(registerEnterprise.role());
            user.setScope(UserScope.ENTERPRISE);
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
    }
    
    @Override
    public UserEntity updateBasicUser(UserEntity user) {
        return userRepo.save(user);
    }
    
    @Override
    @EnableHibernateFilter(
            filterName = UserEntity.BELONG_ENTERPRISE_FILTER,
            params = {
                    @EnableHibernateFilter.Param(name = UserEntity.BELONG_ENTERPRISE_PARAM, value = "${enterpriseId}")
            }
    )
    public void deleteUsers(Set<UUID> userIds, UUID enterpriseId) {
        if (CollectionUtils.isEmpty(userIds)) {
            throw new BusinessException("userIds", "user.delete.no.ids", Collections.emptyList());
        }
        var users = userRepo.findByIDs(userIds);
        if (users.size() != userIds.size()) {
            userIds.removeAll(users.stream().map(UserEntity::getId).collect(Collectors.toSet()));
            throw new BusinessException("userIds", "user.delete.not.found",
                                        List.of(new BusinessErrorParam("ids", userIds)));
        }
        // Stream approach to check if any user has the ENTERPRISE_OWNER role
        if (users.stream().anyMatch(user -> user.getRole() == UserRole.ENTERPRISE_OWNER)) {
            throw new BusinessException("userIds", "user.cannot.delete.owner");
        }
        userRepo.deleteAll(users);
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
    
    private SEPMailMessage createMessageForVerifyOTP(UserEntity userEntity) {
        SEPMailMessage message = new SEPMailMessage();
        
        message.setTemplateName("mail-verify-otp.ftl");
        message.setTo(userEntity.getEmail());
        message.setSubject(messageUtil.getMessage("verifyOtp.mail.title"));
        
        message.addTemplateModel("userEmail", userEntity.getEmail());
        message.addTemplateModel("otpCode", userEntity.getOtp().getOtpCode());
        return message;
    }
    
    @Override
    public UserEntity getEnterpriseUserDetail(UUID id) {
        return userRepo.findByIdWithBuildingPermissions(id).orElseThrow();
    }
    
    @Override
    public UserEntity getUserDetail(String email) {
        return userRepo.findByEmail(email).orElseThrow();
    }
    
    @Transactional(readOnly = true)
    @Override
    public Optional<UserEntity> findById(UUID enterpriseId) {
        return userRepo.findById(enterpriseId);
    }
    
    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }
    
    @Override
    public void update(UserEntity user) {
        userRepo.save(user);
    }
    
    @Override
    public void sendOtp() {
        UserEntity userEntity = userRepo.findByEmail(SecurityUtils.getCurrentUserEmail().orElseThrow()).orElseThrow();
        
        if (userEntity.isEmailVerified()) {
            return;
        }
        
        if (userEntity.getOtp() != null) {
            userEntity.getOtp().resetOTP();
        } else {
            userEntity.setOtp(new UserOTP(userEntity));
        }
        userEntity = userRepo.save(userEntity);
        
        try {
            var message = createMessageForVerifyOTP(userEntity);
            emailUtil.sendMail(message);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new TechnicalException("Gửi OTP thất bại, vui lòng thử lại sau!");
        }
    }
    
    @Override
    public void validateOTP(ValidateOTPRequest request) {
        UserEntity userEntity = userRepo.findByEmail(SecurityUtils.getCurrentUserEmail().orElseThrow()).orElseThrow();
        
        var otp = userEntity.getOtp();
        if (!otp.getOtpCode().equals(request.otpCode())) {
            throw new BusinessException("otpCode", "validateOTP.invalidCode");
        }
        if (LocalDateTime.now().isAfter(otp.getExpiredTime())) {
            throw new BusinessException("otpCode", "validateOTP.expired");
        }
        userEntity.setEmailVerified(true);
        userEntity.setOtp(null);
        userRepo.saveAndFlush(userEntity);
        otpRepo.delete(otp);
    }
    
}
