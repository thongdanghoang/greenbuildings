package greenbuildings.enterprise.mappers.decorators;

import greenbuildings.enterprise.dtos.BuildingGroupDTO;
import greenbuildings.enterprise.dtos.CreateBuildingGroupDTO;
import greenbuildings.enterprise.entities.BuildingEntity;
import greenbuildings.enterprise.entities.BuildingGroupEntity;
import greenbuildings.enterprise.entities.TenantEntity;
import greenbuildings.enterprise.mappers.BuildingGroupMapper;
import greenbuildings.enterprise.mappers.BuildingMapper;
import greenbuildings.enterprise.mappers.TenantMapper;
import greenbuildings.enterprise.repositories.BuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public abstract class BuildingGroupMapperDecorator implements BuildingGroupMapper {

    @Autowired
    @Qualifier("delegate")
    private BuildingGroupMapper delegate;
    
    @Autowired
    private BuildingMapper mapper;
    
    @Autowired
    private TenantMapper tenantMapper;

    @Autowired
    private BuildingRepository buildingRepository;
    
    @Override
    public BuildingGroupDTO toDetailDTO(BuildingGroupEntity entity) {
        BuildingEntity building = entity.getBuilding();
        TenantEntity tenant = entity.getTenant();
        
        return BuildingGroupDTO
                .builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .building(mapper.toDto(building))
                .tenant(tenantMapper.toBasicDTO(tenant))
                .build();
    }
    
    @Override
    public BuildingGroupEntity toEntity(CreateBuildingGroupDTO dto) {
        BuildingGroupEntity entity = delegate.toEntity(dto);
        entity.setBuilding(buildingRepository.findById(dto.buildingId()).orElseThrow());
        return entity;
    }
}
