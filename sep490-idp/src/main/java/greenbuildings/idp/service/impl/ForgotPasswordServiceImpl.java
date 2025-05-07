package greenbuildings.idp.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import greenbuildings.idp.dto.ForgotResetPasswordDTO;
import greenbuildings.idp.entity.UserEntity;
import greenbuildings.idp.entity.UserOTP;
import greenbuildings.idp.repository.UserOTPRepository;
import greenbuildings.idp.repository.UserRepository;
import greenbuildings.idp.service.ForgotPasswordService;
import commons.springfw.impl.utils.IEmailUtil;
import commons.springfw.impl.utils.IMessageUtil;
import commons.springfw.impl.utils.SEPMailMessage;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(rollbackFor = Throwable.class)
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private final UserRepository userRepo;
    private final UserOTPRepository otpRepo;
    private final IEmailUtil emailUtil;
    private final IMessageUtil messageUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean sendResetPasswordEmail(String email) {
        UserEntity user = userRepo.findByEmail(email).orElse(null);
        if (user == null) {
            return false;
        }
        UserOTP userOTP = otpRepo.findByUserEmail(email).orElse(new UserOTP());
        userOTP.updateOtp(user);
        otpRepo.save(userOTP);
        SEPMailMessage message = populateOTPMailMessage(email, userOTP);
        emailUtil.sendMail(message);
        return true;
    }

    @Override
    public boolean verifyOtp(String otpCode, String email) {
        UserOTP userOTP = otpRepo.findByUserEmail(email).orElse(null);
        return userOTP != null
            && userOTP.getOtpCode().equals(otpCode)
            && LocalDateTime.now().isBefore(userOTP.getExpiredTime());
    }

    @Override
    public boolean changePassword(ForgotResetPasswordDTO resetPasswordDTO, String email) {
        if (!resetPasswordDTO.getPassword().equals(resetPasswordDTO.getConfirmPassword()) || StringUtils.isEmpty(email)) {
            return false;
        }
        UserOTP userOTP = otpRepo.findByUserEmail(email).orElseThrow();
        UserEntity user = userOTP.getUser();

        user.setPassword(passwordEncoder.encode(resetPasswordDTO.getPassword()));
        otpRepo.delete(userOTP);

        return true;
    }

    private SEPMailMessage populateOTPMailMessage(String email, UserOTP userOTP) {
        SEPMailMessage message = new SEPMailMessage();

        message.setTemplateName("forgot-password-otp.ftl");
        message.setTo(email);
        message.setSubject(messageUtil.getMessage("resetPassword.title"));
        
        message.addTemplateModel("userEmail", email);
        message.addTemplateModel("otpCode", userOTP.getOtpCode());

        return message;
    }
}
