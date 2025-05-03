package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.ExcelImportDTO;
import greenbuildings.enterprise.dtos.RecordFileDTO;
import greenbuildings.enterprise.entities.ExcelImportFileEntity;
import greenbuildings.enterprise.entities.RecordFileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExcelImportMapper {
    
    ExcelImportDTO toDto(ExcelImportFileEntity entity);
} 