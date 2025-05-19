package greenbuildings.idp.repository;

import greenbuildings.commons.springfw.impl.repositories.AbstractBaseRepository;
import greenbuildings.idp.entity.UserAuthenticator;
import greenbuildings.idp.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserAuthenticatorRepository extends AbstractBaseRepository<UserAuthenticator> {
    
    Optional<UserAuthenticator> findByCredentialId(String credentialId);
    
    List<UserAuthenticator> findUserAuthenticatorByUser(UserEntity user);
    
    long deleteByCredentialIdAndUser(String id, UserEntity user);
    
}