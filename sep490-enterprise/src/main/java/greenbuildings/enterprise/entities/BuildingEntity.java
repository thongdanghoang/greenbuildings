package greenbuildings.enterprise.entities;

import greenbuildings.commons.springfw.impl.entities.AbstractAuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SqlFragmentAlias;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@FilterDef(name = BuildingEntity.BUILDING_SUBSCRIPTION_FILTER_FOR_TENANT,
           parameters = {@ParamDef(name = BuildingEntity.TENANT_ID_PARAMETER_NAME, type = UUID.class)})
@Filter(name = BuildingEntity.BUILDING_SUBSCRIPTION_FILTER_FOR_TENANT,
        condition = """
                            exists (
                                select 1 from building_group bg
                                join subscriptions s on {rootEntity}.id = s.building_id
                                where bg.building_id = {rootEntity}.id
                                and s.start_date <= current_date
                                and current_date <= s.end_date
                                and bg.tenant_id = :""" + BuildingEntity.TENANT_ID_PARAMETER_NAME + ")",
        aliases = {@SqlFragmentAlias(alias = "rootEntity", table = "buildings")})

@FilterDef(name = BuildingEntity.BUILDING_SUBSCRIPTION_FILTER_FOR_ENTERPRISE,
           parameters = {@ParamDef(name = BuildingEntity.ENTERPRISE_ID_PARAMETER_NAME, type = UUID.class)})
@Filter(name = BuildingEntity.BUILDING_SUBSCRIPTION_FILTER_FOR_ENTERPRISE,
        condition = """
                            exists (
                                select 1 from subscriptions s
                                where {rootEntity}.id = s.building_id
                                and s.start_date <= current_date
                                and current_date <= s.end_date
                                and {rootEntity}.enterprise_id = :""" + BuildingEntity.ENTERPRISE_ID_PARAMETER_NAME + ")",
        aliases = {@SqlFragmentAlias(alias = "rootEntity", table = "buildings")})
@Entity
@Table(name = "buildings")
@SoftDelete
@Getter
@Setter
@FieldNameConstants
@NoArgsConstructor
public class BuildingEntity extends AbstractAuditableEntity {
    
    public static final String BUILDING_SUBSCRIPTION_FILTER_FOR_TENANT = "building_subscription_filter_for_tenant";
    public static final String TENANT_ID_PARAMETER_NAME = "tenantId";
    
    public static final String BUILDING_SUBSCRIPTION_FILTER_FOR_ENTERPRISE = "building_subscription_filter_for_enterprise";
    public static final String ENTERPRISE_ID_PARAMETER_NAME = "enterpriseId";
    
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
    
    @Column(name = "number_of_levels")
    private int numberOfLevels;
    
    @Column(name = "area")
    private double area;
    
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    @Column(name = "latitude")
    private double latitude;
    
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    @Column(name = "longitude")
    private double longitude;
    
    @PositiveOrZero
    @Column(name = "ghg_limit")
    private BigDecimal limit = BigDecimal.ZERO;
    
    @OneToMany(mappedBy = "building")
    private Set<SubscriptionEntity> subscriptions = new HashSet<>();
    
    @OneToMany(mappedBy = "building")
    private Set<BuildingGroupEntity> buildingGroups = new HashSet<>();
    
    @OneToMany(mappedBy = "building")
    private Set<ActivityTypeEntity> activityTypes = new HashSet<>();
    
    public static BuildingEntity of(UUID id) {
        var building = new BuildingEntity();
        building.setId(id);
        return building;
    }
}
