package greenbuildings.enterprise.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ActivityTypeCriteriaDTO(String name, @NotNull UUID buildingId) {
}
