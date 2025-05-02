package greenbuildings.idp.rest;

import commons.springfw.impl.mappers.CommonMapper;
import commons.springfw.impl.securities.UserContextData;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.dto.SearchResultDTO;
import greenbuildings.commons.api.security.UserRole;
import greenbuildings.idp.dto.EnterpriseUserDTO;
import greenbuildings.idp.dto.EnterpriseUserDetailsDTO;
import greenbuildings.idp.dto.RegisterEnterpriseDTO;
import greenbuildings.idp.dto.UserCriteriaDTO;
import greenbuildings.idp.dto.ValidateOTPRequest;
import greenbuildings.idp.entity.UserEntity;
import greenbuildings.idp.mapper.EnterpriseUserMapper;
import greenbuildings.idp.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/enterprise-user")
@RequiredArgsConstructor
@RolesAllowed({UserRole.RoleNameConstant.ENTERPRISE_OWNER,
               UserRole.RoleNameConstant.SYSTEM_ADMIN,
               UserRole.RoleNameConstant.BASIC_USER,
               UserRole.RoleNameConstant.TENANT})
public class EnterpriseUserRestController {
    
    private final UserService userService;
    private final EnterpriseUserMapper userMapper;
    
    @GetMapping("/{id}")
    public ResponseEntity<EnterpriseUserDetailsDTO> getEnterpriseUserDetail(@PathVariable("id") UUID id) {
        var userDetailsEntity = userService.getEnterpriseUserDetail(id);
        var userDetails = userMapper.userEntityToEnterpriseUserDetailDTO(userDetailsEntity);
        return ResponseEntity.ok(userDetails);
    }
    
    @GetMapping("/enterprise-owner")
    @RolesAllowed({UserRole.RoleNameConstant.ENTERPRISE_OWNER,
                   UserRole.RoleNameConstant.SYSTEM_ADMIN})
    public ResponseEntity<EnterpriseUserDetailsDTO> getEnterpriseOwnerDetail(@AuthenticationPrincipal UserContextData userContextData) {
        var enterpriseOwnerDetail = userService.findById(userContextData.getId());
        if (enterpriseOwnerDetail.isPresent()) {
            var userDetailsEntity = userService.getEnterpriseUserDetail(enterpriseOwnerDetail.get().getId());
            var userDetails = userMapper.userEntityToEnterpriseUserDetailDTO(userDetailsEntity);
            return ResponseEntity.ok(userDetails);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/search")
    @RolesAllowed({UserRole.RoleNameConstant.SYSTEM_ADMIN})
    public ResponseEntity<SearchResultDTO<EnterpriseUserDTO>> searchEnterpriseUser(
            @RequestBody SearchCriteriaDTO<UserCriteriaDTO> searchCriteria) {
        
        var searchResults = userService.search(searchCriteria);
        
        return ResponseEntity.ok(
                CommonMapper.toSearchResultDTO(
                        searchResults,
                        userMapper::userEntityToEnterpriseUserDTO));
    }
    
    @PostMapping()
    public ResponseEntity<Void> createNewEnterpriseUser(@RequestBody EnterpriseUserDetailsDTO enterpriseUserDTO) {
        if (Objects.isNull(enterpriseUserDTO.id())) { // create new user
            var newEnterpriseUser = userMapper.createNewEnterpriseUser(enterpriseUserDTO);
            return saveUserAndReturnResponse(newEnterpriseUser, HttpStatus.CREATED);
        }
        
        var user = userService.getEnterpriseUserDetail(enterpriseUserDTO.id());
        userMapper.updateEnterpriseUser(user, enterpriseUserDTO);
        return saveUserAndReturnResponse(user, HttpStatus.OK);
    }
    
    @PutMapping("/self-update")
    @RolesAllowed({UserRole.RoleNameConstant.BASIC_USER})
    public ResponseEntity<EnterpriseUserDetailsDTO> updateEnterpriseUser(@RequestBody EnterpriseUserDetailsDTO enterpriseUserDTO) {
        var user = userService.getEnterpriseUserDetail(enterpriseUserDTO.id());
        userMapper.updateSelfUser(user, enterpriseUserDTO);
        user = userService.updateBasicUser(user);
        return ResponseEntity.ok(userMapper.userEntityToBasicEnterpriseUserDetailDTO(user));
    }
    
    private ResponseEntity<Void> saveUserAndReturnResponse(UserEntity user, HttpStatus status) {
        associateUserWithEntities(user);
        userService.createOrUpdateUser(user);
        return ResponseEntity.status(status).build();
    }
    
    private void associateUserWithEntities(UserEntity user) {
        user.getAuthorities().forEach(bp -> bp.setUser(user));
    }
    
    @DeleteMapping
    public ResponseEntity<Void> deleteUsers(@RequestBody Set<UUID> userIds) {
        userService.deleteUsers(userIds);
        return ResponseEntity.noContent().build();
    }
    
    
    @PostMapping("/new-enterprise")
    @RolesAllowed({UserRole.RoleNameConstant.BASIC_USER})
    public ResponseEntity<Void> createNewEnterprise(@AuthenticationPrincipal UserContextData userContextData,
                                                    @RequestBody RegisterEnterpriseDTO enterpriseDTO) {
        userService.createNewEnterprise(userContextData, enterpriseDTO.withEmail(userContextData.getEmail()));
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExist(@RequestParam String email) {
        return ResponseEntity.ok(userService.findByEmail(email).isPresent());
    }
    
    @GetMapping("/requestOTP")
    public ResponseEntity<?> getOtpByMail(@AuthenticationPrincipal UserContextData userContextData) {
        userService.sendOtp(userContextData.getId());
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/validateOTP")
    public ResponseEntity<?> getOtpByMail(@RequestBody ValidateOTPRequest request,
                                          @AuthenticationPrincipal UserContextData userContextData) {
        userService.validateOTP(request, userContextData.getId());
        return ResponseEntity.noContent().build();
    }
}
