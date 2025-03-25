package greenbuildings.enterprise.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
public class PowerBiBasicAuthenticationFilter extends GenericFilter {
    
    public static final String AUTHORIZATION = "Authorization";
    public static final String BASIC = "Basic";
    
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest httpRequest && response instanceof HttpServletResponse) {
            
            authorization(httpRequest);
            
            chain.doFilter(request, response);
            
        } else {
            chain.doFilter(request, response);
        }
    }
    
    public void authorization(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);
        if (header == null) {
            return;
        } else {
            header = header.trim();
            if (!org.springframework.util.StringUtils.startsWithIgnoreCase(header, BASIC)) {
                return;
            } else if (header.equalsIgnoreCase(BASIC)) {
                throw new BadCredentialsException("Empty basic authentication token");
            } else {
                byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
                byte[] decoded = this.decode(base64Token);
                String token = new String(decoded, StandardCharsets.UTF_8);
                int delim = token.indexOf(":");
                if (delim == -1) {
                    throw new BadCredentialsException("Invalid basic authentication token");
                } else {
                    var principle = token.substring(0, delim);
                    var credentials = token.substring(delim + 1);
                    log.info("Principal: {}", principle);
                    log.info("Credentials: {}", credentials);
                    // TODO: Implement authentication by call IdP
                }
            }
        }
    }
    
    private byte[] decode(byte[] base64Token) {
        try {
            return Base64.getDecoder().decode(base64Token);
        } catch (IllegalArgumentException var3) {
            throw new BadCredentialsException("Failed to decode basic authentication token");
        }
    }
}
