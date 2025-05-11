package greenbuildings.enterprise.services.impl;

import greenbuildings.enterprise.dtos.TransactionEnterpriseAdminDTO;
import greenbuildings.enterprise.entities.TransactionEntity;
import greenbuildings.enterprise.repositories.TransactionRepository;
import greenbuildings.enterprise.services.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@Transactional(rollbackFor = Throwable.class)
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    
    @Override
    public List<TransactionEntity> getTransactions(UUID buildingId, UUID enterpriseId) {
        return transactionRepository.findAllByBuilding_IdAndEnterprise_Id(buildingId, enterpriseId);
    }
    
    @Override
    public Page<TransactionEntity> search(UUID buildingId, Pageable page, UUID enterpriseId) {
        return transactionRepository.findAllByBuilding_IdAndEnterprise_Id(buildingId, enterpriseId, page);
    }

    @Override
    public Page<TransactionEnterpriseAdminDTO> searchEnterpriseAdmin(
            Pageable page, String name
    ) {
        return transactionRepository.findAllOrderByCreatedDateDesc(page,name).map(
                transaction -> new TransactionEnterpriseAdminDTO(
                        transaction.getId(),
                        transaction.getVersion(),
                        transaction.getCreatedDate(),
                        transaction.getBuilding().getEnterprise().getName(),
                        transaction.getBuilding().getName(),
                        transaction.getAmount(),
                        transaction.getMonths(),
                        transaction.getNumberOfDevices(),
                        transaction.getTransactionType()
                )
        );
    }
}
