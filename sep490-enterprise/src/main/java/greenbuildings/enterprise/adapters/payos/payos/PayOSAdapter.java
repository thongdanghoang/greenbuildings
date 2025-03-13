package greenbuildings.enterprise.adapters.payos.payos;

import greenbuildings.enterprise.entities.CreditPackageEntity;
import greenbuildings.enterprise.entities.CreditPackageVersionEntity;
import greenbuildings.enterprise.entities.EnterpriseEntity;
import greenbuildings.enterprise.entities.PaymentEntity;
import greenbuildings.commons.api.enums.PaymentStatus;

import java.util.Optional;

public interface PayOSAdapter {
    
    PaymentEntity newPayment(CreditPackageVersionEntity creditPackageVersionEntity, EnterpriseEntity enterpriseEntity, String requestOrigin);
    
    Optional<PaymentStatus> getPaymentStatus(Long orderCode);
}
