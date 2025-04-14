package greenbuildings.enterprise.entities;

import commons.springfw.impl.entities.AbstractAuditableEntity;
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
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;

import java.util.HashSet;
import java.util.Set;

@Entity
@NamedEntityGraph(
        name = BuildingEntity.WITH_ACTIVITIES_ENTITY_GRAPH,
        attributeNodes = {
                @NamedAttributeNode(value = "buildingGroups", subgraph = BuildingEntity.WITH_ACTIVITIES_RECORDS_SUB_GRAPH)
        },
        subgraphs = {
                @NamedSubgraph(name = BuildingEntity.WITH_ACTIVITIES_RECORDS_SUB_GRAPH, attributeNodes = @NamedAttributeNode("groupItems"))
        }
)
@Table(name = "buildings")
@Getter
@Setter
@NoArgsConstructor
@SoftDelete
public class BuildingEntity extends AbstractAuditableEntity {
    
    public static final String WITH_ACTIVITIES_ENTITY_GRAPH = "building-activites-entity-graph";
    public static final String WITH_ACTIVITIES_RECORDS_SUB_GRAPH = "building-activites-records-sub-graph";
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "enterprise_id", nullable = false)
    private EnterpriseEntity enterprise;
    
    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;
    
    @NotBlank
    @Column(name = "address", nullable = false)
    private String address;

    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    @Column(name = "latitude")
    private double latitude;
    
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    @Column(name = "longitude")
    private double longitude;
    
    @OneToMany(mappedBy = "building", orphanRemoval = true)
    private Set<SubscriptionEntity> subscriptions = new HashSet<>();
    
    @OneToMany(mappedBy = "building", orphanRemoval = true)
    private Set<BuildingGroupEntity> buildingGroups = new HashSet<>();
    
}
