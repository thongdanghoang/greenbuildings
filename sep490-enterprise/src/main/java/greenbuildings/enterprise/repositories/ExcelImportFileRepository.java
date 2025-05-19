package greenbuildings.enterprise.repositories;

import greenbuildings.commons.springfw.impl.repositories.AbstractBaseRepository;
import greenbuildings.enterprise.entities.ExcelImportFileEntity;
import greenbuildings.enterprise.enums.ImportExcelType;

import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ExcelImportFileRepository extends AbstractBaseRepository<ExcelImportFileEntity> {
    Optional<Object> findByType(ImportExcelType type);
}