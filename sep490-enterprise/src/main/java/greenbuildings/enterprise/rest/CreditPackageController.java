package greenbuildings.enterprise.rest;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.dto.SearchResultDTO;
import greenbuildings.commons.api.security.UserRole;
import greenbuildings.commons.springfw.impl.mappers.CommonMapper;
import greenbuildings.enterprise.dtos.CreditPackageDTO;
import greenbuildings.enterprise.dtos.GetCreditPackageDTOAdmin;
import greenbuildings.enterprise.entities.CreditPackageEntity;
import greenbuildings.enterprise.entities.CreditPackageVersionEntity;
import greenbuildings.enterprise.mappers.CreditPackageMapper;
import greenbuildings.enterprise.services.CreditPackageService;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/credit-package")
@RequiredArgsConstructor
public class CreditPackageController {
    
    private final CreditPackageService creditPackageService;
    private final CreditPackageMapper mapper;
    
    @GetMapping
    @RolesAllowed({UserRole.RoleNameConstant.SYSTEM_ADMIN, UserRole.RoleNameConstant.ENTERPRISE_OWNER})
    public List<CreditPackageDTO> findAll() {
        return creditPackageService.findAll()
                                   .stream()
                                   .map(mapper::entityToDTO)
                                   .toList();
    }
    
    @GetMapping("/{id}")
    @RolesAllowed({UserRole.RoleNameConstant.SYSTEM_ADMIN, UserRole.RoleNameConstant.ENTERPRISE_OWNER})
    public ResponseEntity<CreditPackageDTO> findById(@PathVariable UUID id) {
        CreditPackageDTO creditPackageDTO = creditPackageService.findById(id)
                                                                .map(mapper::entityToDTO)
                                                                .orElseThrow();
        return ResponseEntity.ok(creditPackageDTO);
    }

    @PostMapping()
    @RolesAllowed({UserRole.RoleNameConstant.SYSTEM_ADMIN})
    public ResponseEntity<Void> createCreditPackage(@RequestBody CreditPackageDTO creditPackageDTO) {
        if (Objects.isNull(creditPackageDTO.id())) {
            return createNewCreditPackage(creditPackageDTO);
        }

        return updateExistingCreditPackage(creditPackageDTO)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private ResponseEntity<Void> createNewCreditPackage(CreditPackageDTO creditPackageDTO) {
        var creditPackageVersionEntity = mapper.dtoToCreateCreditPackage(creditPackageDTO);
        return saveUserAndReturnResponse(creditPackageVersionEntity, HttpStatus.CREATED);
    }

    private Optional<ResponseEntity<Void>> updateExistingCreditPackage(CreditPackageDTO creditPackageDTO) {
        return creditPackageService.findById(creditPackageDTO.id())
                .map(existingEntity -> {
                    CreditPackageEntity creditPackageEntity = existingEntity.getCreditPackageEntity();
                    CreditPackageVersionEntity updatedEntity = mapper.dtoToUpdateCreditPackage(creditPackageEntity, creditPackageDTO);
                    return saveUserAndReturnResponse(updatedEntity, HttpStatus.OK);
                });
    }


    private ResponseEntity<Void> saveUserAndReturnResponse(CreditPackageVersionEntity entity, HttpStatus status) {
        creditPackageService.createOrUpdate(entity);
        return ResponseEntity.status(status).build();
    }

    @DeleteMapping
    @RolesAllowed({UserRole.RoleNameConstant.SYSTEM_ADMIN})
    public ResponseEntity<Void> deleteCreditPackages(@RequestBody Set<UUID> packageIds) {
        UUID firstPackageId = packageIds.iterator().next();
        creditPackageService.deletePackage(firstPackageId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/search")
    @RolesAllowed({UserRole.RoleNameConstant.SYSTEM_ADMIN})
    public ResponseEntity<SearchResultDTO<GetCreditPackageDTOAdmin>> search(@RequestBody SearchCriteriaDTO<Void> searchCriteria ) {
        var pageable = CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort());
        var searchResults = creditPackageService.search(pageable);
        return ResponseEntity.ok(
                CommonMapper.toSearchResultDTO(
                        searchResults,
                        mapper::entityToDTOAdmin));
    }

    
}
