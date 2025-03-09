package greenbuildings.enterprise.entities;

import commons.springfw.impl.entities.AbstractBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "fuel")
public class FuelEntity extends AbstractBaseEntity {
    
    @Column(name = "name_vi")
    private String nameVN;
    
    @Column(name = "name_en")
    private String nameEN;
    
    @Column(name = "name_zh")
    private String nameZH;
    
}
