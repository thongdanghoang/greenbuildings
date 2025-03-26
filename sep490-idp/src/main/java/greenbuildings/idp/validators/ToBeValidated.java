package greenbuildings.idp.validators;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
public abstract class ToBeValidated {
    
    private boolean validated = false;
    private Map<String, String> errorMap = new HashMap<>();
    private String errorMsg;
    
    public void addError(String key, String errorMsg) {
        this.errorMap.put(key, errorMsg);
    }
    
    public Optional<String> getFirstErrorMsg() {
        if (CollectionUtils.isEmpty(this.getErrorMap())) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.getErrorMap().entrySet().iterator().next().getValue());
    }
}
