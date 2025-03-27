package greenbuildings.enterprise.dtos;

import java.util.UUID;

public record RecordFileDTO(
    UUID id,
    String fileName,
    String contentType
) {} 