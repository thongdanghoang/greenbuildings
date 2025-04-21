package greenbuildings.enterprise.mappers.decorators;

import greenbuildings.enterprise.dtos.NewGroupItemDTO;
import greenbuildings.enterprise.entities.GroupItemEntity;
import greenbuildings.enterprise.mappers.GroupItemMapper;
import greenbuildings.enterprise.repositories.BuildingGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public abstract class GroupItemMapperDecorator implements GroupItemMapper {
    
    @Autowired
    @Qualifier("delegate")
    private GroupItemMapper delegate;
    
    @Autowired
    private BuildingGroupRepository buildingGroupRepository;
    
    
    @Override
    public GroupItemEntity toEntity(NewGroupItemDTO dto) {
        GroupItemEntity entity = delegate.toEntity(dto);
        entity.setBuildingGroup(buildingGroupRepository.findById(dto.buildingGroupId()).orElseThrow());
        return entity;
    }
}
