package enterprise.entities;

import commons.springfw.impl.entities.AbstractAuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "wallets")
@Getter
@Setter
@NoArgsConstructor
public class WalletEntity extends AbstractAuditableEntity {
    
    @NotNull
    @OneToOne(optional = false)
    @JoinColumn(name = "enterprise_id", nullable = false)
    private EnterpriseEntity enterprise;
    
    @Column(name = "balance")
    private long balance; // credit
    
    public static WalletEntity of(EnterpriseEntity enterprise) {
        var wallet = new WalletEntity();
        wallet.setEnterprise(enterprise);
        wallet.setBalance(0L);
        return wallet;
    }
    
}
