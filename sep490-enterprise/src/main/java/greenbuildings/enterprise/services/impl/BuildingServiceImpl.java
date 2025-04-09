package greenbuildings.enterprise.services.impl;

import commons.springfw.impl.utils.SecurityUtils;
import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.commons.api.exceptions.TechnicalException;
import greenbuildings.enterprise.dtos.DownloadReportDTO;
import greenbuildings.enterprise.entities.BuildingEntity;
import greenbuildings.enterprise.entities.EmissionActivityEntity;
import greenbuildings.enterprise.entities.EmissionActivityRecordEntity;
import greenbuildings.enterprise.enums.EmissionUnit;
import greenbuildings.enterprise.repositories.BuildingRepository;
import greenbuildings.enterprise.repositories.EmissionActivityRecordRepository;
import greenbuildings.enterprise.repositories.EmissionActivityRepository;
import greenbuildings.enterprise.services.BuildingService;
import greenbuildings.enterprise.services.CalculationService;
import greenbuildings.enterprise.utils.CalculationUtils;
import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Throwable.class)
@RequiredArgsConstructor
public class BuildingServiceImpl implements BuildingService {
    
    private final BuildingRepository buildingRepository;
    private final EmissionActivityRecordRepository recordRepo;
    private final EmissionActivityRepository activityRepo;
    private final CalculationService calculationService;
    
    @Override
    public BuildingEntity createBuilding(BuildingEntity building) {
        UUID enterpriseId = SecurityUtils.getCurrentUserEnterpriseId().orElseThrow();
        if (building.getId() != null) {
            var oldBuilding = buildingRepository.findById(building.getId()).orElseThrow();
            if (oldBuilding.getName().equals(building.getName())) {
                return buildingRepository.save(building);
            }
        }
        if (buildingRepository.existsByNameBuildingInEnterprise(building.getName(), enterpriseId)) {
            throw new BusinessException("name", "business.buildings.name.exist");
        }
        return buildingRepository.save(building);
    }
    
    @Override
    public Optional<BuildingEntity> findById(UUID id) {
        return buildingRepository.findByIdWithGraph(id);
    }
    
    @Override
    public Page<BuildingEntity> getEnterpriseBuildings(UUID enterpriseId, Pageable page) {
        return buildingRepository.findByEnterpriseId(enterpriseId, page);
    }
    
    @Override
    public void deleteBuilding(UUID id) {
        UUID enterpriseId = SecurityUtils.getCurrentUserEnterpriseId().orElseThrow();
        var optionalBuilding = buildingRepository.findByIdAndEnterpriseId(id, enterpriseId);
        if (optionalBuilding.isPresent()) {
            buildingRepository.deleteById(id);
        }
    }
    
    @Override
    public byte[] generateReport(DownloadReportDTO downloadReport) {
        BuildingEntity building = buildingRepository.findById(downloadReport.buildingID()).orElseThrow();
        List<EmissionActivityEntity> activities = activityRepo.findAllByIdIn(downloadReport.selectedActivities());
        
        if (activities.size() != downloadReport.selectedActivities().size()) {
            throw new BusinessException("activities", "validation.business.activities.notFound");
        }
        
        Map<EmissionActivityEntity, List<EmissionActivityRecordEntity>> records = new HashMap<>();
        
        for (EmissionActivityEntity activity : activities) {
            List<EmissionActivityRecordEntity> recordsByActivity = recordRepo.findAllByEmissionActivityEntityId(activity.getId());
            records.put(activity, recordsByActivity);
        }
        try {
            // TODO: store file in cache
            File file = ResourceUtils.getFile("classpath:files/Template_Report.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowIdx = 2;
            
            for (Map.Entry<EmissionActivityEntity, List<EmissionActivityRecordEntity>> entry : records.entrySet()) {
                EmissionActivityEntity activity = entry.getKey();
                List<EmissionActivityRecordEntity> activityRecords = entry.getValue();
                calculationService.calculate(activity.getId(), activityRecords);
                int startRow = rowIdx;
                int endRow = rowIdx + activityRecords.size() - 1;
                for (EmissionActivityRecordEntity recordEntity : activityRecords) {
                    Row row = sheet.createRow(rowIdx++);
                    int colIdx = 0;
                    
                    row.createCell(colIdx++).setCellValue(activity.getName());
                    row.createCell(colIdx++).setCellValue(activity.getEmissionFactorEntity().getSource().getNameVN());
                    row.createCell(colIdx++).setCellValue(activity.getEmissionFactorEntity().getNameVN());
                    if (activity.getEmissionFactorEntity().getEnergyConversion() != null) {
                        row.createCell(colIdx++).setCellValue(activity.getEmissionFactorEntity().getEnergyConversion().getFuel().getNameVN());
                    } else {
                        row.createCell(colIdx++).setCellValue("-");
                    }
                    row.createCell(colIdx++).setCellValue(recordEntity.getStartDate().toString() + "-" + recordEntity.getEndDate().toString());
                    row.createCell(colIdx++).setCellValue(recordEntity.getValue().toString());
                    row.createCell(colIdx++).setCellValue(recordEntity.getQuantity());
                    row.createCell(colIdx++).setCellValue(recordEntity.getUnit().name());
                    BigDecimal ghg = recordEntity.getGhg().setScale(2, RoundingMode.HALF_UP);
                    ghg = CalculationUtils.convertUnit(EmissionUnit.KILOGRAM, EmissionUnit.GIGAGRAM, ghg);
                    row.createCell(colIdx++).setCellValue(ghg.toString());
                }
                if (endRow > startRow) {
                    sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 0, 0));
                    sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 1, 1));
                    sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 2, 2));
                    sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 3, 3));
                }
            }
            
            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                workbook.write(byteArrayOutputStream);
                
                return byteArrayOutputStream.toByteArray();
            }
        } catch (IOException | InvalidFormatException ex) {
            throw new TechnicalException(ex);
        }
    }
    
    @Override
    public List<BuildingEntity> findBuildingsByEnterpriseId(UUID enterpriseId) {
        return buildingRepository.findAllByEnterpriseId(enterpriseId);
    }
    
}
