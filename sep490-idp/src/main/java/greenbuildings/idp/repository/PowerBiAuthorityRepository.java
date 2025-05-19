package greenbuildings.idp.repository;

import greenbuildings.commons.springfw.impl.repositories.AbstractBaseRepository;
import greenbuildings.idp.entity.PowerBiAuthority;

import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PowerBiAuthorityRepository extends AbstractBaseRepository<PowerBiAuthority> {
    
    Optional<PowerBiAuthority> findByApiKey(@NotBlank String apiKey);
    
    List<PowerBiAuthority> findAllByUser_Id(UUID userId);
    
    boolean existsByNote(String note);
}
