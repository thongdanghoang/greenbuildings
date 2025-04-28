package greenbuildings.commons.api.utils;

/**
 * Definition of what can be put into MDC so that later on, can be accessible in its logging context.
 */
public final class MDCContext {
    public static final String CORRELATION_ID = "CorrelationId";
    public static final String TOKEN_ID = "TokenId";

    private MDCContext() {
    }
}
