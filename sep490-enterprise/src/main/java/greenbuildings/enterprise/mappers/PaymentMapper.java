package greenbuildings.enterprise.mappers;

import greenbuildings.enterprise.dtos.PaymentDTO;
import greenbuildings.enterprise.dtos.PaymentDetailDTO;
import greenbuildings.enterprise.dtos.PaymentEnterpriseAdminDTO;
import greenbuildings.enterprise.entities.PaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import vn.payos.type.CheckoutResponseData;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {
    
    PaymentDTO paymentEntityToPaymentDTO(PaymentEntity paymentEntity);

    @Mapping(source = "enterprise", target = "enterpriseDTO")
    @Mapping(source = "enterprise.enterpriseEmail", target = "enterpriseDTO.email")
    @Mapping(source = "creditPackageVersionEntity", target = "creditPackageVersionDTO")
    PaymentDetailDTO paymentEntityToPaymentDetailDTO(PaymentEntity paymentEntity);
    
    void updatePaymentFromCheckoutData(CheckoutResponseData payOSResult, @MappingTarget PaymentEntity payment);

    PaymentEnterpriseAdminDTO toPaymentEnterpriseAdminDTO(PaymentEnterpriseAdminDTO paymentEntity);
}
