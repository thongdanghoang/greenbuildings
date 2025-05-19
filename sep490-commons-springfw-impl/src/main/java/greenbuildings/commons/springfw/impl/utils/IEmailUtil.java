package greenbuildings.commons.springfw.impl.utils;

public interface IEmailUtil {

    void sendMail(SEPMailMessage mailMessage);

    String maskEmail(String email);
}
