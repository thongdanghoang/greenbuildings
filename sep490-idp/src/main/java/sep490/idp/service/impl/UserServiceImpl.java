package sep490.idp.service.impl;

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
import sep490.common.api.dto.SearchCriteriaDTO;
import sep490.common.api.exceptions.BusinessErrorParam;
import sep490.common.api.exceptions.BusinessException;
import sep490.common.api.exceptions.TechnicalException;
import sep490.common.api.security.UserRole;
import sep490.common.api.security.UserScope;
import sep490.common.api.utils.CommonUtils;
import sep490.idp.dto.NewEnterpriseUserDTO;
import sep490.idp.dto.SignupDTO;
import sep490.idp.dto.SignupResult;
import sep490.idp.dto.UserCriteriaDTO;
import sep490.idp.entity.BuildingPermissionEntity;
import sep490.idp.entity.UserEntity;
import sep490.idp.mapper.CommonMapper;
import sep490.idp.mapper.EnterpriseUserMapper;
import sep490.idp.repository.BuildingRepository;
import sep490.idp.repository.UserRepository;
import sep490.idp.service.UserService;
import sep490.idp.utils.IEmailUtil;
import sep490.idp.utils.IMessageUtil;
import sep490.idp.utils.SEPMailMessage;
import sep490.idp.utils.SecurityUtils;
import sep490.idp.validation.Validator;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(rollbackFor = Throwable.class)
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    @Qualifier("signupValidator")
    private final Validator<SignupDTO> validator;
    private final EnterpriseUserMapper userMapper;
    private final BuildingRepository buildingRepo;
    private final IMessageUtil messageUtil;
    private final IEmailUtil emailUtil;
    @Value("${spring.application.homepage}")
    private String homepage;
    
    
    @Override
    public SignupResult signup(SignupDTO signupDTO, Model model) {
        SignupResult result = validateSignupDTO(signupDTO);
        
        if (result.isSuccess()) {
            var user = createUser(signupDTO);
            userRepo.save(user);
            result.setSuccess(true);
            result.setSuccessMessage("signup.notification");
            result.setRedirectUrl("redirect:/login?message=" + result.getSuccessMessage());
        }
        
        return result;
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
    
    
    private UserEntity createUser(SignupDTO signupDTO) {
        return UserEntity.register(
                signupDTO.getEmail(),
                false,
                UserRole.ENTERPRISE_OWNER,
                UserScope.ENTERPRISE,
                signupDTO.getFirstName(),
                signupDTO.getLastName(),
                signupDTO.getPhone(),
                false,
                passwordEncoder.encode(signupDTO.getPassword()));
    }
    
    @Override
    public Page<UserEntity> search(SearchCriteriaDTO<UserCriteriaDTO> searchCriteria) {
        var userIDs = userRepo.findByName(
                searchCriteria.criteria().criteria(),
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
        var users = userRepo.findByIdInAndDeletedFalse(userIds);
        if (users.size() != userIds.size()) {
            userIds.removeAll(users.stream().map(UserEntity::getId).collect(Collectors.toSet()));
            throw new BusinessException("userIds", "user.delete.not.found",
                                        List.of(new BusinessErrorParam("ids", userIds)));
        }
        userRepo.deleteAll(users);
    }
    
    @Override
    public void createNewUser(NewEnterpriseUserDTO dto) {
        var user = userMapper.newEnterpriseUserDTOToEntity(dto);
        if (userRepo.existsByEmail(user.getEmail())) {
            throw new BusinessException("email", "email.exist");
        }
        mappingUserPermission(dto, user);
        String password = CommonUtils.alphaNumericString(12);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(UserRole.ENTERPRISE_EMPLOYEE);
        
        SEPMailMessage message = populateNewUserMailMessage(user.getEmail(), password);
        emailUtil.sendMail(message);
        
        userRepo.save(user);
    }
    
    private void mappingUserPermission(NewEnterpriseUserDTO dto, UserEntity user) throws BusinessException {
        if (user.getScope() == UserScope.ENTERPRISE) {
            String currentUserEmail = SecurityUtils.getUserEmail().orElseThrow(() -> new TechnicalException("No user in context"));
            UserEntity owner = userRepo.findByEmail(currentUserEmail)
                                       .orElseThrow(() -> new TechnicalException("Owner does not exist"));
            Set<BuildingPermissionEntity> buildingPermissionEntitySet =
                    owner.getPermissions()
                         .stream()
                         .map(p -> p.getId().getBuildingId())
                         .map(bId -> new BuildingPermissionEntity(bId, user, dto.permissionRole()))
                         .collect(Collectors.toSet());
            
            user.setPermissions(buildingPermissionEntitySet);
        } else if (user.getScope() == UserScope.BUILDING) {
            if (CollectionUtils.isEmpty(dto.buildings()) || !buildingRepo.existsAllByIdIn(dto.buildings())) {
                throw new BusinessException("buildings", "buildings.invalid");
            }
            Set<BuildingPermissionEntity> buildingPermissionEntitySet =
                    dto.buildings()
                       .stream()
                       .map(bId -> new BuildingPermissionEntity(bId, user, dto.permissionRole()))
                       .collect(Collectors.toSet());
            
            user.setPermissions(buildingPermissionEntitySet);
        }
    }
    
    private SEPMailMessage populateNewUserMailMessage(String email, String password) {
        SEPMailMessage message = new SEPMailMessage();
        
        message.setTemplateName("new-user-notify.ftl");
        message.setTo(email);
        message.setSubject(messageUtil.getMessage("newUser.mail.title"));
        
        message.addTemplateModel("userEmail", email);
        message.addTemplateModel("password", password);
        message.addTemplateModel("homepage", homepage);
        
        return message;
    }
}
