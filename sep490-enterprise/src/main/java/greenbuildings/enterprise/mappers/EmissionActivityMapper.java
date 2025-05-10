package greenbuildings.enterprise.mappers;

import greenbuildings.commons.api.views.DateRangeView;
import greenbuildings.enterprise.dtos.CreateEmissionActivityDTO;
import greenbuildings.enterprise.dtos.EmissionActivityDTO;
import greenbuildings.enterprise.dtos.EmissionActivityDetailsDTO;
import greenbuildings.enterprise.dtos.EmissionActivityTableView;
import greenbuildings.enterprise.entities.EmissionActivityEntity;
import greenbuildings.enterprise.entities.EmissionActivityRecordEntity;
import greenbuildings.enterprise.entities.EmissionFactorEntity;
import greenbuildings.enterprise.entities.EmissionSourceEntity;
import greenbuildings.enterprise.entities.RecordFileEntity;
import greenbuildings.enterprise.mappers.decorators.EmissionActivityMapperDecorator;
import greenbuildings.enterprise.models.ActivityRecordDateRange;
import greenbuildings.enterprise.views.emission_activities.EmissionActivityView;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@DecoratedWith(EmissionActivityMapperDecorator.class)
public interface EmissionActivityMapper {
    
    @Mapping(target = "buildingGroupID", ignore = true)
    @Mapping(target = "emissionFactorID", ignore = true)
    @Mapping(target = "records", ignore = true)
    EmissionActivityDTO toDTO(EmissionActivityEntity emissionActivityEntity);
    
    @Mapping(target = "emissionFactor", source = "emissionFactorEntity")
    @Mapping(target = "records", ignore = true)
    EmissionActivityTableView toTableView(EmissionActivityEntity emissionActivityEntity);
    
    @Mapping(target = "building", source = "building.id")
    @Mapping(target = "emissionFactor", source = "emissionFactorEntity")
    @Mapping(target = "type", source = "type.name")
    EmissionActivityView toEmissionActivityView(EmissionActivityEntity source);
    
    EmissionActivityView.EmissionActivityRecordView toEmissionActivityRecordView(EmissionActivityRecordEntity source);
    
    @Mapping(source = "source", target = "emissionSourceDTO")
    EmissionActivityView.EmissionActivityFactorView toEmissionActivityFactorView(EmissionFactorEntity source);
    
    EmissionActivityView.EmissionActivityFactorView.EmissionSourceView toEmissionSourceView(EmissionSourceEntity source);
    
    EmissionActivityView.EmissionActivityRecordView.RecordFileView toRecordFileView(RecordFileEntity source);
    
    @Mapping(target = "building.id", source = "buildingId")
    @Mapping(target = "buildingGroup", ignore = true)
    @Mapping(target = "emissionFactorEntity", ignore = true)
    @Mapping(target = "type", ignore = true)
    EmissionActivityEntity createNewActivity(CreateEmissionActivityDTO dto);

    @Mapping(target = "buildingId", source = "building.id")
    @Mapping(target = "buildingGroup", source = "buildingGroup")
    @Mapping(target = "buildingGroup.emissionActivities", ignore = true)
    @Mapping(target = "buildingGroup.building", ignore = true)
    @Mapping(target = "buildingGroup.tenant", ignore = true)
    @Mapping(target = "emissionFactor", source = "emissionFactorEntity")
    @Mapping(target = "emissionFactor.directEmission", source = "emissionFactorEntity.directEmission")
    @Mapping(target = "emissionFactor.emissionSourceDTO", source = "emissionFactorEntity.source")
    @Mapping(target = "emissionFactor.energyConversionDTO", source = "emissionFactorEntity.energyConversion")
    @Mapping(target = "records", ignore = true)
    @Mapping(target = "type", source = "type")
    EmissionActivityDetailsDTO toDetailsDTO(EmissionActivityEntity entity);
    
    @Mapping(target = "building.id", source = "buildingId")
    @Mapping(target = "emissionFactorEntity", ignore = true)
    @Mapping(target = "buildingGroup", ignore = true)
    @Mapping(target = "type", ignore = true)
    EmissionActivityEntity updateActivity(CreateEmissionActivityDTO dto);
    
    @Mapping(target = "fromInclusive", source = "startDate")
    @Mapping(target = "toInclusive", source = "endDate")
    DateRangeView toDateRangeView(ActivityRecordDateRange source);
    
    List<DateRangeView> toDateRangeView(List<ActivityRecordDateRange> source);
}
