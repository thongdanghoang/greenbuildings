package greenbuildings.idp.exceptions;

import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.idp.entity.PowerBiAuthority;
import org.springframework.http.HttpStatus;

import java.util.Collections;

public class PowerBiAuthorityNoteAlreadyExists extends BusinessException {
    
    public PowerBiAuthorityNoteAlreadyExists() {
        super(PowerBiAuthority.Fields.note, "powerBiAuthority.note.alreadyExists", Collections.emptyList(), HttpStatus.CONFLICT.value());
    }
    
}
