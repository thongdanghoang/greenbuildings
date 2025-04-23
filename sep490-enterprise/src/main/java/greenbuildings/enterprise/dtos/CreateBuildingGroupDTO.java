package greenbuildings.enterprise.dtos;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateBuildingGroupDTO(String name, String description, UUID buildingId) {
}
