package greenbuildings.enterprise.mappers.decorators;


import greenbuildings.enterprise.dtos.EnergyConversionDTO;
import greenbuildings.enterprise.entities.EnergyConversionEntity;
import greenbuildings.enterprise.entities.FuelEntity;
import greenbuildings.enterprise.mappers.EnergyConversionMapper;
import greenbuildings.enterprise.mappers.FuelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public abstract class EnergyConversionMapperDecorator  implements EnergyConversionMapper {
    @Autowired
    @Qualifier("delegate")
    private EnergyConversionMapper delegate;
    
    @Autowired
    private FuelMapper fuelMapper;


    @Override
    public EnergyConversionEntity createNewEnergyConversion(EnergyConversionDTO dto) {
        var entity = delegate.createNewEnergyConversion(dto);
        var fuelEntity = new FuelEntity();
        fuelEntity.setNameEN(dto.fuel().nameEN());
        fuelEntity.setNameVN(dto.fuel().nameVN());
        fuelEntity.setNameZH(dto.fuel().nameZH());
        entity.setFuel(fuelEntity);
        return entity;
    }

    @Override
    public EnergyConversionEntity updateEnergyConversion(EnergyConversionEntity entity, EnergyConversionDTO dto) {
        var energyConversionEntity = delegate.updateEnergyConversion(entity, dto);
        energyConversionEntity.setFuel(fuelMapper.updateFuel(entity.getFuel(), dto.fuel()));
        return energyConversionEntity;
    }
}
