package greenbuildings.enterprise.entities;

import commons.springfw.impl.entities.AbstractAuditableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.util.Set;


@Entity
@Table(name = "emission_activity")
@NamedEntityGraph(
    name = EmissionActivityEntity.DETAILS_GRAPH,
    attributeNodes = {
        @NamedAttributeNode("buildingGroup"),
        @NamedAttributeNode("type"),
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
    
    @NonNull
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    private BuildingEntity building;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emission_factor_id")
    private EmissionFactorEntity emissionFactorEntity;
    
    @NonNull
    @NotBlank
    @Size(max = 255)
    @Column(name = "name")
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "type_id")
    private ActivityTypeEntity type;
    
    @NonNull
    @NotBlank
    @Size(max = 255)
    @Column(name = "category")
    private String category;
    
    @Size(max = 1000)
    @Column(name = "description")
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_group_id")
    private BuildingGroupEntity buildingGroup;
    
    @OneToMany(mappedBy = "emissionActivity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<EmissionActivityRecordEntity> records = new java.util.HashSet<>();
}
