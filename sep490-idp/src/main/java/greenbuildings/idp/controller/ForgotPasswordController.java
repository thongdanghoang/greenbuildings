package greenbuildings.idp.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import greenbuildings.idp.dto.ForgotPasswordDTO;
import greenbuildings.idp.dto.ForgotResetPasswordDTO;
import greenbuildings.idp.dto.OtpDTO;
import greenbuildings.idp.limiter.ForgotPasswordLimiter;
import greenbuildings.idp.service.ForgotPasswordService;
import commons.springfw.impl.utils.IEmailUtil;
import commons.springfw.impl.utils.IMessageUtil;


@Controller
@RequiredArgsConstructor
public class ForgotPasswordController {
    
    private final ForgotPasswordService forgotPasswordService;
    private final IMessageUtil messageUtil;
    private final HttpSession session;
    private final IEmailUtil emailUtil;
    private final ForgotPasswordLimiter rateLimiter;
    
    private static final String SESSION_OTP_SENT = "otp_sent";
    private static final String SESSION_OTP_VERIFIED = "otp_verified";
    private static final String SESSION_FORGOT_PASSWORD_EMAIL = "forgot_password_email";
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String MESSAGE = "message";
    
    @GetMapping("/forgot-password")
    public String forgotPasswordPage(Model model) {
        session.setAttribute(SESSION_OTP_SENT, false);
        session.setAttribute(SESSION_OTP_VERIFIED, false);
        // add message
        model.addAttribute("forgotPasswordDTO", new ForgotPasswordDTO(null));
        return "forgot-password";
    }
    
    @PostMapping("/forgot-password")
    public String processForgotPassword(@ModelAttribute ForgotPasswordDTO forgotPasswordDTO,
                                        RedirectAttributes redirectAttributes) {
        String email = forgotPasswordDTO.email();
        if (!rateLimiter.isSubmitEmailPermitted(email)) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, messageUtil.getMessage("forgotPassword.error.rateLimited"));
            return "redirect:/forgot-password";
        } else if (!rateLimiter.isSubmitOTPAvailable(email)) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, messageUtil.getMessage("forgotPassword.error.otpTempLock"));
            return "redirect:/forgot-password";
        }
        if (forgotPasswordService.sendResetPasswordEmail(email)) {
            session.setAttribute(SESSION_FORGOT_PASSWORD_EMAIL, email);
            session.setAttribute(SESSION_OTP_SENT, true);
            return "redirect:/enter-otp";
        } else {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, messageUtil.getMessage("forgotPassword.error.noUser"));
            return "redirect:/forgot-password";
        }
    }
    
    @GetMapping("/enter-otp")
    public String showOtpPage(Model model) {
        Boolean otpSent = (Boolean) session.getAttribute(SESSION_OTP_SENT);
        String email = (String) session.getAttribute(SESSION_FORGOT_PASSWORD_EMAIL);
        if (otpSent == null || !otpSent || email == null) {
            return "redirect:/login";
        }
        String maskEmail = emailUtil.maskEmail(email);
        model.addAttribute("otpDTO", new OtpDTO());
        model.addAttribute("email", maskEmail);
        return "enter-otp";
    }
    
    @PostMapping("/enter-otp")
    public String verifyOtp(@ModelAttribute OtpDTO otpDTO, Model model, RedirectAttributes redirectAttributes) {
        String email = (String) session.getAttribute(SESSION_FORGOT_PASSWORD_EMAIL);
        if (!rateLimiter.isSubmitOTPPermitted(email)) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, messageUtil.getMessage("forgotPassword.error.otpRateLimited"));
            return "redirect:/forgot-password";
        }
        boolean isValid = forgotPasswordService.verifyOtp(otpDTO.getOtpCode(), email);
        if (isValid) {
            session.setAttribute(SESSION_OTP_VERIFIED, true);
            return "redirect:/forgot-reset-password";
        }
        model.addAttribute(ERROR_MESSAGE, messageUtil.getMessage("forgotPassword.error.invalidOtp"));
        String maskEmail = emailUtil.maskEmail(email);
        model.addAttribute("email", maskEmail);
        return "enter-otp";
    }
    
    @GetMapping("/forgot-reset-password")
    public String showResetPasswordPage(Model model) {
        Boolean otpVerified = (Boolean) session.getAttribute(SESSION_OTP_VERIFIED);
        if (otpVerified == null || !otpVerified) {
            return "redirect:/login";
        }
        
        model.addAttribute("resetPasswordDTO", new ForgotResetPasswordDTO());
        return "forgot-reset-password";
    }
    
    @PostMapping("/forgot-reset-password")
    public String resetPassword(@Valid @ModelAttribute("resetPasswordDTO") ForgotResetPasswordDTO resetPasswordDTO,
                                BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "forgot-reset-password";
        }
        
        if (!forgotPasswordService.changePassword(resetPasswordDTO, (String) session.getAttribute(SESSION_FORGOT_PASSWORD_EMAIL))) {
            model.addAttribute(ERROR_MESSAGE, messageUtil.getMessage("validation.password.invalid.notmatch"));
            return "enter-otp";
        }
        
        redirectAttributes.addFlashAttribute(MESSAGE, messageUtil.getMessage("forgotPassword.notification"));
        return "redirect:/login";
    }
}



