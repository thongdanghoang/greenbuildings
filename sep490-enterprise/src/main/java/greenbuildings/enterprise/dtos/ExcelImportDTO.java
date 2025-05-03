package greenbuildings.enterprise.dtos;

import java.util.UUID;

public record ExcelImportDTO(
        UUID id,
        String fileName,
        String contentType
) {
}
