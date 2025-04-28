package greenbuildings.commons.api.enums;

import greenbuildings.commons.api.BaseEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum UserLocale implements BaseEnum {
    VI("vi-VN"),
    EN("en-US"),
    ZH("zh-CN");
    
    private final String code;
    
    public static UserLocale fromCode(String code) {
        return Arrays.stream(values())
                     .filter(userLanguage -> userLanguage.getCode().equals(code))
                     .findFirst()
                     .orElseThrow();
    }
}
