package greenbuildings.enterprise.entities;

import greenbuildings.commons.springfw.impl.entities.AbstractBaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "credit_packages_versions")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CreditPackageVersionEntity extends AbstractBaseEntity {
    @Column(name = "number_of_credits", nullable = false)
    @Min(0)
    private int numberOfCredits = 0;

    @Column(name = "price", nullable = false)
    @Min(0)
    private long price = 0;
    
    @Max(100)
    @PositiveOrZero
    @Column(name = "discount")
    private int discount;

    @Column(name = "active")
    private boolean active;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "credit_package_id")
    private CreditPackageEntity creditPackageEntity;
}
