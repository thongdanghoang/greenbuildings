package greenbuildings.enterprise.services.impl;

import commons.springfw.impl.mappers.CommonMapper;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.enterprise.dtos.BuildingGroupCriteria;
import greenbuildings.enterprise.dtos.BuildingGroupDTO;
import greenbuildings.enterprise.dtos.InviteTenantToBuildingGroup;
import greenbuildings.enterprise.entities.BuildingGroupEntity;
import greenbuildings.enterprise.entities.InvitationEntity;
import greenbuildings.enterprise.enums.InvitationStatus;
import greenbuildings.enterprise.mappers.BuildingGroupMapper;
import greenbuildings.enterprise.repositories.BuildingGroupRepository;
import greenbuildings.enterprise.repositories.EnterpriseRepository;
import greenbuildings.enterprise.repositories.InvitationRepository;
import greenbuildings.enterprise.repositories.TenantRepository;
import greenbuildings.enterprise.services.BuildingGroupService;
import greenbuildings.enterprise.services.IdpClientService;
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
@Transactional(rollbackFor = Throwable.class)
public class BuildingGroupServiceImpl implements BuildingGroupService {
    
    private final BuildingGroupRepository buildingGroupRepository;
    private final BuildingGroupMapper buildingGroupMapper;
    private final TenantServiceImpl tenantService;
    private final IdpClientService idpClientService;
    private final TenantRepository tenantRepository;
    private final EnterpriseRepository enterpriseRepository;
    private final InvitationRepository invitationRepository;
    
    @Override
    public BuildingGroupEntity create(BuildingGroupDTO dto) {
        BuildingGroupEntity entity = buildingGroupMapper.toEntity(dto);
        return buildingGroupRepository.save(entity);
    }
    
    @Override
    public Optional<BuildingGroupEntity> findById(UUID id) {
        return buildingGroupRepository.findById(id);
    }
    
    @Override
    public List<BuildingGroupEntity> findAll() {
        return buildingGroupRepository.findAll();
    }
    
    @Override
    public Page<BuildingGroupEntity> findAll(Pageable pageable) {
        return buildingGroupRepository.findAll(pageable);
    }
    
    @Override
    public BuildingGroupEntity update(UUID id, BuildingGroupDTO dto) {
        BuildingGroupEntity existingEntity = buildingGroupRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("BuildingGroup not found with id: " + id));
        
        buildingGroupMapper.partialUpdate(dto, existingEntity);
        return buildingGroupRepository.save(existingEntity);
    }
    
    @Override
    public void delete(UUID id) {
        buildingGroupRepository.deleteById(id);
    }
    
    @Override
    public List<BuildingGroupEntity> findByBuildingId(UUID buildingId) {
        return buildingGroupRepository.findByBuildingId(buildingId);
    }
    
    @Override
    public List<BuildingGroupEntity> findByTenantId(UUID tenantId) {
        return buildingGroupRepository.findByTenantId(tenantId);
    }
    
    @Override
    public Page<BuildingGroupEntity> search(SearchCriteriaDTO<BuildingGroupCriteria> searchCriteria) {
        return buildingGroupRepository
                .findAllByBuildingIdAndNameContainingIgnoreCase(
                        searchCriteria.criteria().buildingId(),
                        Optional.ofNullable(searchCriteria.criteria().name()).orElse(""),
                        CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort()));
    }
    
    @Override
    public List<BuildingGroupEntity> getAvailableBuildingGroups(UUID buildingId) {
        return buildingGroupRepository.findAvailableGroupForInvite(buildingId);
    }
    
    @Override
    public BuildingGroupEntity create(BuildingGroupEntity buildingGroupEntity) {
        if (buildingGroupRepository.existsByNameIgnoreCaseAndBuildingId(buildingGroupEntity.getName(), buildingGroupEntity.getBuilding().getId())) {
            throw new BusinessException("name", "business.buildings.group.nameExists");
        }
        return buildingGroupRepository.save(buildingGroupEntity);
    }
    
    @Override
    public void inviteTenant(InviteTenantToBuildingGroup dto) {
        BuildingGroupEntity group = buildingGroupRepository.findById(dto.selectedGroupId()).orElseThrow();
        // TODO: what about email belong to registered user?
        if (enterpriseRepository.findByEnterpriseEmail(dto.tenantEmail()).isPresent()) {
            throw new BusinessException("tenantEmail", "business.groups.tenantEmail");
        }
        InvitationEntity invitation = InvitationEntity.builder()
                                                      .buildingGroup(group)
                                                      .status(InvitationStatus.PENDING)
                                                      .email(dto.tenantEmail())
                                                      .build();
        invitationRepository.save(invitation);
    }
} 