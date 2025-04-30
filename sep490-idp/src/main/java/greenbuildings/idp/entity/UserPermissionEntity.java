package greenbuildings.idp.entity;

import greenbuildings.commons.api.security.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;

import java.util.UUID;

@Entity
@Table(name = "user_authorities")
@NoArgsConstructor
@Getter
@Setter
@SoftDelete
public class UserPermissionEntity {
    
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @NotNull
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private UserEntity user;
    
    @Column(name = "reference_id")
    private UUID referenceId;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "permission")
    private UserRole role;
    
    public static UserPermissionEntity of(UserEntity user, UserRole role, UUID referenceId) {
        var userPermission = new UserPermissionEntity();
        userPermission.setUser(user);
        userPermission.setRole(role);
        userPermission.setReferenceId(referenceId);
        return userPermission;
    }
    
}
