package greenbuildings.idp.validators;

import greenbuildings.idp.entity.UserEntity;
import greenbuildings.idp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UserValidator {
    
    private final UserRepository userRepository;
    
    public void validateEnterpriseOwnerManageEmployees(UserEntity employee) {

    }
    
}

