package greenbuildings.commons.api.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import greenbuildings.commons.api.BaseEnum;

@Getter
@RequiredArgsConstructor
public enum UserScope implements BaseEnum {
    BUILDING("building"),
    ENTERPRISE("enterprise");

    private final String code;
}
