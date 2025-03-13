package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.CreditPackageDTO;
import greenbuildings.enterprise.entities.CreditPackageEntity;
import greenbuildings.enterprise.entities.CreditPackageVersionEntity;
import greenbuildings.enterprise.mappers.decorators.CreditPackageDecorator;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@DecoratedWith(CreditPackageDecorator.class)
public interface CreditPackageMapper {
    
    CreditPackageDTO entityToDTO(CreditPackageVersionEntity creditPackageVersionEntity);
    CreditPackageVersionEntity dtoToCreateCreditPackage(CreditPackageDTO dto);
    CreditPackageVersionEntity dtoToUpdateCreditPackage(CreditPackageEntity entity, CreditPackageDTO dto);
}
