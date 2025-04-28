package greenbuildings.commons.api.utils;

public final class CommonConstant {
    
    // RFC5322
    public static final String EMAIL_PATTERN =
            "^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\""
            + "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9]"
            + "(?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}"
            + "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:"
            + "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$";

    public static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d\\W]{8,}$";
    // 8 characters at least one letter and one number

    public static final String VIETNAM_PHONE_PATTERN = "^[0]{1}[0-9]{9}$";
    public static final String VIETNAM_ENTERPRISE_HOTLINE_PATTERN = "^(1800\\d{4}|1900\\d{4}|0\\d{9})$";
    
    // Regex for Vietnamese enterprise tax code: 10 digits or 10 digits + "-XXX"
    public static final String VIETNAME_TAX_CODE = "^\\d{10}(-\\d{3})?$";

    public static final int DEFAULT_PAGE_SIZE = 100;
    public static final int MAX_PAGE_SIZE = 1000;
    
    private CommonConstant() {
        // Utility class. No instantiation allowed.
    }
}
