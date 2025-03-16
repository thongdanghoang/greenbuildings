package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.CreditPackageVersionDTO;
import greenbuildings.enterprise.entities.CreditPackageVersionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CreditPackageVersionMapper {

    CreditPackageVersionDTO entityToDTO(CreditPackageVersionEntity creditPackageVersionEntity);
}
