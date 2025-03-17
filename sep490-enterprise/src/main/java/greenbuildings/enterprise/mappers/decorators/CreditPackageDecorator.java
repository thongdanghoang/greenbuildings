package greenbuildings.enterprise.mappers.decorators;

import greenbuildings.enterprise.dtos.CreditPackageDTO;
import greenbuildings.enterprise.dtos.CreditPackageVersionDTO;
import greenbuildings.enterprise.dtos.GetCreditPackageDTOAdmin;
import greenbuildings.enterprise.entities.CreditPackageEntity;
import greenbuildings.enterprise.entities.CreditPackageVersionEntity;
import greenbuildings.enterprise.mappers.CreditPackageMapper;
import greenbuildings.enterprise.mappers.CreditPackageVersionMapper;
import greenbuildings.enterprise.repositories.CreditPackageRepository;
import greenbuildings.enterprise.repositories.CreditPackageVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public abstract class CreditPackageDecorator implements CreditPackageMapper {
    @Autowired
    @Qualifier("delegate")
    private CreditPackageMapper delegate;
    @Autowired
    private CreditPackageVersionRepository creditPackageVersionRepository;
    @Autowired
    private CreditPackageVersionMapper creditPackageVersionMapper;

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

    @Override
    public GetCreditPackageDTOAdmin entityToDTOAdmin(CreditPackageVersionEntity creditPackageVersionEntity){
        GetCreditPackageDTOAdmin dto = delegate.entityToDTOAdmin(creditPackageVersionEntity);
        Optional<CreditPackageVersionEntity> creditPackageVersion = creditPackageVersionRepository.findById(dto.id());
        if(creditPackageVersion.isPresent()){
            CreditPackageEntity creditPackageEntity = creditPackageVersion.get().getCreditPackageEntity();

            // Update the dto with the new instance returned by withActive
            dto = dto.withActive(creditPackageEntity.isActive());

            List<CreditPackageVersionEntity> creditPackageVersions = creditPackageVersionRepository.findAllByIdCreditPackage(creditPackageEntity.getId());
            List<CreditPackageVersionDTO> creditPackageVersionDTOList = new ArrayList<>();
            for(CreditPackageVersionEntity packageVersionEntity : creditPackageVersions){
                creditPackageVersionDTOList.add(creditPackageVersionMapper.entityToDTO(packageVersionEntity));
            }

            // Update the dto with the new instance returned by withPackageVersionDTOList
            dto = dto.withPackageVersionDTOList(creditPackageVersionDTOList);

            return dto;
        }
        return dto;
    }
}
