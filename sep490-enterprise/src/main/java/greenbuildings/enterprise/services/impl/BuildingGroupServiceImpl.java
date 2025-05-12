package greenbuildings.enterprise.services.impl;

import commons.springfw.impl.mappers.CommonMapper;
import commons.springfw.impl.utils.EmailUtil;
import commons.springfw.impl.utils.IMessageUtil;
import commons.springfw.impl.utils.SEPMailMessage;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.enterprise.dtos.BuildingGroupCriteria;
import greenbuildings.enterprise.dtos.BuildingGroupDTO;
import greenbuildings.enterprise.dtos.InviteTenantToBuildingGroup;
import greenbuildings.enterprise.entities.BuildingGroupEntity;
import greenbuildings.enterprise.entities.EnterpriseEntity;
import greenbuildings.enterprise.entities.InvitationEntity;
import greenbuildings.enterprise.enums.InvitationStatus;
import greenbuildings.enterprise.mappers.BuildingGroupMapper;
import greenbuildings.enterprise.repositories.*;
import greenbuildings.enterprise.services.BuildingGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
public class BuildingGroupServiceImpl implements BuildingGroupService {
    
    private final BuildingGroupRepository buildingGroupRepository;
    private final BuildingGroupMapper buildingGroupMapper;
    private final EnterpriseRepository enterpriseRepository;
    private final InvitationRepository invitationRepository;
    private final EmissionActivityRepository emissionActivityRepository;
    private final EmailUtil emailUtil;
    private final IMessageUtil messageUtil;
    private final TenantRepository tenantRepository;
    
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
    public void delete(Set<UUID> buildingGroupIDs) {
        var activities = emissionActivityRepository
                .findByBuildingGroupIdIn(buildingGroupIDs)
                .stream()
                .peek(activity -> activity.setBuildingGroup(null))
                .toList();
        emissionActivityRepository.saveAll(activities);
        buildingGroupRepository.deleteAllById(buildingGroupIDs);
    }
    
    @Override
    public List<BuildingGroupEntity> findByBuildingId(UUID buildingId) {
        return buildingGroupRepository.findByBuildingId(buildingId);
    }
    
    @Override
    public Page<BuildingGroupEntity> searchByBuildingIdWithTenant(UUID buildingId, Pageable pageable) {
        var buildingGroupsIDs = buildingGroupRepository.findByBuildingId(buildingId, pageable);
        var results = buildingGroupRepository
                .fetchTenants(buildingGroupsIDs.toSet())
                .stream()
                .collect(Collectors.toMap(BuildingGroupEntity::getId, Function.identity()));
        return buildingGroupsIDs.map(results::get);
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
        return buildingGroupRepository.findByBuildingIdAndTenantIsNull(buildingId);
    }
    
    @Override
    public BuildingGroupEntity create(BuildingGroupEntity buildingGroupEntity, String tenantEmail) {
        if (buildingGroupRepository.existsByNameIgnoreCaseAndBuildingId(buildingGroupEntity.getName(), buildingGroupEntity.getBuilding().getId())) {
            throw new BusinessException("name", "business.buildings.group.nameExists");
        }
        buildingGroupEntity = buildingGroupRepository.save(buildingGroupEntity);
        if (tenantEmail != null) {
            inviteTenant(InviteTenantToBuildingGroup.builder()
                                                    .buildingId(buildingGroupEntity.getBuilding().getId())
                                                    .selectedGroupId(buildingGroupEntity.getId())
                                                    .tenantEmail(tenantEmail).build(), false);
        }
        return buildingGroupEntity;
    }
    
    @Override
    public void inviteTenant(InviteTenantToBuildingGroup dto, boolean validateEmail) {
        BuildingGroupEntity group = buildingGroupRepository.findById(dto.selectedGroupId()).orElseThrow();
        // TODO: what about email belong to registered user?
        if (enterpriseRepository.findByEnterpriseEmail(dto.tenantEmail()).isPresent()) {
            throw new BusinessException("tenantEmail", "business.groups.tenantEmail");
        }
        if (buildingGroupRepository.existsByBuildingIdAndTenantEmail(dto.buildingId(), dto.tenantEmail())) {
            throw new BusinessException("tenantEmail", "business.groups.tenantEmailLinked");
        }
        if (invitationRepository.existsByBuildingGroupBuildingIdAndEmailAndStatus(dto.buildingId(), dto.tenantEmail(), InvitationStatus.PENDING)) {
            throw new BusinessException("tenantEmail", "business.groups.tenantEmailPending");
        }
        if(validateEmail && dto.tenantEmail().isEmpty()) {
            throw new BusinessException("tenantEmail", "business.groups.noFindEmailTenant");
        }
        InvitationEntity invitation = InvitationEntity.builder()
                                                      .buildingGroup(group)
                                                      .status(InvitationStatus.PENDING)
                                                      .email(dto.tenantEmail())
                                                      .build();
        SEPMailMessage message = createInviteMailMessage(invitation, dto.tenantEmail());
        emailUtil.sendMail(message);
        invitationRepository.save(invitation);

    }
    
    @Override
    public void unlinkTenant(UUID groupId) {
        BuildingGroupEntity buildingGroupEntity = buildingGroupRepository.findById(groupId).orElseThrow();
        if (buildingGroupEntity.getTenant() != null) {
            SEPMailMessage message = createUnlinkTenantMailMessage(buildingGroupEntity);
            buildingGroupEntity.setTenant(null);
            emailUtil.sendMail(message);
            buildingGroupRepository.save(buildingGroupEntity);
        }
    }
    
    private SEPMailMessage createUnlinkTenantMailMessage(BuildingGroupEntity buildingGroupEntity) {
        SEPMailMessage message = new SEPMailMessage();
        
        message.setTemplateName("unlink-tenant.ftl");
        message.setTo(buildingGroupEntity.getTenant().getEmail());
        message.setSubject(messageUtil.getMessage("unlinkTenant.title"));
        
        message.addTemplateModel("subject", messageUtil.getMessage("unlinkTenant.title"));
        message.addTemplateModel("tenantName", buildingGroupEntity.getTenant().getName());
        message.addTemplateModel("groupName", buildingGroupEntity.getName());
        message.addTemplateModel("ownerEmail", buildingGroupEntity.getBuilding().getEnterprise().getEnterpriseEmail());
        message.addTemplateModel("appName", "GreenBuildings");
        
        return message;
    }

    private SEPMailMessage createInviteMailMessage(InvitationEntity invitation, String tenantEmail) {
        SEPMailMessage message = new SEPMailMessage();

        message.setTemplateName("invite-tenant.ftl");
        message.setTo(invitation.getEmail());
        message.setSubject(messageUtil.getMessage("invite.title"));

        message.addTemplateModel("subject", messageUtil.getMessage("invite.title"));
        message.addTemplateModel("tenantName", tenantEmail);
        message.addTemplateModel("buildingGroupName", invitation.getBuildingGroup().getName());
        message.addTemplateModel("buildingName", invitation.getBuildingGroup().getBuilding().getName());
        message.addTemplateModel("appName", "GreenBuildings");
        message.addTemplateModel("appUrl", "https://greenbuildings.cloud");
        EnterpriseEntity enterprise = enterpriseRepository.findByBuidingId(invitation.getBuildingGroup().getBuilding().getId()).orElseThrow();
        message.addTemplateModel("ownerEmail", enterprise.getEnterpriseEmail());

        return message;
    }
} 