package greenbuildings.enterprise.services.impl;

import commons.springfw.impl.mappers.CommonMapper;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.dtos.BuildingGroupCriteria;
import greenbuildings.enterprise.dtos.BuildingGroupDTO;
import greenbuildings.enterprise.entities.BuildingGroupEntity;
import greenbuildings.enterprise.mappers.BuildingGroupMapper;
import greenbuildings.enterprise.repositories.BuildingGroupRepository;
import greenbuildings.enterprise.services.BuildingGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BuildingGroupServiceImpl implements BuildingGroupService {
    
    private final BuildingGroupRepository buildingGroupRepository;
    private final BuildingGroupMapper buildingGroupMapper;
    
    @Override
    @Transactional
    public BuildingGroupEntity create(BuildingGroupDTO dto) {
        BuildingGroupEntity entity = buildingGroupMapper.toEntity(dto);
        return buildingGroupRepository.save(entity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<BuildingGroupEntity> findById(UUID id) {
        return buildingGroupRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BuildingGroupEntity> findAll() {
        return buildingGroupRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<BuildingGroupEntity> findAll(Pageable pageable) {
        return buildingGroupRepository.findAll(pageable);
    }
    
    @Override
    @Transactional
    public BuildingGroupEntity update(UUID id, BuildingGroupDTO dto) {
        BuildingGroupEntity existingEntity = buildingGroupRepository.findById(id)
                                                                    .orElseThrow(() -> new RuntimeException("BuildingGroup not found with id: " + id));
        
        buildingGroupMapper.partialUpdate(dto, existingEntity);
        return buildingGroupRepository.save(existingEntity);
    }
    
    @Override
    @Transactional
    public void delete(UUID id) {
        buildingGroupRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BuildingGroupEntity> findByBuildingId(UUID buildingId) {
        return buildingGroupRepository.findByBuildingId(buildingId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BuildingGroupEntity> findByTenantId(UUID tenantId) {
        return buildingGroupRepository.findByTenantId(tenantId);
    }
    
    @Override
    public Page<BuildingGroupEntity> search(SearchCriteriaDTO<BuildingGroupCriteria> searchCriteria) {
        return buildingGroupRepository
                .findAllByBuildingIdAndNameContainingIgnoreCase(searchCriteria.criteria().buildingId(),
                                                                                           Optional.ofNullable(searchCriteria.criteria().name()).orElse(""),
                                                                                           CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort()));
    }
} 