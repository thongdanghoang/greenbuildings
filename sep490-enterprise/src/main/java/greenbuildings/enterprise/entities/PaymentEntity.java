package greenbuildings.enterprise.entities;

import greenbuildings.commons.api.enums.PaymentStatus;
import greenbuildings.commons.springfw.impl.entities.AbstractAuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class PaymentEntity extends AbstractAuditableEntity {
    
    @NonNull
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "enterprise_id", nullable = false)
    private EnterpriseEntity enterprise;
    
    @NonNull
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "credit_packages_versions_id")
    private CreditPackageVersionEntity creditPackageVersionEntity;
    
    @NonNull
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatus status;
    
    @Column(name = "amount")
    private long amount;
    
    @Column(name = "number_of_credits")
    private int numberOfCredits;
    
    @Column(name = "bin")
    private String bin;
    
    @Column(name = "account_number")
    private String accountNumber;
    
    @Column(name = "account_name")
    private String accountName;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "order_code")
    private Long orderCode;
    
    @Column(name = "currency")
    private String currency;
    
    @Column(name = "payment_link_id")
    private String paymentLinkId;
    
    @Column(name = "payos_status")
    private String payOSStatus;
    
    @Column(name = "expired_at")
    private Long expiredAt;
    
    @Column(name = "checkout_url")
    private String checkoutUrl;
    
    @Column(name = "qr_code")
    private String qrCode;

    
}
