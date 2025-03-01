package greenbuildings.idp.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

@Slf4j
public class MvcAuthenticationFailureHandler implements AuthenticationFailureHandler {
    
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        
        if (exception.getClass().isAssignableFrom(UsernameNotFoundException.class)
            || exception.getClass().isAssignableFrom(BadCredentialsException.class)) {
            String errorKey = "login.error.badCredentials";
            String contextPath = request.getContextPath();
            if (contextPath.isEmpty()) {
                log.warn("Missing context path");
            }
            
            response.sendRedirect(contextPath + "/login?error=" + errorKey);
        }
        
    }
}
