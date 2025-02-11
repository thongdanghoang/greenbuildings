package enterprise.rest;

import commons.springfw.impl.securities.UserContextData;
import enterprise.dtos.EnterpriseDTO;
import enterprise.mappers.EnterpriseMapper;
import enterprise.services.EnterpriseService;
import green_buildings.commons.api.exceptions.BusinessException;
import green_buildings.commons.api.security.UserRole;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/enterprise")
@RequiredArgsConstructor
@RolesAllowed({
        UserRole.RoleNameConstant.ENTERPRISE_OWNER
})
public class EnterpriseController {
    
    private final EnterpriseService service;
    private final EnterpriseMapper mapper;
    
    @PostMapping
    public ResponseEntity<UUID> createEnterprise(@AuthenticationPrincipal UserContextData userContextData,
                                                 @RequestBody EnterpriseDTO enterpriseDTO) {
        if (Objects.nonNull(userContextData.getEnterpriseId())) {
            throw new BusinessException(StringUtils.EMPTY, "error.user.already.belongs.to.enterprise", Collections.emptyList());
        }
        
        var enterprise = mapper.createEnterprise(enterpriseDTO);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createEnterprise(enterprise));
    }
    
}
