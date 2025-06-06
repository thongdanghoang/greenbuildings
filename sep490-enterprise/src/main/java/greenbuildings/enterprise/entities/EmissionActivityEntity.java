package greenbuildings.enterprise.entities;

import greenbuildings.commons.springfw.impl.entities.AbstractAuditableEntity;

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
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SqlFragmentAlias;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

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
@NamedEntityGraph(
        name = EmissionActivityEntity.SEARCH_PAGE_GRAPH,
        attributeNodes = {
                @NamedAttributeNode(value = "buildingGroup"),
                @NamedAttributeNode(value = "type"),
                @NamedAttributeNode(value = "records", subgraph = "record"),
                @NamedAttributeNode(value = "emissionFactorEntity", subgraph = "emissionFactor")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "emissionFactor",
                        attributeNodes = {
                                @NamedAttributeNode("source"),
                        }
                ),
                @NamedSubgraph(
                        name = "record",
                        attributeNodes = {
                                @NamedAttributeNode("file")
                        }
                )
        }
)
@NamedEntityGraph(
        name = EmissionActivityEntity.RECORDS_BUILDING_GROUPS_DETAIL,
        attributeNodes = {
                @NamedAttributeNode(value = "records"),
                @NamedAttributeNode(value = "type"),
                @NamedAttributeNode(value = "building"),
                @NamedAttributeNode(value = "buildingGroup", subgraph = "buildingGroupSubgraph"),
                @NamedAttributeNode(value = "emissionFactorEntity", subgraph = "factorSubgraph"),
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "buildingGroupSubgraph",
                        attributeNodes = {
                                @NamedAttributeNode("tenant"),
                        }
                ),
                @NamedSubgraph(
                        name = "factorSubgraph",
                        attributeNodes = {
                                @NamedAttributeNode("source"),
                        }
                )
        }
)
@FilterDef(name = EmissionActivityEntity.ACTIVITY_BUILDING_SUBSCRIPTION_FILTER_FOR_ENTERPRISE,
           parameters = {@ParamDef(name = EmissionActivityEntity.ENTERPRISE_ID_PARAMETER_NAME, type = UUID.class)})
@Filter(name = EmissionActivityEntity.ACTIVITY_BUILDING_SUBSCRIPTION_FILTER_FOR_ENTERPRISE,
        condition = """
                            {rootEntity}.building_id IN (
                                SELECT b_filter.id FROM buildings b_filter
                                JOIN subscriptions s_filter ON b_filter.id = s_filter.building_id
                                WHERE s_filter.start_date <= current_date
                                AND current_date <= s_filter.end_date
                                AND b_filter.enterprise_id = :""" + EmissionActivityEntity.ENTERPRISE_ID_PARAMETER_NAME + """
                            )
                            """,
        aliases = {@SqlFragmentAlias(alias = "rootEntity", table = "emission_activity")}
)
@FilterDef(name = EmissionActivityEntity.ACTIVITY_BUILDING_SUBSCRIPTION_FILTER_FOR_TENANT,
           parameters = {@ParamDef(name = EmissionActivityEntity.TENANT_ID_PARAMETER_NAME, type = UUID.class)})
@Filter(name = EmissionActivityEntity.ACTIVITY_BUILDING_SUBSCRIPTION_FILTER_FOR_TENANT,
        condition = """
                            {rootEntity}.building_id IN (
                                SELECT b_filter.id FROM buildings b_filter
                                JOIN building_group bg_filter ON b_filter.id = bg_filter.building_id
                                JOIN subscriptions s_filter ON b_filter.id = s_filter.building_id
                                WHERE s_filter.start_date <= current_date
                                AND current_date <= s_filter.end_date
                                AND bg_filter.tenant_id = :""" + EmissionActivityEntity.TENANT_ID_PARAMETER_NAME + """
                            )
                            """,
        aliases = {@SqlFragmentAlias(alias = "rootEntity", table = "emission_activity")}
)
@Getter
@Setter
@NoArgsConstructor
@FieldNameConstants
public class EmissionActivityEntity extends AbstractAuditableEntity {
    
    public static final String ACTIVITY_BUILDING_SUBSCRIPTION_FILTER_FOR_ENTERPRISE = "ACTIVITY_BUILDING_SUBSCRIPTION_FILTER_FOR_ENTERPRISE";
    public static final String ACTIVITY_BUILDING_SUBSCRIPTION_FILTER_FOR_TENANT = "ACTIVITY_BUILDING_SUBSCRIPTION_FILTER_FOR_TENANT";
    public static final String TENANT_ID_PARAMETER_NAME = "tenantId";
    public static final String ENTERPRISE_ID_PARAMETER_NAME = "enterpriseId";
    
    public static final String DETAILS_GRAPH = "EmissionActivity.details";
    public static final String RECORDS_BUILDING_GROUPS_DETAIL = "records-building-groups-detail";
    public static final String SEARCH_PAGE_GRAPH = "search-page-graph";
    
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
    
    @Transient
    private BigDecimal totalEmission = BigDecimal.ZERO;
}
