package greenbuildings.enterprise.dtos;

import java.util.UUID;

public record CreateBuildingGroupDTO(String name, String description, UUID buildingId) {
}
