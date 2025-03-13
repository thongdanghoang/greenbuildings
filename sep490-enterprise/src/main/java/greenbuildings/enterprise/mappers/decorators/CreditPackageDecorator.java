package greenbuildings.enterprise.mappers.decorators;

import greenbuildings.enterprise.dtos.CreditPackageDTO;
import greenbuildings.enterprise.entities.CreditPackageEntity;
import greenbuildings.enterprise.entities.CreditPackageVersionEntity;
import greenbuildings.enterprise.mappers.CreditPackageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public abstract class CreditPackageDecorator implements CreditPackageMapper {
    @Autowired
    @Qualifier("delegate")
    private CreditPackageMapper delegate;

    @Override
    public CreditPackageVersionEntity dtoToCreateCreditPackage(CreditPackageDTO dto) {
        CreditPackageVersionEntity creditPackageVersionEntity = delegate.dtoToCreateCreditPackage(dto);
        CreditPackageEntity creditPackageEntity = new CreditPackageEntity();
        creditPackageVersionEntity.setCreditPackageEntity(creditPackageEntity);
        return creditPackageVersionEntity;
    }

    @Override
    public CreditPackageVersionEntity dtoToUpdateCreditPackage(CreditPackageEntity entity, CreditPackageDTO dto) {
        CreditPackageVersionEntity creditPackageVersionEntity = delegate.dtoToUpdateCreditPackage(entity, dto);
        creditPackageVersionEntity.setCreditPackageEntity(entity);
        return creditPackageVersionEntity;
    }
}
