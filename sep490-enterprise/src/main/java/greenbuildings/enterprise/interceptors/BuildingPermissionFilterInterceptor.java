package greenbuildings.enterprise.interceptors;

import greenbuildings.commons.springfw.impl.securities.UserContextData;
import greenbuildings.commons.springfw.impl.utils.SecurityUtils;
import greenbuildings.enterprise.entities.BuildingEntity;
import greenbuildings.enterprise.securities.PowerBiAuthenticationFacade;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class BuildingPermissionFilterInterceptor {
    
    private final EntityManager entityManager;
    
    private final PowerBiAuthenticationFacade authenticationFacade;
    
    @Around("@annotation(buildingPermissionFilter)")
    public Object applyFilter(ProceedingJoinPoint joinPoint, BuildingPermissionFilter buildingPermissionFilter) throws Throwable {
        var session = entityManager.unwrap(Session.class);
        var powerBiAuthentication = Optional.ofNullable(authenticationFacade.getAuthentication());
        var currentEnterpriseId = SecurityUtils.getUserContextData().flatMap(UserContextData::getEnterpriseId);
        var currentTenantId = SecurityUtils.getUserContextData().flatMap(UserContextData::getTenantId);
        
        if (Objects.nonNull(session)) {
            if (powerBiAuthentication.isPresent()) {
                var filter = session.enableFilter(BuildingEntity.BUILDING_SUBSCRIPTION_FILTER_FOR_ENTERPRISE);
                filter.setParameter(BuildingEntity.ENTERPRISE_ID_PARAMETER_NAME, powerBiAuthentication.get().getPrincipal().enterpriseId());
            } else if (currentEnterpriseId.isPresent()) {
                var filter = session.enableFilter(BuildingEntity.BUILDING_SUBSCRIPTION_FILTER_FOR_ENTERPRISE);
                filter.setParameter(BuildingEntity.ENTERPRISE_ID_PARAMETER_NAME, currentEnterpriseId.get());
            } else if (currentTenantId.isPresent()) {
                var filter = session.enableFilter(BuildingEntity.BUILDING_SUBSCRIPTION_FILTER_FOR_TENANT);
                filter.setParameter(BuildingEntity.TENANT_ID_PARAMETER_NAME, currentTenantId.get());
            }
        }
        
        try {
            // Proceed with the original method execution
            return joinPoint.proceed();
        } finally {
            if (Objects.nonNull(session)) {
                session.disableFilter(BuildingEntity.BUILDING_SUBSCRIPTION_FILTER_FOR_ENTERPRISE);
                session.disableFilter(BuildingEntity.BUILDING_SUBSCRIPTION_FILTER_FOR_TENANT);
            }
        }
    }
}
