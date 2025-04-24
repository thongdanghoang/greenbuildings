package greenbuildings.enterprise.services.impl;

import commons.springfw.impl.utils.SecurityUtils;
import greenbuildings.commons.api.exceptions.TechnicalException;
import greenbuildings.enterprise.dtos.InvitationResponseDTO;
import greenbuildings.enterprise.entities.InvitationEntity;
import greenbuildings.enterprise.entities.TenantEntity;
import greenbuildings.enterprise.enums.InvitationStatus;
import greenbuildings.enterprise.repositories.InvitationRepository;
import greenbuildings.enterprise.repositories.TenantRepository;
import greenbuildings.enterprise.services.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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
}
