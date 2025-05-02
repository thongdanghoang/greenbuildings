package greenbuildings.enterprise.services.impl;

import greenbuildings.enterprise.dtos.InvitationResponseDTO;
import greenbuildings.enterprise.dtos.InvitationSearchCriteria;
import greenbuildings.enterprise.entities.InvitationEntity;
import greenbuildings.enterprise.enums.InvitationStatus;
import greenbuildings.enterprise.repositories.BuildingGroupRepository;
import greenbuildings.enterprise.repositories.InvitationRepository;
import greenbuildings.enterprise.repositories.TenantRepository;
import greenbuildings.enterprise.services.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
public class InvitationServiceImpl implements InvitationService {
    
    private final InvitationRepository invitationRepository;
    private final TenantRepository tenantRepository;
    private final BuildingGroupRepository buildingGroupRepository;
    
    @Override
    public List<InvitationEntity> findAllByEmail(String email) {
        var invitations = invitationRepository.findByEmailOrderByCreatedByDesc(email);
        invitations.sort(Comparator.comparingInt(i -> i.getStatus().ordinal()));
        return invitations;
    }
    
    @Override
    public void updateStatus(InvitationResponseDTO invitationDTO, Optional<UUID> tenantId) {
        var invitation = invitationRepository.findById(invitationDTO.id()).orElseThrow();
        invitation.setStatus(invitationDTO.status());
        if (invitationDTO.status() == InvitationStatus.ACCEPTED) {
            var tenant = tenantId
                    .flatMap(tenantRepository::findById)
                    .orElseThrow();
            var buildingGroup = buildingGroupRepository
                    .findById(invitation.getBuildingGroup().getId())
                    .orElseThrow();
            buildingGroup.setTenant(tenant);
            invitation.getBuildingGroup().setTenant(tenant);
            var pendingInvitationsByBuildingGroup = invitationRepository
                    .findByBuildingGroupIdAndStatusAndIdIsNot(
                            invitation.getBuildingGroup().getId(),
                            InvitationStatus.PENDING,
                            invitation.getId())
                    .stream()
                    .peek(invitationEntity -> invitationEntity.setStatus(InvitationStatus.REJECTED))
                    .toList();
            invitationRepository.saveAll(pendingInvitationsByBuildingGroup);
        }
        // TODO: send mails
        invitationRepository.save(invitation);
    }
    
    @Override
    public Page<InvitationEntity> search(InvitationSearchCriteria criteria, Pageable pageable) {
        var sortByLastModifiedDate = Sort.by(Sort.Direction.DESC, "lastModifiedDate");
        var pageableWithSort = pageable.getSort().isSorted()
                               ? PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().and(sortByLastModifiedDate))
                               : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortByLastModifiedDate);
        var invitationIDs = invitationRepository
                .search(criteria.enterpriseId(), criteria.buildingId(),
                        criteria.buildingGroupId(), criteria.status(),
                        criteria.tenantEmail(), pageableWithSort);
        var entityMap = invitationRepository
                .findAllById(invitationIDs).stream()
                .collect(Collectors.toMap(InvitationEntity::getId, Function.identity()));
        
        // Preserve original order
        var orderedResults = invitationIDs.stream()
                                          .map(entityMap::get)
                                          .filter(Objects::nonNull) // safeguard in case of missing entity
                                          .toList();
        
        return new PageImpl<>(orderedResults, pageable, invitationIDs.getTotalElements());
    }
    
    @Override
    public InvitationEntity findPendingInvitationByBuildingGroupId(UUID id) {
        return this.invitationRepository.findFirstByBuildingGroupIdAndStatus(id, InvitationStatus.PENDING);
    }
}
