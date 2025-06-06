package greenbuildings.enterprise.entities;

import greenbuildings.commons.api.utils.CommonConstant;
import greenbuildings.commons.springfw.impl.entities.AbstractAuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tenant")
@Getter
@Setter
@FieldNameConstants
public class TenantEntity extends AbstractAuditableEntity {
    
    @OneToMany(mappedBy = "tenant")
    private Set<BuildingGroupEntity> buildingGroups = new HashSet<>();
    
    @NotBlank
    @Column(name = "name")
    private String name;
    
    @NotBlank
    @Pattern(regexp = CommonConstant.EMAIL_PATTERN)
    @Column(name = "email")
    private String email;
    
    @NotBlank
    @Column(name = "address")
    private String address;
    
    @NotBlank
    @Pattern(regexp = CommonConstant.VIETNAM_PHONE_PATTERN)
    @Column(name = "hotline")
    private String hotline;
    
    @NotBlank
    @Pattern(regexp = CommonConstant.VIETNAME_TAX_CODE)
    @Column(name = "tax_code")
    private String taxCode;
    
    @NotBlank
    @Column(name = "business_license_image_url")
    private String businessLicenseImageUrl;
    
    @Column(name = "representative_name")
    private String representativeName;
    
    @Column(name = "representative_position")
    private String representativePosition;
    
    @Column(name = "representative_contact")
    private String representativeContact;
    
    public static TenantEntity of(UUID id) {
        TenantEntity tenant = new TenantEntity();
        tenant.setId(id);
        return tenant;
    }
}
