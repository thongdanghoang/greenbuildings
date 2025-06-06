package greenbuildings.enterprise.entities;

import greenbuildings.commons.springfw.impl.entities.AbstractBaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "fuel")
@Getter
@Setter
public class FuelEntity extends AbstractBaseEntity {
    
    @Column(name = "name_vi")
    private String nameVN;
    
    @Column(name = "name_en")
    private String nameEN;
    
    @Column(name = "name_zh")
    private String nameZH;
    
}
