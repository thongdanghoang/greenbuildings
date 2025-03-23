package greenbuildings.idp.exceptions.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "greenbuildings.idp.controller")
@Slf4j
public class MvcExceptionHandler {
    @Value("${spring.application.homepage}")
    private String frontendUrl;

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        log.error(ex.getMessage(), ex);
        model.addAttribute("error", "An unexpected error occurred, please contact admin!");
        model.addAttribute("frontendUrl", frontendUrl);
        return "error";
    }
}
