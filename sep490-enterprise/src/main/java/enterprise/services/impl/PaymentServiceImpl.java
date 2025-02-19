package enterprise.services.impl;

import commons.springfw.impl.utils.SecurityUtils;
import enterprise.adapters.payos.payos.PayOSAdapter;
import enterprise.dtos.PaymentCriteriaDTO;
import enterprise.entities.CreditPackageEntity;
import enterprise.entities.EnterpriseEntity;
import enterprise.entities.PaymentEntity;
import enterprise.entities.WalletEntity;
import enterprise.repositories.CreditPackageRepository;
import enterprise.repositories.EnterpriseRepository;
import enterprise.repositories.PaymentRepository;
import enterprise.repositories.WalletRepository;
import enterprise.services.PaymentService;
import green_buildings.commons.api.dto.SearchCriteriaDTO;
import green_buildings.commons.api.enums.PaymentStatus;
import green_buildings.commons.api.exceptions.TechnicalException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@Transactional(rollbackOn = Throwable.class)
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository payRepo;
    private final CreditPackageRepository packageRepo;
    private final EnterpriseRepository enterpriseRepo;
    private final WalletRepository walletRepository;
    
    private final PayOSAdapter payOSAdapter;
    
    public static final String CREDIT_ITEM = "Credit";
    
    @Override
    public Page<PaymentEntity> search(SearchCriteriaDTO<PaymentCriteriaDTO> searchCriteria, Pageable pageable) {
        UUID enterpriseId = SecurityUtils.getCurrentUserEnterpriseId().orElseThrow();
        return payRepo.findByEnterpriseId(enterpriseId, pageable);
    }
    
    @Override
    public PaymentEntity createPayment(UUID creditPackageUUID) {
        try {
            // Prepare
            CreditPackageEntity creditPackageEntity = packageRepo.findById(creditPackageUUID).orElseThrow();
            UUID enterpriseUUID = SecurityUtils.getCurrentUserEnterpriseId().orElseThrow();
            EnterpriseEntity enterpriseEntity = enterpriseRepo.findById(enterpriseUUID).orElseThrow();
            
            // Build info
            PaymentEntity paymentEntity = payOSAdapter.newPayment(creditPackageEntity, enterpriseEntity);
            
            return payRepo.save(paymentEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new TechnicalException("Failed to create payment link", e);
        }
    }
    
    @Override
    public void updatePaymentInfo(Long orderCode) {
        UUID uuid = SecurityUtils.getCurrentUserEnterpriseId().orElseThrow();
        PaymentEntity paymentEntity = payRepo.findByOrderCodeAndEnterpriseId(orderCode, uuid).orElseThrow();
        
        PaymentStatus newStatus = payOSAdapter.getPaymentStatus(orderCode)
                                              .orElseThrow(() -> new TechnicalException("Error in getting payment status from PayOS"));
        
        updateWallet(newStatus, paymentEntity, uuid);
        paymentEntity.setStatus(newStatus);
        payRepo.save(paymentEntity);
        
    }
    
    private void updateWallet(PaymentStatus newStatus, PaymentEntity paymentEntity, UUID uuid) {
        if (newStatus == PaymentStatus.PAID && paymentEntity.getStatus() != PaymentStatus.PAID) {
            WalletEntity wallet = walletRepository.findByEnterpriseId(uuid);
            wallet.deposit(paymentEntity.getNumberOfCredits());
            walletRepository.save(wallet);
        }
    }
    
}
