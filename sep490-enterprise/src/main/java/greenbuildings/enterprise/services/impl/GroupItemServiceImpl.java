package greenbuildings.enterprise.services.impl;

import greenbuildings.enterprise.dtos.GroupItemDTO;
import greenbuildings.enterprise.entities.GroupItemEntity;
import greenbuildings.enterprise.mappers.GroupItemMapper;
import greenbuildings.enterprise.repositories.GroupItemRepository;
import greenbuildings.enterprise.services.GroupItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
public class GroupItemServiceImpl implements GroupItemService {
    
    private final GroupItemRepository groupItemRepository;
    private final GroupItemMapper groupItemMapper;
    
    @Override
    public GroupItemEntity create(GroupItemEntity entity) {
        return groupItemRepository.save(entity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<GroupItemEntity> findById(UUID id) {
        return groupItemRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<GroupItemEntity> findAll() {
        return groupItemRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<GroupItemEntity> findAll(Pageable pageable) {
        return groupItemRepository.findAll(pageable);
    }
    
    @Override
    public GroupItemEntity update(UUID id, GroupItemDTO dto) {
        GroupItemEntity existingEntity = groupItemRepository.findById(id)
                                                            .orElseThrow(() -> new RuntimeException("GroupItem not found with id: " + id));
        
        groupItemMapper.partialUpdate(dto, existingEntity);
        return groupItemRepository.save(existingEntity);
    }
    
    @Override
    public void delete(UUID id) {
        groupItemRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<GroupItemEntity> findByBuildingGroupId(UUID buildingGroupId) {
        return groupItemRepository.findByBuildingGroupId(buildingGroupId);
    }
} 