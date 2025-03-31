package greenbuildings.enterprise.exceptions;

import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.enterprise.entities.EnterpriseDashboardEntity;
import org.springframework.http.HttpStatus;

import java.util.Collections;

public class EnterpriseDashboardTitleAlreadyExists extends BusinessException {
    
    public EnterpriseDashboardTitleAlreadyExists() {
        super(EnterpriseDashboardEntity.Fields.title, "enterpriseDashboard.title.alreadyExists", Collections.emptyList(), HttpStatus.CONFLICT.value());
    }
    
}
