package greenbuildings.commons.springfw.impl.entities;

import greenbuildings.commons.api.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

import java.util.Optional;
import java.util.UUID;

@Setter
@Getter
@FieldNameConstants
@MappedSuperclass
public abstract class AbstractBaseEntity implements BaseEntity {
    
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Version
    @Column(name = "version", nullable = false)
    private int version;
    
    @Transient
    private boolean transientHashCodeLeaked;
    
    protected AbstractBaseEntity() {
        // Abstract class should not be instantiated
    }
    
    /**
     * Get the class of an instance or the underlying class
     * of a proxy (without initializing the proxy!). It is
     * almost always better to use the entity name!
     */
    private static Class<?> getPersistenceClassWithoutInitializingProxy(Object entity) {
        if (entity instanceof HibernateProxy proxy) {
            LazyInitializer li = proxy.getHibernateLazyInitializer();
            return li.getPersistentClass();
        } else {
            return entity.getClass();
        }
    }
    
    public boolean isPersisted() {
        return Optional.ofNullable(getId()).isPresent();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null) {
            return false;
        }
        
        /*
         * The following is a solution that works for hibernate lazy loading proxies.
         */
        if (getPersistenceClassWithoutInitializingProxy(this) != getPersistenceClassWithoutInitializingProxy(obj)) {
            return false;
        }
        
        /*
         * To check whether the class of the argument is equal (or compatible) to the implementing class before
         * casting it
         * */
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        AbstractBaseEntity other = (AbstractBaseEntity) obj;
        if (isPersisted() && other.isPersisted()) { // both entities are not new
            return getId().equals(other.getId());
        }
        return false;
    }
    
    @Override
    @SuppressWarnings("java:S2676") // In case current entity state is transient => should return a negative number
    public int hashCode() {
        if (!isPersisted()) { // is new or is in transient state.
            transientHashCodeLeaked = true;
            return -super.hashCode();
        }
        
        // Because hashcode has just been asked for when the object is in transient state at that time super.hashCode() is returned.
        // Now for consistency, we return the same value.
        if (transientHashCodeLeaked) {
            return -super.hashCode();
        }
        
        // The above mechanism obey the rule: if 2 objects are equal, their hashcode must be same.
        return getId().hashCode();
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + getId() + ")";
    }
}
