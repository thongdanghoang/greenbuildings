package enterprise.entities;

import commons.springfw.impl.entities.AbstractAuditableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sep490.common.api.utils.CommonConstant;

@Entity
@Table(name = "enterprises")
@Getter
@Setter
@NoArgsConstructor
public class EnterpriseEntity extends AbstractAuditableEntity {
    
    @NotNull
    @OneToOne(mappedBy = "enterprise", cascade = CascadeType.ALL, optional = false)
    private WalletEntity wallet;
    
    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;
    
    @NotBlank
    @Pattern(regexp = CommonConstant.EMAIL_PATTERN)
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @NotBlank
    @Pattern(regexp = CommonConstant.VIETNAM_PHONE_PATTERN)
    @Column(name = "hotline", nullable = false)
    private String hotline;
    
}
