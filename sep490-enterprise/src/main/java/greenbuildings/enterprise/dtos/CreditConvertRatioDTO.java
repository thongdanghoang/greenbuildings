package greenbuildings.enterprise.dtos;

import greenbuildings.enterprise.entities.enums.CreditConvertType;

import java.util.UUID;

public record CreditConvertRatioDTO(UUID id, int version, double ratio, CreditConvertType convertType) {
}
