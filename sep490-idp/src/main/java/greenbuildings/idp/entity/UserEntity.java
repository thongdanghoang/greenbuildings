package greenbuildings.idp.entity;

import greenbuildings.commons.api.security.UserRole;
import greenbuildings.commons.api.utils.CommonConstant;
import greenbuildings.commons.springfw.impl.entities.AbstractAuditableEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SoftDelete;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@NamedEntityGraph(
        name = UserEntity.WITH_ENTERPRISE_PERMISSIONS_ENTITY_GRAPH,
        attributeNodes = {
                @NamedAttributeNode("authorities")
        }
)
@FilterDef(name = UserEntity.BELONG_ENTERPRISE_FILTER, parameters = @ParamDef(name = UserEntity.BELONG_ENTERPRISE_PARAM, type = UUID.class))
@Filter(name = UserEntity.BELONG_ENTERPRISE_FILTER,
        condition = "id IN (SELECT ue.user_id FROM enterprise_users ue WHERE ue.enterprise_id = :" + UserEntity.BELONG_ENTERPRISE_PARAM + ")")
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SoftDelete
public class UserEntity extends AbstractAuditableEntity {
    
    public static final String WITH_ENTERPRISE_PERMISSIONS_ENTITY_GRAPH = "user-permissions-entity-graph";
    public static final String BELONG_ENTERPRISE_FILTER = "belongEnterpriseFilter";
    public static final String BELONG_ENTERPRISE_PARAM = "enterpriseId";
    
    @OneToMany(mappedBy = "user",
               cascade = CascadeType.ALL,
               fetch = FetchType.EAGER,
               orphanRemoval = true)
    private Set<UserPermissionEntity> authorities = new LinkedHashSet<>();
    
    @OneToMany(mappedBy = "user",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    private List<PowerBiAuthority> powerBiApiKeys = new ArrayList<>();
    
    @NotNull
    @Pattern(regexp = CommonConstant.EMAIL_PATTERN)
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @Column(name = "password", nullable = false, length = 72)
    private String password;
    
    @Column(name = "email_verified")
    private boolean emailVerified;
    
    @Column(name = "phone", length = 16)
    private String phone;
    
    @Column(name = "first_name", length = 50)
    private String firstName;
    
    @Column(name = "last_name", length = 100)
    private String lastName;
    
    @Column(name = "locale", length = 5)
    private String locale = "vi-VN";
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserOTP otp;
    
    public static UserEntity register(
            String email,
            boolean emailVerified,
            UserRole role,
            String firstName,
            String lastName,
            String phone,
            String password) {
        var user = new UserEntity();
        user.setEmail(email);
        user.setEmailVerified(emailVerified);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setPassword(password);
        user.getAuthorities().add(UserPermissionEntity.of(user, role, null));
        return user;
    }
}
