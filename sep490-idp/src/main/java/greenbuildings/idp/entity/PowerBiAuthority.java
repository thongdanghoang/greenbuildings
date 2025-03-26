package greenbuildings.idp.entity;

import commons.springfw.impl.entities.AbstractAuditableEntity;
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

import java.time.LocalDateTime;

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
    
//    @Column(name = "last-used")
//    private LocalDateTime lastUsed;
}
