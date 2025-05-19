package greenbuildings.commons.springfw.impl.filters;

import greenbuildings.commons.api.utils.MDCContext;

import jakarta.servlet.FilterChain;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.MDC;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Slf4j
public class MonitoringFilter extends GenericFilter {
    
    public static final String CORRELATION_ID_HEADER_NAME = "X-Correlation-ID";
    public static final String PROCESSING_TIME_ATTRIBUTE = "processing_time";
    public static final long MAX_RESPONSE_TIME = 500;
    
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest httpRequest && response instanceof HttpServletResponse) {
            var requestingCorrelationId = httpRequest.getHeader(CORRELATION_ID_HEADER_NAME);
            if (StringUtils.isBlank(requestingCorrelationId)) {
                MDC.put(MDCContext.CORRELATION_ID, UUID.randomUUID().toString());
            } else {
                MDC.put(MDCContext.CORRELATION_ID, requestingCorrelationId);
            }
            httpRequest.setAttribute(PROCESSING_TIME_ATTRIBUTE, LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());
            
            try {
                chain.doFilter(request, response);
            } finally {
                try {
                    var requestingTime = httpRequest.getAttribute(PROCESSING_TIME_ATTRIBUTE);
                    if (requestingTime != null && NumberUtils.isCreatable(requestingTime.toString())) {
                        var requestingEpoch = NumberUtils.toLong(requestingTime.toString());
                        monitorExecutionTime(requestingEpoch, httpRequest.getRequestURI());
                    }
                } finally {
                    MDC.remove(MDCContext.CORRELATION_ID);
                }
            }
        } else {
            chain.doFilter(request, response);
        }
    }
    
    public static void monitorExecutionTime(long requestingEpochTime, String request) {
        var now = LocalDateTime.now();
        var executionTime = now.toInstant(ZoneOffset.UTC).toEpochMilli() - requestingEpochTime;
        if (executionTime > MAX_RESPONSE_TIME) {
            log.warn("Request `{}` is processed too long. Execution time: {} ms", request, executionTime);
        }
    }
}
