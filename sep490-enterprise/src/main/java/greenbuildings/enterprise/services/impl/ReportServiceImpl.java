package greenbuildings.enterprise.services.impl;

import greenbuildings.commons.api.exceptions.TechnicalException;
import greenbuildings.enterprise.dtos.DownloadReportDTO;
import greenbuildings.enterprise.entities.BuildingEntity;
import greenbuildings.enterprise.entities.BuildingGroupEntity;
import greenbuildings.enterprise.entities.EmissionActivityEntity;
import greenbuildings.enterprise.entities.EmissionActivityRecordEntity;
import greenbuildings.enterprise.entities.EnterpriseEntity;
import greenbuildings.enterprise.enums.EmissionUnit;
import greenbuildings.enterprise.repositories.BuildingGroupRepository;
import greenbuildings.enterprise.repositories.BuildingRepository;
import greenbuildings.enterprise.repositories.EmissionActivityRecordRepository;
import greenbuildings.enterprise.repositories.EmissionActivityRepository;
import greenbuildings.enterprise.services.CalculationService;
import greenbuildings.enterprise.services.ReportService;
import greenbuildings.enterprise.utils.CalculationUtils;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.map.HashedMap;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional(rollbackFor = Throwable.class)
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    
    public static final int START_RECORD_ROW_IDX = 31;
    public static final int GROUP_INFO_ROW_IDX = 24;
    public static final int GROUP_INFO_COL_IDX = 1;
    
    private final BuildingRepository buildingRepository;
    private final EmissionActivityRecordRepository recordRepo;
    private final EmissionActivityRepository activityRepo;
    private final CalculationService calculationService;
    private final BuildingGroupRepository buildingGroupRepository;
    
    @Override
    public byte[] generateReport(DownloadReportDTO downloadReport) {
        // Prepare data
        BuildingEntity building = buildingRepository.findById(downloadReport.buildingID()).orElseThrow();
        EnterpriseEntity enterprise = building.getEnterprise();
        List<BuildingGroupEntity> buildingGroups = buildingGroupRepository.findAllById(downloadReport.selectedGroups());
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
            XSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
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
            fillGroupsInfo(sheet, records, buildingGroups, cellStyle);
            
            try (var byteArrayOutputStream = new ByteArrayOutputStream()) {
                workbook.write(byteArrayOutputStream);
                return byteArrayOutputStream.toByteArray();
            }
        } catch (IOException ex) {
            throw new TechnicalException(ex);
        }
    }
    
    private void fillGroupsInfo(XSSFSheet sheet, HashMap<EmissionActivityEntity, List<EmissionActivityRecordEntity>> records,
                                List<BuildingGroupEntity> groups, XSSFCellStyle cellStyle) {
        HashMap<BuildingGroupEntity, List<EmissionActivityEntity>> groupActivities = new HashMap<>();
        
        groups.forEach(g -> groupActivities.put(g, new ArrayList<>()));
        
        for (var entry : records.entrySet()) {
            if (entry.getKey().getBuildingGroup() != null) {
                groupActivities.get(entry.getKey().getBuildingGroup()).add(entry.getKey());
            }
        }
        int colCount = 0;
        for (var gr : groupActivities.entrySet()) {
            BuildingGroupEntity group = gr.getKey();
            List<EmissionActivityEntity> activities = gr.getValue();
            int count = 0;
            BigDecimal totalGhg = BigDecimal.ZERO;
            for (var activity : activities) {
                List<EmissionActivityRecordEntity> emissionActivityRecordEntities = records.get(activity);
                count += emissionActivityRecordEntities.size();
                for (var record : emissionActivityRecordEntities) {
                    BigDecimal ghg = record.getGhg().setScale(2, RoundingMode.HALF_UP);
                    ghg = CalculationUtils.convertUnit(EmissionUnit.KILOGRAM, EmissionUnit.GIGAGRAM, ghg);
                    totalGhg = totalGhg.add(ghg);
                }
            }
            XSSFCell cell1 = sheet.getRow(GROUP_INFO_ROW_IDX).createCell(GROUP_INFO_COL_IDX + colCount);
            cell1.setCellValue(group.getName());
            cell1.setCellStyle(cellStyle);
            XSSFCell cell2 = sheet.getRow(GROUP_INFO_ROW_IDX + 1).createCell(GROUP_INFO_COL_IDX + colCount);
            cell2.setCellValue(count);
            cell2.setCellStyle(cellStyle);
            XSSFCell cell3 = sheet.getRow(GROUP_INFO_ROW_IDX + 2).createCell(GROUP_INFO_COL_IDX + colCount);
            cell3.setCellValue(totalGhg.toString());
            cell3.setCellStyle(cellStyle);
            colCount++;
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
