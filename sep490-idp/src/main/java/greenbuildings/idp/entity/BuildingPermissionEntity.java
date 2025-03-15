package greenbuildings.idp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import greenbuildings.commons.api.security.BuildingPermissionRole;
import org.hibernate.annotations.SoftDelete;

import java.util.UUID;

@Entity
@Table(name = "enterprise_user_building_permissions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SoftDelete
public class BuildingPermissionEntity {
    
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    
    @Column(name = "building_id")
    private UUID building;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "permission")
    private BuildingPermissionRole role;
    
}
