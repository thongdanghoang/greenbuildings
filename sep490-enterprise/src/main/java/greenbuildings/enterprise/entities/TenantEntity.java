package greenbuildings.enterprise.entities;

import commons.springfw.impl.entities.AbstractAuditableEntity;
import greenbuildings.commons.api.utils.CommonConstant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "tenant")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@FieldNameConstants
public class TenantEntity extends AbstractAuditableEntity {

    @Column(name = "name")
    private String name;

    @NonNull
    @NotBlank
    @Pattern(regexp = CommonConstant.EMAIL_PATTERN)
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @NonNull
    @NotBlank
    @Column(name = "hotline")
    @Pattern(regexp = CommonConstant.VIETNAM_PHONE_PATTERN)
    private String hotline;
    
    @OneToMany(mappedBy = "tenant")
    private Set<BuildingGroupEntity> buildingGroups = new HashSet<>();

}
