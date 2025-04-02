package greenbuildings.commons.api.security;

import greenbuildings.commons.api.BaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PowerBiScope implements BaseEnum {
    BUILDING("building"),
    ENTERPRISE("enterprise");

    private final String code;
}
