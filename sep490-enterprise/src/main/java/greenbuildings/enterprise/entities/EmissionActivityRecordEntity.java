package greenbuildings.enterprise.entities;

import commons.springfw.impl.entities.AbstractAuditableEntity;
import greenbuildings.enterprise.enums.EmissionUnit;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "activity_data_record")
@Getter
@Setter
@NoArgsConstructor
public class EmissionActivityRecordEntity extends AbstractAuditableEntity {
    
    @ManyToOne
    @JoinColumn(name = "asset_id")
    private AssetEntity asset;
    
    @NotNull
    @Min(0)
    @Column(name = "value")
    private BigDecimal value;
    
    @Min(0)
    @Column(name = "quantity")
    private int quantity;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "unit")
    private EmissionUnit unit;
    
    @NotNull
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @NotNull
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "file_id", referencedColumnName = "id")
    private RecordFileEntity file;
    
    @Transient
    private BigDecimal ghg;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "emission_activity_id")
    private EmissionActivityEntity emissionActivity;
}
