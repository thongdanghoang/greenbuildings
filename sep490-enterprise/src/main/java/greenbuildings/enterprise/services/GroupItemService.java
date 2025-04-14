package greenbuildings.enterprise.services;

import greenbuildings.enterprise.dtos.GroupItemDTO;
import greenbuildings.enterprise.entities.GroupItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupItemService {
    GroupItemEntity create(GroupItemDTO dto);
    
    Optional<GroupItemEntity> findById(UUID id);
    
    List<GroupItemEntity> findAll();
    
    Page<GroupItemEntity> findAll(Pageable pageable);
    
    GroupItemEntity update(UUID id, GroupItemDTO dto);
    
    void delete(UUID id);
    
    List<GroupItemEntity> findByBuildingGroupId(UUID buildingGroupId);
} 