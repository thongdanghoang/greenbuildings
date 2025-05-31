package greenbuildings.enterprise.views.assets;

import java.util.UUID;

public record AssetView(
        UUID id,
        int version,
        String name,
        String code,
        String description,
        AssetBuildingView building
) {
    public record AssetBuildingView(
            UUID id,
            String name
    ) {
    }
}
