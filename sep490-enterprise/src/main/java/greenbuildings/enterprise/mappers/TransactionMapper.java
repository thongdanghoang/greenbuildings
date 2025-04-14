package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.TransactionDTO;
import greenbuildings.enterprise.entities.TransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionMapper {
    TransactionDTO toDTO(TransactionEntity entity);
}
