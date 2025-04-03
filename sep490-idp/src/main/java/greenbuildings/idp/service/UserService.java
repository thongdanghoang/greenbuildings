package greenbuildings.idp.service;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.exceptions.BusinessErrorResponse;
import greenbuildings.idp.dto.SignupDTO;
import greenbuildings.idp.dto.SignupResult;
import greenbuildings.idp.dto.UserCriteriaDTO;
import greenbuildings.idp.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserService {
    SignupResult signup(SignupDTO signupDTO, Model model);
    
    void commitEnterpriseOwnerCreation(UUID enterpriseId, String correlationId);
    
    void rollbackEnterpriseOwnerCreation(BusinessErrorResponse error);
    
    Page<UserEntity> search(SearchCriteriaDTO<UserCriteriaDTO> searchCriteria);
    
    void deleteUsers(Set<UUID> userIds);
    
    void createOrUpdateEnterpriseUser(UserEntity user);
    
    UserEntity getEnterpriseUserDetail(UUID id);
    
    UserEntity getUserDetail(UUID id);
    
    Optional<UserEntity> findById(UUID id);
    
    Optional<UserEntity> findByEmail(String email);
    
    void update(UserEntity user);

    Page<UserEntity> getUserByBuilding(UUID buldingId, Pageable pageable);
}
