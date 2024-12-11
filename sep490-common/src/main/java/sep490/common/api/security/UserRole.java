package sep490.common.api.security;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sep490.common.api.BaseEnum;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum UserRole implements BaseEnum {
    ENTERPRISE_OWNER(RoleNameConstant.ENTERPRISE_OWNER),
    SYSTEM_ADMIN(RoleNameConstant.SYSTEM_ADMIN);
    
    private final String code;

    
    public static final class RoleNameConstant {
        public static final String ENTERPRISE_OWNER = "sep390.EnterpriseOwner";
        public static final String SYSTEM_ADMIN = "sep390.SystemAdmin";
        
        private RoleNameConstant() {
        }
    }
}
