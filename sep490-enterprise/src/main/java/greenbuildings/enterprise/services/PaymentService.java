package greenbuildings.enterprise.services;

import greenbuildings.enterprise.dtos.PaymentCriteriaDTO;
import greenbuildings.enterprise.entities.PaymentEntity;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;


public interface PaymentService {
    
    Page<PaymentEntity> search(SearchCriteriaDTO<PaymentCriteriaDTO> searchCriteria, Pageable pageable);
    
    PaymentEntity createPayment(UUID id, String requestOrigin);
    
    void updatePaymentInfo(Long orderCode);
}
