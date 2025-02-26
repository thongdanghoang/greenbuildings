package greenbuildings.enterprise.entities;

import commons.springfw.impl.entities.AbstractAuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "credit_convert_ratio")
@Getter
@Setter
@NoArgsConstructor
public class CreditConvertRatioEntity extends AbstractAuditableEntity {
    
    @Column(name = "ratio")
    private double ratio;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "convert_type", unique = true)
    private CreditConvertType convertType;
}
