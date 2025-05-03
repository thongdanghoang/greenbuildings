package greenbuildings.enterprise.dtos;

import java.util.UUID;

public record TenantDetailDTO(
        UUID id,
        int version,
        String name,
        String email,
        String address,
        String hotline,
        String taxCode,
        String businessLicenseImageUrl,
        String representativeName,
        String representativePosition,
        String representativeContact
) {
}
