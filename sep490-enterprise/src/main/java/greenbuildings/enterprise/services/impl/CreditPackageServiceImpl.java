package greenbuildings.enterprise.services.impl;

import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.enterprise.entities.CreditPackageEntity;
import greenbuildings.enterprise.entities.CreditPackageVersionEntity;
import greenbuildings.enterprise.repositories.CreditPackageRepository;
import greenbuildings.enterprise.repositories.CreditPackageVersionRepository;
import greenbuildings.enterprise.services.CreditPackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
public class CreditPackageServiceImpl implements CreditPackageService {
    
    private final CreditPackageRepository creditPackageRepository;
    private final CreditPackageVersionRepository creditPackageVersionRepository;
    
    @Override
    public List<CreditPackageVersionEntity> findAll() {
        List<CreditPackageEntity> creditPackageEntityList = creditPackageRepository.findAllByActiveTrue();
        List<CreditPackageVersionEntity> creditPackageVersionEntityList = new ArrayList<>();
        for (CreditPackageEntity creditPackageEntity : creditPackageEntityList) {
            CreditPackageVersionEntity creditPackageVersionEntity = creditPackageVersionRepository.findActiveTrueAndId(creditPackageEntity.getId());
            creditPackageVersionEntityList.add(creditPackageVersionEntity);
        }
        creditPackageVersionEntityList.sort(Comparator.comparing(CreditPackageVersionEntity::getNumberOfCredits));
        return creditPackageVersionEntityList;
    }
    
    @Override
    public Optional<CreditPackageVersionEntity> findById(UUID id) {
        return creditPackageVersionRepository.findById(id);
    }
    
    @Override
    public void createOrUpdate(CreditPackageVersionEntity creditPackageVersionEntity) {
        if (creditPackageVersionEntity.getCreditPackageEntity().getId() == null) {
            var creditPackageEntity = creditPackageVersionEntity.getCreditPackageEntity();
            creditPackageEntity.setActive(true);
            creditPackageRepository.save(creditPackageEntity);
        } else {
            CreditPackageVersionEntity creditPackageVersionEntityBefore = creditPackageVersionRepository.findActiveTrueAndId(
                    creditPackageVersionEntity.getCreditPackageEntity().getId());
            if (creditPackageVersionEntityBefore.getCreditPackageEntity() != null) {
                creditPackageVersionEntityBefore.setActive(false);
                creditPackageVersionRepository.save(creditPackageVersionEntityBefore);
            }
        }
        creditPackageVersionEntity.setActive(true);
        creditPackageVersionRepository.save(creditPackageVersionEntity);
        
    }
    
    
    @Override
    public void deletePackage(UUID packageId) {
        if (packageId == null) {
            throw new BusinessException("packageId", "package.delete.no.id", Collections.emptyList());
        }
        
        CreditPackageVersionEntity creditPackageVersionEntity = creditPackageVersionRepository.findActiveTrueAndIdVersion(packageId);
        if (creditPackageVersionEntity == null) {
            throw new BusinessException("packageId", "package.not.found", Collections.emptyList());
        }
        
        Optional<CreditPackageEntity> creditPackageEntityOptional = creditPackageRepository.findById(
                creditPackageVersionEntity.getCreditPackageEntity().getId());
        if (creditPackageEntityOptional.isPresent()) {
            CreditPackageEntity creditPackageEntity = creditPackageEntityOptional.get();
            creditPackageEntity.setActive(!creditPackageEntity.isActive());
            creditPackageRepository.save(creditPackageEntity);
        } else {
            throw new BusinessException("packageId", "package.entity.not.found", Collections.emptyList());
        }
    }
    
    @Override
    public Page<CreditPackageVersionEntity> search(Pageable pageable) {
        List<CreditPackageEntity> creditPackageEntityList = creditPackageRepository.findAll();
        List<CreditPackageVersionEntity> creditPackageVersionEntityList = new ArrayList<>();
        for (CreditPackageEntity creditPackageEntity : creditPackageEntityList) {
            CreditPackageVersionEntity creditPackageVersionEntity = creditPackageVersionRepository.findActiveTrueAndId(creditPackageEntity.getId());
            creditPackageVersionEntityList.add(creditPackageVersionEntity);
        }
        creditPackageVersionEntityList.sort(Comparator.comparing(CreditPackageVersionEntity::getNumberOfCredits));
        // Convert list to Page
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), creditPackageVersionEntityList.size());
        
        // Handle case where start might be out of bounds
        if (start > creditPackageVersionEntityList.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, creditPackageVersionEntityList.size());
        }
        
        List<CreditPackageVersionEntity> pageContent = creditPackageVersionEntityList.subList(start, end);
        return new PageImpl<>(pageContent, pageable, creditPackageVersionEntityList.size());
    }
}
