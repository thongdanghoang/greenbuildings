package greenbuildings.commons.api;

import java.util.UUID;

public interface BaseEntity {
    UUID getId();
    int getVersion();
}
