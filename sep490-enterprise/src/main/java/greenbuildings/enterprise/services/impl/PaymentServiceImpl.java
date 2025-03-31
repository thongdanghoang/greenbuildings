package greenbuildings.enterprise.services.impl;

import commons.springfw.impl.utils.SecurityUtils;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.enums.PaymentStatus;
import greenbuildings.commons.api.exceptions.TechnicalException;
import greenbuildings.enterprise.adapters.payos.payos.PayOSAdapter;
import greenbuildings.enterprise.dtos.PaymentCriteriaDTO;
import greenbuildings.enterprise.entities.CreditPackageVersionEntity;
import greenbuildings.enterprise.entities.EnterpriseEntity;
import greenbuildings.enterprise.entities.PaymentEntity;
import greenbuildings.enterprise.entities.WalletEntity;
import greenbuildings.enterprise.repositories.CreditPackageVersionRepository;
import greenbuildings.enterprise.repositories.EnterpriseRepository;
import greenbuildings.enterprise.repositories.PaymentRepository;
import greenbuildings.enterprise.repositories.WalletRepository;
import greenbuildings.enterprise.services.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@Transactional(rollbackFor = Throwable.class)
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository payRepo;
    private final EnterpriseRepository enterpriseRepo;
    private final WalletRepository walletRepository;
    private final CreditPackageVersionRepository creditPackageVersionRepository;
    
    private final PayOSAdapter payOSAdapter;
    
    public static final String CREDIT_ITEM = "Credit";
    
    @Override
    public Page<PaymentEntity> search(SearchCriteriaDTO<PaymentCriteriaDTO> searchCriteria, Pageable pageable) {
        UUID enterpriseId = SecurityUtils.getCurrentUserEnterpriseId().orElseThrow();
        return payRepo.findByEnterpriseId(enterpriseId, pageable);
    }
    
    @Override
    public PaymentEntity createPayment(UUID creditPackageVersionUUID, String requestOrigin) {
        try {
            // Validate
            if (StringUtils.isEmpty(requestOrigin)) {
                throw new TechnicalException("Invalid request origin");
            }
            // Prepare
            CreditPackageVersionEntity creditPackageVersionEntity = creditPackageVersionRepository.findById(creditPackageVersionUUID).orElseThrow();
            UUID enterpriseUUID = SecurityUtils.getCurrentUserEnterpriseId().orElseThrow();
            EnterpriseEntity enterpriseEntity = enterpriseRepo.findById(enterpriseUUID).orElseThrow();
            
            // Build info
            PaymentEntity paymentEntity = payOSAdapter.newPayment(creditPackageVersionEntity, enterpriseEntity, requestOrigin);
            
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
