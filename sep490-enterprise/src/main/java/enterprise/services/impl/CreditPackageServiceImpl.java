package enterprise.services.impl;

import commons.springfw.impl.utils.SecurityUtils;
import enterprise.dtos.PaymentCriteriaDTO;
import enterprise.entities.CreditPackageEntity;
import enterprise.entities.PaymentEntity;
import enterprise.repositories.CreditPackageRepository;
import enterprise.services.CreditPackageService;
import green_buildings.commons.api.dto.SearchCriteriaDTO;
import green_buildings.commons.api.exceptions.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Throwable.class)
public class CreditPackageServiceImpl implements CreditPackageService {
    
    private final CreditPackageRepository creditPackageRepository;
    
    @Override
    public List<CreditPackageEntity> findAll() {
        return creditPackageRepository.findAllByOrderByNumberOfCreditsAsc();
    }
    
    @Override
    public Optional<CreditPackageEntity> findById(UUID id) {
        return creditPackageRepository.findById(id);
    }

    @Override
    public void createOrUpdate(CreditPackageEntity creditPackage) {
        creditPackageRepository.save(creditPackage);
    }

    @Override
    public void deletePackages(Set<UUID> packageIds) {
        if (CollectionUtils.isEmpty(packageIds)) {
            throw new BusinessException("packageIds", "package.delete.no.ids", Collections.emptyList());
        }
        var creditPackageEntityList = creditPackageRepository.findAllById(packageIds);

        creditPackageRepository.deleteAll(creditPackageEntityList);
    }

    @Override
    public Page<CreditPackageEntity> search(Pageable pageable) {
        return creditPackageRepository.findAll(pageable);
    }
}
