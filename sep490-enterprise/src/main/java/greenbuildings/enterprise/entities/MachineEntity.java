package greenbuildings.enterprise.entities;

import commons.springfw.impl.entities.AbstractBaseEntity;
import greenbuildings.enterprise.entities.enums.EmissionSource;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "machine")
@Getter
@Setter
public class MachineEntity extends AbstractBaseEntity {
    
    @ManyToOne
    @JoinColumn(name = "building_id", nullable = false)
    private BuildingEntity building;
    
    @Column(nullable = false, length = 255)
    private String name;
    
    @Column(length = 1000)
    private String description;
    
    @Column(nullable = false)
    private int quantity;
    
    @Column(name = "emission_source", nullable = false)
    @Enumerated(EnumType.STRING)
    private EmissionSource emissionSource;
    
    @OneToMany(mappedBy = "machine", cascade = CascadeType.ALL, orphanRemoval = true)
    List<MachineFuelEmissionEntity> emissionFactors = new ArrayList<>();
}
