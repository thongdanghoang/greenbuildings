package greenbuildings.enterprise.services.impl;

import greenbuildings.enterprise.dtos.ActivityTypeDTO;
import greenbuildings.enterprise.entities.ActivityTypeEntity;
import greenbuildings.enterprise.mappers.ActivityTypeMapper;
import greenbuildings.enterprise.repositories.ActivityTypeRepository;
import greenbuildings.enterprise.services.ActivityTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
public class ActivityTypeServiceImpl implements ActivityTypeService {
    
    private final ActivityTypeRepository repository;
    private final ActivityTypeMapper mapper;
    
    @Override
    public List<ActivityTypeEntity> findAll() {
        return repository.findAll();
    }
    
    @Override
    public List<ActivityTypeEntity> findByEnterpriseId(UUID entepriseId) {
        return repository.findByEnterpriseId(entepriseId);
    }
    
    @Override
    public ActivityTypeEntity create(ActivityTypeDTO dto) {
        ActivityTypeEntity entity = mapper.toEntity(dto);
        return repository.save(entity);
    }
} 