package greenbuildings.enterprise.entities;

import commons.springfw.impl.entities.AbstractAuditableEntity;
import greenbuildings.commons.api.utils.CommonConstant;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "enterprises")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@FieldNameConstants
public class EnterpriseEntity extends AbstractAuditableEntity {
    
    @NonNull
    @NotNull
    @OneToOne(mappedBy = "enterprise", cascade = CascadeType.ALL)
    private WalletEntity wallet;
    
    @OneToMany(mappedBy = "enterprise")
    private Set<BuildingEntity> buildings = new HashSet<>();
    
    @NonNull
    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;
    
    @NotBlank
    @Pattern(regexp = CommonConstant.EMAIL_PATTERN)
    @Column(name = "email", nullable = false, unique = true)
    private String enterpriseEmail;
    
    @NonNull
    @NotBlank
    @Column(name = "address")
    private String address;
    
    @NonNull
    @NotBlank
    @Pattern(regexp = CommonConstant.VIETNAM_PHONE_PATTERN)
    @Column(name = "hotline")
    private String hotline;
    
    @NonNull
    @NotBlank
    @Column(name = "tax_code")
    @Pattern(regexp = CommonConstant.VIETNAME_TAX_CODE)
    private String taxCode;
    
    @NonNull
    @NotBlank
    @Column(name = "business_license_image_url")
    private String businessLicenseImageUrl;
    
    @Column(name = "representative_name")
    private String representativeName;
    
    @Column(name = "representative_position")
    private String representativePosition;
    
    @Column(name = "representative_contact")
    private String representativeContact;
}
