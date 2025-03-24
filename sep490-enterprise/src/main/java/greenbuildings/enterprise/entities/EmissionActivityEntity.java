package greenbuildings.enterprise.entities;

import commons.springfw.impl.entities.AbstractAuditableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedSubgraph;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "emission_activity")
@NamedEntityGraph(
    name = EmissionActivityEntity.DETAILS_GRAPH,
    attributeNodes = {
        @NamedAttributeNode("building"),
        @NamedAttributeNode(value = "emissionFactorEntity", subgraph = "emissionFactor")
    },
    subgraphs = {
        @NamedSubgraph(
            name = "emissionFactor",
            attributeNodes = {
                @NamedAttributeNode("source"),
                @NamedAttributeNode(value = "energyConversion", subgraph = "energyConversion")
            }
        ),
        @NamedSubgraph(
                name = "energyConversion",
                attributeNodes = {
                        @NamedAttributeNode("fuel")
                }
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
public class EmissionActivityEntity extends AbstractAuditableEntity {

    public static final String DETAILS_GRAPH = "EmissionActivity.details";

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    private BuildingEntity building;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emission_factor_id")
    private EmissionFactorEntity emissionFactorEntity;
    
    @OneToMany(mappedBy = "emissionActivityEntity", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private Set<EmissionActivityRecordEntity> records = new HashSet<>();
    
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

    public void addRecord(EmissionActivityRecordEntity record) {
        records.add(record);
        record.setEmissionActivityEntity(this);
    }
    
}
