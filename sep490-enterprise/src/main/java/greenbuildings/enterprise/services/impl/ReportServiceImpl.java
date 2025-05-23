package greenbuildings.enterprise.services.impl;

import greenbuildings.commons.api.events.PowerBiAccessTokenAuthResult;
import greenbuildings.commons.api.exceptions.TechnicalException;
import greenbuildings.commons.api.security.PowerBiScope;
import greenbuildings.enterprise.dtos.DownloadReportDTO;
import greenbuildings.enterprise.dtos.GeneralReportDTO;
import greenbuildings.enterprise.entities.BuildingEntity;
import greenbuildings.enterprise.entities.BuildingGroupEntity;
import greenbuildings.enterprise.entities.EmissionActivityEntity;
import greenbuildings.enterprise.entities.EmissionActivityRecordEntity;
import greenbuildings.enterprise.entities.EnterpriseEntity;
import greenbuildings.enterprise.enums.EmissionUnit;
import greenbuildings.enterprise.interceptors.BuildingPermissionFilter;
import greenbuildings.enterprise.repositories.BuildingRepository;
import greenbuildings.enterprise.repositories.EmissionActivityRecordRepository;
import greenbuildings.enterprise.repositories.EmissionActivityRepository;
import greenbuildings.enterprise.repositories.EnterpriseRepository;
import greenbuildings.enterprise.services.CalculationService;
import greenbuildings.enterprise.services.ReportService;
import greenbuildings.enterprise.utils.CalculationUtils;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.map.HashedMap;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Throwable.class)
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    
    public static final int START_RECORD_ROW_IDX = 31;
    
    private final BuildingRepository buildingRepository;
    private final EmissionActivityRecordRepository recordRepo;
    private final EmissionActivityRepository activityRepo;
    private final CalculationService calculationService;
    private final EnterpriseRepository enterpriseRepository;
    
    @Override
    @BuildingPermissionFilter
    public GeneralReportDTO generateReport(PowerBiAccessTokenAuthResult contextData) {
        EnterpriseEntity enterprise = enterpriseRepository.findById(contextData.enterpriseId())
                                                          .orElseThrow();
        
        List<BuildingEntity> buildings = fetchBuildingsByScope(contextData);
        if (buildings.isEmpty()) {return null;}
        
        return buildGeneralReportDTO(enterprise, buildings);
    }
    
    private List<BuildingEntity> fetchBuildingsByScope(PowerBiAccessTokenAuthResult contextData) {
        return contextData.scope() == PowerBiScope.ENTERPRISE
               ? buildingRepository.findAll()
               : buildingRepository.findAllById(new HashSet<>(contextData.buildings()));
    }
    
    private GeneralReportDTO buildGeneralReportDTO(EnterpriseEntity enterprise, List<BuildingEntity> buildings) {
        GeneralReportDTO dto = new GeneralReportDTO();
        dto.setEnterpriseName(enterprise.getName());
        dto.setEnterpriseAddress(enterprise.getAddress());
        dto.setEnterpriseEmail(enterprise.getEnterpriseEmail());
        dto.setEnterpriseHotline(enterprise.getHotline());
        
        dto.setBuildings(buildings.stream()
                                  .map(this::mapToBuildingDTO)
                                  .collect(Collectors.toList()));
        
        return dto;
    }
    
    private GeneralReportDTO.BuildingDTO mapToBuildingDTO(BuildingEntity building) {
        GeneralReportDTO dto = new GeneralReportDTO();
        GeneralReportDTO.BuildingDTO buildingDto = dto.new BuildingDTO();
        buildingDto.setName(building.getName());
        buildingDto.setAddress(building.getAddress());
        
        buildingDto.setBuildingGroups(building.getBuildingGroups().stream()
                                              .map(group -> mapToBuildingGroupDTO(dto, buildingDto, group))
                                              .collect(Collectors.toList()));
        
        return buildingDto;
    }
    
    private GeneralReportDTO.BuildingDTO.BuildingGroupDTO mapToBuildingGroupDTO(
            GeneralReportDTO dto,
            GeneralReportDTO.BuildingDTO buildingDto,
            BuildingGroupEntity group) {
        
        GeneralReportDTO.BuildingDTO.BuildingGroupDTO groupDto = buildingDto.new BuildingGroupDTO();
        groupDto.setName(group.getName());
        
        groupDto.setActivities(group.getEmissionActivities().stream()
                                    .map(activity -> mapToActivityDTO(dto, buildingDto, groupDto, activity))
                                    .collect(Collectors.toList()));
        
        return groupDto;
    }
    
    private GeneralReportDTO.BuildingDTO.BuildingGroupDTO.ActivityDTO mapToActivityDTO(
            GeneralReportDTO dto,
            GeneralReportDTO.BuildingDTO buildingDto,
            GeneralReportDTO.BuildingDTO.BuildingGroupDTO groupDto,
            EmissionActivityEntity activity) {
        
        GeneralReportDTO.BuildingDTO.BuildingGroupDTO.ActivityDTO activityDto = groupDto.new ActivityDTO();
        activityDto.setName(activity.getName());
        activityDto.setDescription(activity.getDescription());
        activityDto.setCategory(activity.getCategory());
        activityDto.setType(activity.getType() != null ? activity.getType().getName() : null);
        
        calculationService.calculate(activity.getId(), activity.getRecords());
        
        activityDto.setRecords(activity.getRecords().stream()
                                       .map(record -> mapToRecordDTO(groupDto, activityDto, record))
                                       .collect(Collectors.toList()));
        
        return activityDto;
    }
    
    private GeneralReportDTO.BuildingDTO.BuildingGroupDTO.ActivityDTO.RecordDTO mapToRecordDTO(
            GeneralReportDTO.BuildingDTO.BuildingGroupDTO groupDto,
            GeneralReportDTO.BuildingDTO.BuildingGroupDTO.ActivityDTO activityDto,
            EmissionActivityRecordEntity record) {
        
        GeneralReportDTO.BuildingDTO.BuildingGroupDTO.ActivityDTO.RecordDTO recordDto = activityDto.new RecordDTO();
        recordDto.setStartDate(record.getStartDate());
        recordDto.setEndDate(record.getEndDate());
        recordDto.setQuantity(record.getQuantity());
        recordDto.setValue(record.getValue());
        recordDto.setUnit(record.getUnit());
        recordDto.setGhg(record.getGhg());
        
        return recordDto;
    }
    
    
    @Override
    public byte[] generateReport(DownloadReportDTO downloadReport) {
        // Prepare data
        BuildingEntity building = buildingRepository.findById(downloadReport.buildingID()).orElseThrow();
        EnterpriseEntity enterprise = building.getEnterprise();
        List<EmissionActivityEntity> activities = activityRepo.findByBuildingGroupIdIn(new HashSet<>(downloadReport.selectedGroups()));
        activities.addAll(activityRepo.findByBuildingIdAndBuildingGroupIsNull(downloadReport.buildingID()));
        
        // Prepare activities - records for populating
        var records = getActivityAndRecordsMap(downloadReport, activities);
        try {
            InputStream inputStream = Thread.currentThread()
                                            .getContextClassLoader()
                                            .getResourceAsStream("files/Template_Report.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            fillEnterpriseInfo(downloadReport, sheet, enterprise, building);
            Map<UUID, Integer> groupMergeMap = new HashedMap();
            int rowIdx = START_RECORD_ROW_IDX;
            for (var entry : records.entrySet()) {
                if (entry.getValue().isEmpty()) {
                    continue;
                }
                var activity = entry.getKey();
                var group = activity.getBuildingGroup();
                
                
                var activityRecords = entry.getValue();
                calculationService.calculate(activity.getId(), activityRecords);
                
                int startRow = rowIdx;
                int endRow = rowIdx + activityRecords.size() - 1;
                for (var recordEntity : activityRecords) {
                    groupMergeMap.put(group != null ? group.getId() : null, rowIdx);
                    Row row = sheet.createRow(rowIdx++);
                    fillActivityDataInRow(recordEntity, group, row, activity);
                }
                handleMergeCommonFieldInActivities(endRow, startRow, sheet);
            }
            handleMergeBuildingGroupName(groupMergeMap, sheet);
            
            try (var byteArrayOutputStream = new ByteArrayOutputStream()) {
                workbook.write(byteArrayOutputStream);
                return byteArrayOutputStream.toByteArray();
            }
        } catch (IOException ex) {
            throw new TechnicalException(ex);
        }
    }
    
    @NotNull
    private HashMap<EmissionActivityEntity, List<EmissionActivityRecordEntity>> getActivityAndRecordsMap(DownloadReportDTO downloadReport,
                                                                                                         List<EmissionActivityEntity> activities) {
        if (activities.isEmpty() || downloadReport.selectedGroups().isEmpty() || downloadReport.startDate() == null || downloadReport.endDate() == null) {
            return new HashMap<>();
        }
        var records = new HashMap<EmissionActivityEntity, List<EmissionActivityRecordEntity>>() {
            {
                activities.stream()
                          .sorted((a1, a2) -> {
                              if (a1.getBuildingGroup() == null || a2.getBuildingGroup() == null) {
                                  return a1.getBuildingGroup() == null ? 1 : -1;
                              }
                              return a1.getBuildingGroup().getId().compareTo(a2.getBuildingGroup().getId());
                          })
                          .forEach(activity -> {
                              var recordsByActivity = recordRepo
                                      .findAllByEmissionActivityIdAndDateRangeExclusive(activity.getId(), downloadReport.startDate(), downloadReport.endDate());
                              put(activity, recordsByActivity);
                          });
            }
        };
        for (var activity : activities) {
            var recordsByActivity = recordRepo
                    .findAllByEmissionActivityIdAndDateRangeExclusive(activity.getId(), downloadReport.startDate(), downloadReport.endDate());
            records.put(activity, recordsByActivity);
        }
        return records;
    }
    
    private void fillActivityDataInRow(EmissionActivityRecordEntity recordEntity, BuildingGroupEntity group, Row row, EmissionActivityEntity activity) {
        int colIdx = 0;
        if (group != null) {
            row.createCell(colIdx++).setCellValue(group.getName());
        } else {
            row.createCell(colIdx++).setCellValue("-");
        }
        
        row.createCell(colIdx++).setCellValue(activity.getName());
        row.createCell(colIdx++).setCellValue(activity.getType() != null ? activity.getType().getName() : "-");
        row.createCell(colIdx++).setCellValue(activity.getCategory());
        row.createCell(colIdx++).setCellValue(activity.getDescription() != null ? activity.getDescription() : "-");
        row.createCell(colIdx++).setCellValue(activity.getEmissionFactorEntity().getSource().getNameVN());
        row.createCell(colIdx++).setCellValue(activity.getEmissionFactorEntity().getNameVN());
        if (activity.getEmissionFactorEntity().getEnergyConversion() != null) {
            row.createCell(colIdx++).setCellValue(activity.getEmissionFactorEntity().getEnergyConversion().getFuel().getNameVN());
        } else {
            row.createCell(colIdx++).setCellValue("-");
        }
        row.createCell(colIdx++).setCellValue(recordEntity.getStartDate().toString());
        row.createCell(colIdx++).setCellValue(recordEntity.getEndDate().toString());
        row.createCell(colIdx++).setCellValue(recordEntity.getQuantity());
        row.createCell(colIdx++).setCellValue(recordEntity.getValue().toString());
        row.createCell(colIdx++).setCellValue(recordEntity.getUnit().name());
        BigDecimal ghg = recordEntity.getGhg().setScale(2, RoundingMode.HALF_UP);
        row.createCell(colIdx++).setCellValue(ghg.toString());
        ghg = CalculationUtils.convertUnit(EmissionUnit.KILOGRAM, EmissionUnit.GIGAGRAM, ghg);
        row.createCell(colIdx++).setCellValue(ghg.toString());
    }
    
    private static void handleMergeCommonFieldInActivities(int endRow, int startRow, XSSFSheet sheet) {
        if (endRow > startRow) {
            sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 1, 1));
            sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 2, 2));
            sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 3, 3));
            sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 4, 4));
            sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 5, 5));
            sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 6, 6));
            sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 7, 7));
        }
    }
    
    private void handleMergeBuildingGroupName(Map<UUID, Integer> groupMergeMap, XSSFSheet sheet) {
        AtomicInteger currentRow = new AtomicInteger(START_RECORD_ROW_IDX);
        groupMergeMap
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(entry -> {
                    if (entry.getValue() > currentRow.get()) {
                        sheet.addMergedRegion(new CellRangeAddress(currentRow.get(), entry.getValue(), 0, 0));
                    }
                    currentRow.getAndSet(entry.getValue() + 1);
                });
    }
    
    private void fillEnterpriseInfo(DownloadReportDTO downloadReport, XSSFSheet sheet, EnterpriseEntity enterprise, BuildingEntity building) {
        // Enterprise
        sheet.getRow(3).getCell(1).setCellValue(enterprise.getName());
        sheet.getRow(4).getCell(1).setCellValue(enterprise.getAddress());
        sheet.getRow(5).getCell(1).setCellValue(enterprise.getRepresentativeName());
        sheet.getRow(6).getCell(1).setCellValue(enterprise.getEnterpriseEmail());
        sheet.getRow(7).getCell(1).setCellValue(enterprise.getHotline());
        sheet.getRow(8).getCell(1).setCellValue(LocalDate.now().toString());
        // Building
        sheet.getRow(3).getCell(4).setCellValue(building.getName());
        sheet.getRow(4).getCell(4).setCellValue(building.getName());
        sheet.getRow(5).getCell(4).setCellValue(downloadReport.startDate().toString() + " - " + downloadReport.endDate().toString());
    }
}
