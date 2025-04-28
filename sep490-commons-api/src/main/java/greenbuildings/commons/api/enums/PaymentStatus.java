package greenbuildings.commons.api.enums;

import greenbuildings.commons.api.BaseEnum;
import lombok.Getter;

@Getter
public enum PaymentStatus implements BaseEnum {
    PENDING("PENDING"),
    PAID("PAID"),
    PROCESSING("PROCESSING"),
    CANCELLED("CANCELLED");
    
    private final String code;
    
    PaymentStatus(String code) {
        this.code = code;
    }
}
