package greenbuildings.commons.api.utils;

import org.apache.commons.codec.binary.StringUtils;
import greenbuildings.commons.api.BaseEnum;

import java.util.Arrays;
import java.util.Optional;

public final class EnumUtil {

    private EnumUtil() {
        // Utility class, no instances allowed
    }

    public static <T extends BaseEnum> Optional<T> getCodeFromString(Class<T> clazz, String code) {
        return Arrays.stream(clazz.getEnumConstants())
            .filter(e -> StringUtils.equals(e.getCode(), code))
            .findFirst();
    }

}
