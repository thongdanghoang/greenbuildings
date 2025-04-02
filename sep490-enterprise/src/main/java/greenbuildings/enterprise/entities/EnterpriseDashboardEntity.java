package greenbuildings.enterprise.entities;

import commons.springfw.impl.entities.AbstractBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Entity
@Table(name = "dashboard")
@Getter
@Setter
@NoArgsConstructor
public class EnterpriseDashboardEntity extends AbstractBaseEntity {
    
    @NotBlank
    @Column(name = "title")
    private String title;
    
    @NotBlank
    @Column(name = "src")
    private String source;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "enterprise_id")
    private EnterpriseEntity enterprise;
}
