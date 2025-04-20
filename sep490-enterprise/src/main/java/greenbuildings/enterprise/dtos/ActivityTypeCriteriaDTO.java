package greenbuildings.enterprise.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ActivityTypeCriteriaDTO(String criteria, @NotNull UUID buildingGroupId) {
}
