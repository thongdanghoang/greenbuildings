package greenbuildings.enterprise.entities;

import commons.springfw.impl.entities.AbstractAuditableEntity;
import greenbuildings.enterprise.enums.EmissionUnit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_data_record")
@Getter
@Setter
@NoArgsConstructor
public class EmissionActivityRecordEntity extends AbstractAuditableEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emission_activity_id")
    private EmissionActivityEntity emissionActivityEntity;
    
    @NotNull
    @Positive
    @Column(name = "value")
    private BigDecimal value;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "unit")
    private EmissionUnit unit;
    
    @NotNull
    @Column(name = "start_date")
    private LocalDateTime startDate;
    
    @NotNull
    @Column(name = "end_date")
    private LocalDateTime endDate;
    
}
