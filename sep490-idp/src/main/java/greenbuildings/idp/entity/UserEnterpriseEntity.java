package greenbuildings.idp.entity;

import commons.springfw.impl.entities.AbstractAuditableEntity;
import greenbuildings.commons.api.security.UserScope;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;

import java.util.UUID;

@Entity
@Table(name = "enterprise_users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SoftDelete
public class UserEnterpriseEntity extends AbstractAuditableEntity {
    
    @NotNull
    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    
    @Column(name = "enterprise_id")
    private UUID enterprise;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "user_scope")
    private UserScope scope;
    
    public static UserEnterpriseEntity of(UserEntity user, UserScope scope) {
        UserEnterpriseEntity userEnterpriseEntity = new UserEnterpriseEntity();
        userEnterpriseEntity.setUser(user);
        userEnterpriseEntity.setScope(scope);
        return userEnterpriseEntity;
    }
}
