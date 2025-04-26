package greenbuildings.enterprise.services;

import greenbuildings.commons.api.enums.PaymentStatus;
import greenbuildings.enterprise.TestcontainersConfigs;
import greenbuildings.enterprise.adapters.payos.payos.PayOSAdapterImpl;
import greenbuildings.enterprise.entities.PaymentEntity;
import greenbuildings.enterprise.repositories.CreditPackageVersionRepository;
import greenbuildings.enterprise.repositories.PaymentRepository;
import greenbuildings.enterprise.repositories.WalletRepository;
import greenbuildings.enterprise.services.impl.PaymentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class PaymentServiceTest extends TestcontainersConfigs {
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private WalletRepository walletRepository;
    
    @Autowired
    private CreditPackageVersionRepository creditPackageVersionRepository;
    
    @Mock
    private PayOSAdapterImpl payOSAdapter;
    
    private PaymentService paymentService;
    
    @Override
    protected String getBaseUrl() {
        return "";
    }
    
    @BeforeEach
    void setUp() {
        paymentService = new PaymentServiceImpl(
                paymentRepository,
                enterpriseRepository,
                walletRepository,
                creditPackageVersionRepository,
                payOSAdapter
        );
    }
    
    @Test
    void createPayment() {
        var creditPackageVersionEntity = creditPackageVersionRepository.findAll().stream().findAny().orElseThrow();
        var paymentEntity = new PaymentEntity();
        paymentEntity.setEnterprise(enterpriseRepository.getReferenceById(enterpriseOwnerEnterpriseId()));
        paymentEntity.setCreditPackageVersionEntity(creditPackageVersionEntity);
        paymentEntity.setStatus(PaymentStatus.PENDING);
        
        Mockito.when(payOSAdapter.newPayment(Mockito.any(), Mockito.any(), Mockito.any()))
               .thenReturn(paymentEntity);
        var payment = paymentService.createPayment(enterpriseOwnerEnterpriseId(), creditPackageVersionEntity.getId(), "test origin");
        
        Assertions.assertNotNull(payment.getId());
    }
    
    @Test
    void updatePaymentInfo() {
        var current = walletRepository.findByEnterpriseId(enterpriseOwnerEnterpriseId()).getBalance();
        var orderCode = 4L;
        var creditPackageVersionEntity = creditPackageVersionRepository.findAll().stream().findAny().orElseThrow();
        var paymentEntity = new PaymentEntity();
        paymentEntity.setEnterprise(enterpriseRepository.getReferenceById(enterpriseOwnerEnterpriseId()));
        paymentEntity.setCreditPackageVersionEntity(creditPackageVersionEntity);
        paymentEntity.setStatus(PaymentStatus.PENDING);
        paymentEntity.setOrderCode(orderCode);
        paymentEntity.setNumberOfCredits(creditPackageVersionEntity.getNumberOfCredits());
        paymentRepository.save(paymentEntity);
        
        Mockito.when(payOSAdapter.getPaymentStatus(Mockito.any()))
               .thenReturn(Optional.of(PaymentStatus.PAID));
        paymentService.updatePaymentInfo(enterpriseOwnerEnterpriseId(), orderCode);
        
        Assertions.assertEquals(
                PaymentStatus.PAID,
                paymentRepository.findByOrderCodeAndEnterpriseId(orderCode, enterpriseOwnerEnterpriseId()).orElseThrow().getStatus()
                               );
        Assertions.assertEquals(
                current + creditPackageVersionEntity.getNumberOfCredits(),
                walletRepository.findByEnterpriseId(enterpriseOwnerEnterpriseId()).getBalance()
                               );
    }
}
