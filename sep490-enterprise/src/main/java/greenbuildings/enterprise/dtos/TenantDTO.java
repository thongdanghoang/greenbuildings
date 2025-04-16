package greenbuildings.enterprise.dtos;

import greenbuildings.commons.api.BaseDTO;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record TenantDTO(
        UUID id,
        int version,
        Set<BuildingGroupDTO> buildingGroups,
        Set<ActivityTypeDTO> activityTypes,
        String name
) implements BaseDTO {
} 