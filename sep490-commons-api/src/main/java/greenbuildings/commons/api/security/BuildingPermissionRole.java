package greenbuildings.commons.api.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import greenbuildings.commons.api.BaseEnum;

@Getter
@RequiredArgsConstructor
public enum BuildingPermissionRole implements BaseEnum {
    MANAGER("manager"),
    AUDITOR("auditor"),
    STAFF("staff");

    private final String code;
}
