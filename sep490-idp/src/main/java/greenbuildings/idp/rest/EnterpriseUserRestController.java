package greenbuildings.idp.rest;

import commons.springfw.impl.mappers.CommonMapper;
import commons.springfw.impl.securities.UserContextData;
import commons.springfw.impl.utils.SecurityUtils;
import greenbuildings.idp.dto.NewEnterpriseDTO;
import greenbuildings.idp.dto.UserByBuildingDTO;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.dto.SearchResultDTO;
import greenbuildings.commons.api.security.UserRole;
import greenbuildings.idp.dto.EnterpriseUserDTO;
import greenbuildings.idp.dto.EnterpriseUserDetailsDTO;
import greenbuildings.idp.dto.UserCriteriaDTO;
import greenbuildings.idp.entity.UserEntity;
import greenbuildings.idp.mapper.EnterpriseUserMapper;
import greenbuildings.idp.service.UserService;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/enterprise-user")
@RequiredArgsConstructor
@RolesAllowed({UserRole.RoleNameConstant.ENTERPRISE_OWNER})
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
            UserRole.RoleNameConstant.ENTERPRISE_EMPLOYEE,
            UserRole.RoleNameConstant.SYSTEM_ADMIN})
    public ResponseEntity<EnterpriseUserDetailsDTO> getEnterpriseOwnerDetail() {
        String email = SecurityUtils.getCurrentUserEmail().orElseThrow();
        var enterpriseOwnerDetail = userService.findByEmail(email);
        if(enterpriseOwnerDetail.isPresent()) {
            var userDetailsEntity = userService.getEnterpriseUserDetail(enterpriseOwnerDetail.get().getId());
            var userDetails = userMapper.userEntityToEnterpriseUserDetailDTO(userDetailsEntity);
            return ResponseEntity.ok(userDetails);
        }
         return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/search")
    public ResponseEntity<SearchResultDTO<EnterpriseUserDTO>> searchEnterpriseUser(@RequestBody SearchCriteriaDTO<UserCriteriaDTO> searchCriteria) {
        var searchResults = userService.search(searchCriteria);
        return ResponseEntity.ok(
                CommonMapper.toSearchResultDTO(
                        searchResults,
                        userMapper::userEntityToEnterpriseUserDTO));
    }

    @PostMapping("/search/{buildingId}")
    public ResponseEntity<SearchResultDTO<UserByBuildingDTO>> searchUserByBuildings(@RequestBody SearchCriteriaDTO<Void> searchCriteria, @PathVariable UUID buildingId) {
        var pageable = CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort());
        Page<UserEntity> searchResults = userService.getUserByBuilding(buildingId, pageable);
        var searchResultDTO = CommonMapper.toSearchResultDTO(
                searchResults,
                userEntity -> userMapper.userEntityToUserByBuildingDTO(userEntity, buildingId)  );
        return ResponseEntity.ok(searchResultDTO);
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
    
    private ResponseEntity<Void> saveUserAndReturnResponse(UserEntity user, HttpStatus status) {
        associateUserWithEntities(user);
        userService.createOrUpdateEnterpriseUser(user);
        return ResponseEntity.status(status).build();
    }
    
    private void associateUserWithEntities(UserEntity user) {
        user.getEnterprise().setUser(user);
        user.getBuildingPermissions().forEach(bp -> bp.setUser(user));
    }
    
    @DeleteMapping
    public ResponseEntity<Void> deleteUsers(@RequestBody Set<UUID> userIds) {
        userService.deleteUsers(userIds);
        return ResponseEntity.noContent().build();
    }
    

    @PostMapping("/new-enterprise")
    @RolesAllowed({UserRole.RoleNameConstant.BASIC_USER})
    public ResponseEntity<Void> createNewEnterprise(@AuthenticationPrincipal UserContextData userContextData, @RequestBody NewEnterpriseDTO enterpriseDTO) {
        userService.createNewEnterprise(userContextData, enterpriseDTO);
        return ResponseEntity.noContent().build();
    }
    
}
