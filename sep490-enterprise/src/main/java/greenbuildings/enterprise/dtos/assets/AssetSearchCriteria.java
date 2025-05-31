package greenbuildings.enterprise.dtos.assets;

import java.util.List;
import java.util.UUID;

public record AssetSearchCriteria(
        String keyword,
        List<UUID> buildings
) {
}
