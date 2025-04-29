package greenbuildings.idp.controller;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import greenbuildings.idp.dto.passkeys.CredentialsRegistration;
import greenbuildings.idp.dto.passkeys.CredentialsVerification;
import greenbuildings.idp.security.MvcUserContextData;
import greenbuildings.idp.service.AuthenticatorService;
import greenbuildings.idp.service.impl.LoginService;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PasskeyController {

    private final AuthenticatorService authenticatorService;
    private final LoginService loginService;
    
    @Hidden
    @PostMapping(value = "/passkey/login")
    @ResponseBody
    public Map<String, String> login(@RequestBody CredentialsVerification verification, SessionStatus sessionStatus, HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        String url = request.getHeader(HttpHeaders.ORIGIN) + request.getContextPath() + "/account";
        try {
            var user = authenticatorService.authenticate(verification);
            loginService.login(user);
            sessionStatus.setComplete(); //Remove challenge in session
        } catch (EntityNotFoundException | NoSuchElementException ex) {
            url = "/login?message=login.error.noPasskey";
        }
        try {
            url = ((DefaultSavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST")).getParameterMap().get("redirect_uri")[0];
        } catch (NullPointerException | IndexOutOfBoundsException ex) {
            // swallow, since this request not called from FE
        }
        map.put("url", url);
        return map;
    }

    @PostMapping("/passkey/register")
    public String register(@RequestBody CredentialsRegistration credentials,
                           @AuthenticationPrincipal MvcUserContextData user) {
        authenticatorService.saveCredentials(credentials, user.getUserEntity());
        return "redirect:/account";
    }

    @PostMapping("/passkey/delete")
    public String login(@NotNull @RequestParam("credential-id") String credentialId, @AuthenticationPrincipal MvcUserContextData userContextData,
                        RedirectAttributes redirectAttributes) {
        if (authenticatorService.deleteCredential(userContextData.getUserEntity(), credentialId)) {
            redirectAttributes.addFlashAttribute("alert", "Passkey deleted.");
        }
        return "redirect:/account";
    }

}
