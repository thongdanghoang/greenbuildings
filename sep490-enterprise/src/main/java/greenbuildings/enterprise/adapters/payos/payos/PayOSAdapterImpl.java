package greenbuildings.enterprise.adapters.payos.payos;

import greenbuildings.enterprise.entities.CreditPackageEntity;
import greenbuildings.enterprise.entities.EnterpriseEntity;
import greenbuildings.enterprise.entities.PaymentEntity;
import greenbuildings.enterprise.mappers.PaymentMapper;
import greenbuildings.commons.api.enums.PaymentStatus;
import greenbuildings.commons.api.exceptions.TechnicalException;
import greenbuildings.commons.api.utils.EnumUtil;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;
import vn.payos.type.PaymentLinkData;

import java.util.Optional;

import static greenbuildings.enterprise.services.impl.PaymentServiceImpl.CREDIT_ITEM;


@Service
@RequiredArgsConstructor
@Slf4j
public class PayOSAdapterImpl implements PayOSAdapter {
    
    @Value("${payment.payos.returnPath}")
    private String returnPath;
    
    @Value("${payment.payos.cancelPath}")
    private String cancelPath;
    
    private String returnUrl;
    private String cancelUrl;
    
    private final PayOS payOS;
    private final PaymentMapper mapper;
    
    
    @Override
    public PaymentEntity newPayment(@NotNull CreditPackageEntity creditPackageEntity, @NotNull EnterpriseEntity enterpriseEntity, @NotNull String requestOrigin) {
        try {
            this.setUrls(requestOrigin);
            log.info("Creating payment link for enterprise: {}", enterpriseEntity.getId());
            
            ItemData itemData = buildItemData(creditPackageEntity);
            PaymentData paymentData = getPaymentData(creditPackageEntity, enterpriseEntity, itemData);
            CheckoutResponseData payOSResult = payOS.createPaymentLink(paymentData);
            
            log.info("Payment link created successfully for order code: {}", payOSResult.getOrderCode());
            return preparePaymentEntity(enterpriseEntity, payOSResult, creditPackageEntity);
        } catch (Exception ex) {
            log.error("Failed to create payment link for enterprise: {}", enterpriseEntity.getId(), ex);
            throw new TechnicalException("Failed to create payment link", ex);
        }
    }
    
    @Override
    public Optional<PaymentStatus> getPaymentStatus(@NotNull Long orderCode) {
        try {
            log.info("Getting latest payment data for order code: {}", orderCode);
            PaymentLinkData latestPaymentData = payOS.getPaymentLinkInformation(orderCode);
            log.info("Latest payment status: {}", latestPaymentData.getStatus());
            return EnumUtil.getCodeFromString(PaymentStatus.class, latestPaymentData.getStatus());
        } catch (Exception e) {
            throw new TechnicalException(e);
        }
    }
    
    private PaymentEntity preparePaymentEntity(EnterpriseEntity enterpriseEntity, CheckoutResponseData payOSResult,
                                               CreditPackageEntity creditPackageEntity) {
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setEnterprise(enterpriseEntity);
        paymentEntity.setStatus(PaymentStatus.PENDING);
        paymentEntity.setAmount(payOSResult.getAmount());
        paymentEntity.setNumberOfCredits(creditPackageEntity.getNumberOfCredits());
        mapper.updatePaymentFromCheckoutData(payOSResult, paymentEntity);
        return paymentEntity;
    }
    
    
    private ItemData buildItemData(CreditPackageEntity creditPackageEntity) {
        return ItemData.builder()
                       .name(CREDIT_ITEM)
                       .quantity(creditPackageEntity.getNumberOfCredits())
                       .price(0) // total price will be set at PaymentData
                       .build();
    }
    
    private PaymentData getPaymentData(CreditPackageEntity creditPackageEntity,
                                       EnterpriseEntity enterpriseEntity,
                                       ItemData itemData) {
        return PaymentData.builder()
                          .orderCode(System.currentTimeMillis())
                          .amount((int) creditPackageEntity.getPrice())
                          .description("Credit purchase") // max 25 chars
                          .buyerName(enterpriseEntity.getName())
                          .buyerEmail(enterpriseEntity.getEmail())
                          .buyerPhone(enterpriseEntity.getHotline())
                          .item(itemData)
                          .returnUrl(returnUrl)
                          .cancelUrl(cancelUrl)
                          .build();
    }
    
    private void setUrls(String homepage) {
        returnUrl = UriComponentsBuilder.fromUriString(homepage)
                                        .path(returnPath)
                                        .build()
                                        .toUriString();
        cancelUrl = UriComponentsBuilder.fromUriString(homepage)
                                        .path(cancelPath)
                                        .build()
                                        .toUriString();
        log.info("Settings up URL: {} - {}", returnUrl, cancelUrl);
    }
    
}
