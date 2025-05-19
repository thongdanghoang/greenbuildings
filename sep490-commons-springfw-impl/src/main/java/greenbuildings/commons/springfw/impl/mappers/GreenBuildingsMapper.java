package greenbuildings.commons.springfw.impl.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

public interface GreenBuildingsMapper<MODEL, ENTITY> {
    
    MODEL toModel(ENTITY entity);
    
    ENTITY toEntity(MODEL model);
    
    void partialUpdate(ENTITY source, @MappingTarget ENTITY target);
    
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
    )
    void fullUpdate(ENTITY source, @MappingTarget ENTITY target);
}
