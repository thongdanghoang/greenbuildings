package greenbuildings.commons.api.utils;

import java.security.SecureRandom;

public class CommonUtils {

    private CommonUtils() {
        // Utility class, no instances allowed
    }

    private static final SecureRandom random = new SecureRandom();

    public static String generateRandomOTP(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Size must be at least 1");
        }
        StringBuilder otp = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            int digit = random.nextInt(10);
            otp.append(digit);
        }
        return otp.toString();
    }
    
    public static String alphaNumericString(int len) {
        String AB = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(random.nextInt(AB.length())));
        }
        return sb.toString();
    }

}
