package greenbuildings.enterprise.services;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.dtos.InvitationResponseDTO;
import greenbuildings.enterprise.dtos.InvitationSearchCriteria;
import greenbuildings.enterprise.entities.InvitationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InvitationService {
    List<InvitationEntity> findAllByEmail(String username);
    
    void updateStatus(InvitationResponseDTO invitationDTO);
    
    Page<InvitationEntity> search(SearchCriteriaDTO<InvitationSearchCriteria> searchCriteria, Pageable pageable);
}
