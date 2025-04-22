package greenbuildings.enterprise.services.impl;

import greenbuildings.enterprise.repositories.InvitationRepository;
import greenbuildings.enterprise.services.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
public class InvitationServiceImpl implements InvitationService {
    
    private final InvitationRepository invitationRepository;
    
}
