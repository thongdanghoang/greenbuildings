package greenbuildings.idp.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import greenbuildings.idp.dto.passkeys.CredentialsRegistration;
import greenbuildings.idp.dto.passkeys.CredentialsVerification;
import greenbuildings.idp.security.MvcUserContextData;
import greenbuildings.idp.service.AuthenticatorService;
import greenbuildings.idp.service.impl.LoginService;

import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PasskeyController {

    private final AuthenticatorService authenticatorService;
    private final LoginService loginService;

    @PostMapping("/passkey/login")
    public String login(@RequestBody CredentialsVerification verification, SessionStatus sessionStatus) {
        try {
            var user = authenticatorService.authenticate(verification);
            loginService.login(user);
        } catch (EntityNotFoundException | NoSuchElementException ex) {
            return "redirect:/login?message=login.error.noPasskey";
        }

        sessionStatus.setComplete(); //Remove challenge in session
        return "redirect:/account";
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
