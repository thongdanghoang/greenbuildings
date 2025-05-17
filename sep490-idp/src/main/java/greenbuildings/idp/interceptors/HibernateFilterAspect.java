package greenbuildings.idp.interceptors;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
public class HibernateFilterAspect {
    
    @PersistenceContext
    private final EntityManager entityManager;
    
    @Around("@annotation(enableHibernateFilter)")
    public Object applyFilter(ProceedingJoinPoint joinPoint, EnableHibernateFilter enableHibernateFilter) throws Throwable {
        
        var session = entityManager.unwrap(Session.class);
        
        if (Objects.nonNull(session)) {
            var filter = session.enableFilter(enableHibernateFilter.filterName());
            for (EnableHibernateFilter.Param param : enableHibernateFilter.params()) {
                if (param.value().startsWith("${") && param.value().endsWith("}")) {
                    var filterParamName = param.value().substring(2, param.value().length() - 1);
                    var parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
                    var methodParamValues = joinPoint.getArgs();
                    for (int i = 0; i < parameterNames.length; i++) {
                        if (parameterNames[i].equals(filterParamName)) {
                            filter.setParameter(filterParamName, methodParamValues[i]);
                            break;
                        }
                    }
                } else {
                    filter.setParameter(param.name(), param.value());
                }
            }
        }
        
        try {
            return joinPoint.proceed();
        } finally {
            if (Objects.nonNull(session)) {
                session.disableFilter(enableHibernateFilter.filterName());
            }
        }
    }
}
