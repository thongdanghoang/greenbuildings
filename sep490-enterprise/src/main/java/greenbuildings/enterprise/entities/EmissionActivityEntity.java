package greenbuildings.enterprise.entities;

import commons.springfw.impl.entities.AbstractAuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "emission_activity")
@Getter
@Setter
@NoArgsConstructor
public class EmissionActivityEntity extends AbstractAuditableEntity {
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    private BuildingEntity building;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emission_factor_id")
    private EmissionFactorEntity emissionFactorEntity;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "emission_source_id")
    private EmissionSourceEntity source;
    
    @NotBlank
    @Size(max = 255)
    @Column(name = "name")
    private String name;
    
    @NotBlank
    @Size(max = 255)
    @Column(name = "type")
    private String type;
    
    @NotBlank
    @Size(max = 255)
    @Column(name = "category")
    private String category;
    
    @Min(1)
    @Column(name = "quantity")
    private int quantity;
    
    @Size(max = 1000)
    @Column(name = "description")
    private String description;
    
}
