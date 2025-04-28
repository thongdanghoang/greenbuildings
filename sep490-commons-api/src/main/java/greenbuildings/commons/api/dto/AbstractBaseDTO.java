package greenbuildings.commons.api.dto;

import lombok.Data;

import java.util.UUID;

@Data
public abstract class AbstractBaseDTO {
    protected UUID id;
    protected int version;

    protected AbstractBaseDTO() {
        // Abstract class should not be instantiated
    }
}