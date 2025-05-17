package greenbuildings.enterprise.interceptors;

import greenbuildings.enterprise.entities.BuildingEntity;
import greenbuildings.enterprise.securities.PowerBiAuthenticationFacade;

import commons.springfw.impl.securities.UserContextData;
import commons.springfw.impl.utils.SecurityUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
    
    @PersistenceContext
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
            }
            if (currentEnterpriseId.isPresent()) {
                var filter = session.enableFilter(BuildingEntity.BUILDING_SUBSCRIPTION_FILTER_FOR_ENTERPRISE);
                filter.setParameter(BuildingEntity.ENTERPRISE_ID_PARAMETER_NAME, currentEnterpriseId.get());
            }
            if (currentTenantId.isPresent()) {
                var filter = session.enableFilter(BuildingEntity.BUILDING_SUBSCRIPTION_FILTER_FOR_TENANT);
                filter.setParameter(BuildingEntity.TENANT_ID_PARAMETER_NAME, currentTenantId.get());
            }
        }
        
        try {
            // Proceed with the original method execution
            return joinPoint.proceed();
        } finally {
            if (Objects.nonNull(session)) {
                if (currentEnterpriseId.isPresent()) {
                    session.disableFilter(BuildingEntity.BUILDING_SUBSCRIPTION_FILTER_FOR_ENTERPRISE);
                }
                if (currentTenantId.isPresent()) {
                    session.disableFilter(BuildingEntity.BUILDING_SUBSCRIPTION_FILTER_FOR_TENANT);
                }
            }
        }
    }
}
