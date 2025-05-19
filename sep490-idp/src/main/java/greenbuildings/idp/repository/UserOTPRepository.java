package greenbuildings.idp.repository;

import greenbuildings.commons.springfw.impl.repositories.AbstractBaseRepository;
import greenbuildings.idp.entity.UserOTP;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserOTPRepository extends AbstractBaseRepository<UserOTP> {

    Optional<UserOTP> findByUserEmail(String userEmail);

}
