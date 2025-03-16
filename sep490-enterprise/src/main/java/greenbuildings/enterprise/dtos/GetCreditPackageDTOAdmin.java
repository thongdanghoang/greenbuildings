package greenbuildings.enterprise.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.With;

import java.util.List;
import java.util.UUID;
@Builder
public record GetCreditPackageDTOAdmin(UUID id, int version, @Positive int numberOfCredits, @Min(1) long price, @With boolean active, @With List<CreditPackageVersionDTO> packageVersionDTOList) {
}
