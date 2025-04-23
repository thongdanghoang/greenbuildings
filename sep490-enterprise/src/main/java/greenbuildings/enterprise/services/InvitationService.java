package greenbuildings.enterprise.services;

import greenbuildings.enterprise.entities.InvitationEntity;

import java.util.List;

public interface InvitationService {
    List<InvitationEntity> findAllByEmail(String username);
}
