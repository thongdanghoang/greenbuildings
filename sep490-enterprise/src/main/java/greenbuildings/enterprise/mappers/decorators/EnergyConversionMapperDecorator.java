package greenbuildings.enterprise.mappers.decorators;


import greenbuildings.enterprise.dtos.EnergyConversionDTO;
import greenbuildings.enterprise.entities.EnergyConversionEntity;
import greenbuildings.enterprise.entities.FuelEntity;
import greenbuildings.enterprise.mappers.CreditPackageMapper;
import greenbuildings.enterprise.mappers.EnergyConversionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public abstract class EnergyConversionMapperDecorator  implements EnergyConversionMapper {
    @Autowired
    @Qualifier("delegate")
    private EnergyConversionMapper delegate;


    @Override
    public EnergyConversionEntity createNewEnergyConversion(EnergyConversionDTO dto) {
        EnergyConversionEntity entity = delegate.createNewEnergyConversion(dto);
        FuelEntity fuelEntity = new FuelEntity();
        fuelEntity.setNameEN(dto.fuel().nameEN());
        fuelEntity.setNameVN(dto.fuel().nameVN());
        fuelEntity.setNameZH(dto.fuel().nameZH());
        entity.setFuel(fuelEntity);
        return entity;
    }

    @Override
    public EnergyConversionEntity updateEnergyConversion(EnergyConversionEntity entity, EnergyConversionDTO dto) {
        EnergyConversionEntity energyConversionEntity = delegate.updateEnergyConversion(entity, dto);
        energyConversionEntity.getFuel().setNameEN(dto.fuel().nameEN());
        energyConversionEntity.getFuel().setNameVN(dto.fuel().nameVN());
        energyConversionEntity.getFuel().setNameZH(dto.fuel().nameZH());
        return energyConversionEntity;
    }
}
