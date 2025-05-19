package greenbuildings.idp.entity;

import greenbuildings.commons.springfw.impl.entities.AbstractBaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "authenticator")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserAuthenticator extends AbstractBaseEntity {
    
    @Column(name = "credential_id", nullable = false, unique = true)
    private String credentialId;
    
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    
    @Column(name = "credential_name")
    private String credentialName;
    
    @Column(name = "attestation_object", nullable = false)
    private byte[] attestationObject;
    
    @Column(name = "sign_count")
    private long signCount;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (!(o instanceof UserAuthenticator that)) {return false;}
        if (!super.equals(o)) {return false;}
        return Objects.equals(credentialId, that.credentialId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), credentialId);
    }
    
    public static UserAuthenticator createSerializedAuthenticator(
            String credentialId,
            UserEntity user,
            String credentialName,
            byte[] attestationObject,
            long signCount) {
        return new UserAuthenticator(credentialId, user, credentialName, attestationObject, signCount);
    }
}
