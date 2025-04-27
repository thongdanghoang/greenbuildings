package greenbuildings.enterprise.services.impl;

import greenbuildings.enterprise.dtos.EnterpriseDetailDTO;
import greenbuildings.enterprise.entities.EnterpriseEntity;
import greenbuildings.enterprise.mappers.EnterpriseMapper;
import greenbuildings.enterprise.repositories.EnterpriseRepository;
import greenbuildings.enterprise.services.EnterpriseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Throwable.class)
@RequiredArgsConstructor
public class EnterpriseServiceImpl implements EnterpriseService {
    
    private final EnterpriseRepository repository;
    private final EnterpriseMapper mapper;

    @Override
    public UUID createEnterprise(EnterpriseEntity enterprise) {
        return repository.save(enterprise).getId();
    }
    
    @Override
    public EnterpriseEntity getById(UUID id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public EnterpriseDetailDTO getEnterpriseDetail(UUID id) {
        EnterpriseEntity entity = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Enterprise not found with ID: " + id));
        return mapper.toEnterpriseDetailDTO(entity);
    }

    @Override
    public void updateEnterpriseDetail(EnterpriseDetailDTO dto) {
        EnterpriseEntity entity = repository.findById(dto.id())
                .orElseThrow(() -> new NoSuchElementException("Enterprise not found with ID: " + dto.id()));

        if (entity.getVersion() != dto.version()) {
            throw new RuntimeException("Optimistic lock exception: Entity has been modified");
        }

        mapper.updateEntityFromDetailDTO(dto, entity);
        repository.save(entity);
    }
}
