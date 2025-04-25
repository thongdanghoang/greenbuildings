package greenbuildings.enterprise.services.impl;

import commons.springfw.impl.utils.SecurityUtils;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.exceptions.TechnicalException;
import greenbuildings.enterprise.dtos.InvitationResponseDTO;
import greenbuildings.enterprise.dtos.InvitationSearchCriteria;
import greenbuildings.enterprise.entities.InvitationEntity;
import greenbuildings.enterprise.entities.TenantEntity;
import greenbuildings.enterprise.enums.InvitationStatus;
import greenbuildings.enterprise.repositories.InvitationRepository;
import greenbuildings.enterprise.repositories.TenantRepository;
import greenbuildings.enterprise.services.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
public class InvitationServiceImpl implements InvitationService {
    
    private final InvitationRepository invitationRepository;
    private final TenantRepository tenantRepository;
    
    @Override
    public List<InvitationEntity> findAllByEmail(String username) {
        if (username == null) {
            throw new TechnicalException("Username cannot be null");
        }
        return invitationRepository.findByEmailAndStatusOrderByCreatedByDesc(username, InvitationStatus.PENDING);
    }
    
    @Override
    public void updateStatus(InvitationResponseDTO invitationDTO) {
        InvitationEntity invitation = invitationRepository.findById(invitationDTO.id()).orElseThrow();
        invitation.setStatus(invitationDTO.status());
        if (invitationDTO.status() == InvitationStatus.ACCEPTED) {
            UUID tenantId = SecurityUtils.getCurrentUserEnterpriseId().orElseThrow();
            TenantEntity tenant = tenantRepository.findById(tenantId).orElseThrow();
            invitation.getBuildingGroup().setTenant(tenant);
        }
        // TODO: send mails
        invitationRepository.save(invitation);
    }
    
    @Override
    public Page<InvitationEntity> search(SearchCriteriaDTO<InvitationSearchCriteria> searchCriteria, Pageable pageable) {
        InvitationSearchCriteria criteria = searchCriteria.criteria();
        Page<UUID> ids = invitationRepository.search(criteria.enterpriseId(), criteria.buildingId(),
                                                     criteria.buildingGroupId(), criteria.status(),
                                                     criteria.tenantEmail(), pageable);
        Map<UUID, InvitationEntity> entityMap = invitationRepository.findAllById(ids).stream()
                                                                    .collect(Collectors.toMap(InvitationEntity::getId, Function.identity()));
        
        // Preserve original order
        List<InvitationEntity> orderedResults = ids.stream()
                                                   .map(entityMap::get)
                                                   .filter(Objects::nonNull) // safeguard in case of missing entity
                                                   .toList();
        
        return new PageImpl<>(orderedResults, pageable, ids.getTotalElements());
    }
}
