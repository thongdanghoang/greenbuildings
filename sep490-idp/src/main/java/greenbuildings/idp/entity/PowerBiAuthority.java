package greenbuildings.idp.entity;

import commons.springfw.impl.entities.AbstractAuditableEntity;
import greenbuildings.commons.api.security.PowerBiScope;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@FieldNameConstants
@Entity
@Table(name = "power_bi_authorities")
@NoArgsConstructor
@Getter
@Setter
public class PowerBiAuthority extends AbstractAuditableEntity {
    
    @NotBlank
    @Column(name = "key")
    private String apiKey;
    
    @Column(name = "note")
    private String note;
    
    @NotNull
    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;
    
    @NotNull
    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    
    @NotNull
    @Column(name = "enterprise_id")
    private UUID enterpriseId;
    
    @NotNull
    @Column(name = "scope")
    @Enumerated(EnumType.STRING)
    private PowerBiScope scope;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "power_bi_building_permissions",
            joinColumns = @JoinColumn(name = "power_bi_authority_id")
    )
    @Column(name = "building_id")
    private Set<UUID> buildings = new HashSet<>();

    @Column(name = "last_used")
    private LocalDateTime lastUsed;
}
