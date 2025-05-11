package greenbuildings.enterprise.services;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.dtos.PaymentCriteriaDTO;
import greenbuildings.enterprise.dtos.PaymentEnterpriseAdminDTO;
import greenbuildings.enterprise.entities.PaymentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;


public interface PaymentService {
    
    Page<PaymentEntity> search(SearchCriteriaDTO<PaymentCriteriaDTO> searchCriteria, Pageable pageable, UUID enterpriseId);
    
    PaymentEntity createPayment(UUID enterpriseID, UUID creditPackageVersionUUID, String requestOrigin);
    
    void updatePaymentInfo(UUID enterpriseID, Long orderCode);

    Optional<PaymentEntity>  findById(UUID id);
    Page<PaymentEnterpriseAdminDTO> searchEnterpriseAdmin(
            Pageable page, String name
    );
}
