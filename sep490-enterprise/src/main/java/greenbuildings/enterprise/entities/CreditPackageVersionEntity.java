package greenbuildings.enterprise.entities;
import commons.springfw.impl.entities.AbstractBaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;


import java.time.LocalDateTime;
import java.util.UUID;

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

    @Column(name = "active")
    private boolean active;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "credit_package_id")
    private CreditPackageEntity creditPackageEntity;
}
