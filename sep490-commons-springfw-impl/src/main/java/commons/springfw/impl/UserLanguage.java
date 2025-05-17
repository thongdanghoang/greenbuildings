package commons.springfw.impl;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum UserLanguage {
    VI("vi"),
    EN("en"),
    ZH("zh");
    
    @Getter(onMethod_ = @JsonValue)
    private final String code;
    
    public static UserLanguage fromCode(String code) {
        return Arrays.stream(values())
                     .filter(locale -> StringUtils.equalsIgnoreCase(code, locale.getCode()))
                     .findFirst()
                     .orElse(VI);
    }
    
    public String getByLanguage(String vi, String en, String zh) {
        return switch (this) {
            case EN -> en;
            case ZH -> zh;
            default -> vi;
        };
    }
}
