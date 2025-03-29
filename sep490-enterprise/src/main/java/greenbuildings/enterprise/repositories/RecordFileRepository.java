package greenbuildings.enterprise.repositories;

import commons.springfw.impl.repositories.AbstractBaseRepository;
import greenbuildings.enterprise.entities.RecordFileEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RecordFileRepository extends AbstractBaseRepository<RecordFileEntity> {
    List<RecordFileEntity> findByRecordId(UUID recordId);
} 