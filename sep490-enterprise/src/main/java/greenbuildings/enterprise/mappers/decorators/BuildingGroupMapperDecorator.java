package greenbuildings.enterprise.mappers.decorators;

import greenbuildings.enterprise.dtos.CreateBuildingGroupDTO;
import greenbuildings.enterprise.entities.BuildingGroupEntity;
import greenbuildings.enterprise.mappers.BuildingGroupMapper;
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
    private BuildingRepository buildingRepository;

    @Override
    public BuildingGroupEntity toEntity(CreateBuildingGroupDTO dto) {
        BuildingGroupEntity entity = delegate.toEntity(dto);
        entity.setBuilding(buildingRepository.findById(dto.buildingId()).orElseThrow());
        return entity;
    }
}
