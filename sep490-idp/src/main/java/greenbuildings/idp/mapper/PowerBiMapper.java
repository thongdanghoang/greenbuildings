package greenbuildings.idp.mapper;

import greenbuildings.idp.dto.powerbi.PowerBiAuthorityDTO;
import greenbuildings.idp.entity.PowerBiAuthority;
import greenbuildings.idp.entity.UserEntity;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PowerBiMapper {
    
    @Mapping(target = "user", expression = "java(user)")
    PowerBiAuthority generateApiKey(PowerBiAuthorityDTO request, @Context UserEntity user);
    
    PowerBiAuthorityDTO toPowerBiAuthority(PowerBiAuthority powerBiAuthority);
    
    @Mapping(target = "user", expression = "java(user)")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "apiKey", ignore = true)
    @Mapping(target = "expirationTime", ignore = true)
    PowerBiAuthority updateApiKey(PowerBiAuthorityDTO source, @MappingTarget PowerBiAuthority target, @Context UserEntity user);
}
