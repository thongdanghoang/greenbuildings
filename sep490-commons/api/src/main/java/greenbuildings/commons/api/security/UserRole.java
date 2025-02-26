package greenbuildings.commons.api.security;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import greenbuildings.commons.api.BaseEnum;


@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum UserRole implements BaseEnum {
    ENTERPRISE_OWNER(RoleNameConstant.ENTERPRISE_OWNER),
    ENTERPRISE_EMPLOYEE(RoleNameConstant.ENTERPRISE_EMPLOYEE),
    SYSTEM_ADMIN(RoleNameConstant.SYSTEM_ADMIN);
    
    private final String code;

    
    public static final class RoleNameConstant {
        public static final String ENTERPRISE_OWNER = "ENTERPRISE_OWNER";
        public static final String ENTERPRISE_EMPLOYEE = "ENTERPRISE_EMPLOYEE";
        public static final String SYSTEM_ADMIN = "SYSTEM_ADMIN";

        private RoleNameConstant() {
        }
    }
}
