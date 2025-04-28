package greenbuildings.commons.api.exceptions;

import java.io.Serializable;

/**
 * A record that represents a parameter for a business error.
 */
public record BusinessErrorParam(
        String key,
        Object value
) implements Serializable {
}
