package greenbuildings.enterprise.services.impl;

import greenbuildings.commons.api.exceptions.TechnicalException;
import greenbuildings.enterprise.entities.InvitationEntity;
import greenbuildings.enterprise.enums.InvitationStatus;
import greenbuildings.enterprise.repositories.InvitationRepository;
import greenbuildings.enterprise.services.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
public class InvitationServiceImpl implements InvitationService {
    
    private final InvitationRepository invitationRepository;
    
    @Override
    public List<InvitationEntity> findAllByEmail(String username) {
        if (username == null) {
            throw new TechnicalException("Username cannot be null");
        }
        return invitationRepository.findByEmailAndStatusOrderByCreatedByDesc(username, InvitationStatus.PENDING);
    }
}
