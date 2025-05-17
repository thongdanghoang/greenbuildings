package greenbuildings.enterprise.services;

import greenbuildings.commons.api.dto.PageDTO;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.enterprise.TestcontainersConfigs;
import greenbuildings.enterprise.dtos.EmissionActivityRecordCriteria;
import greenbuildings.enterprise.entities.EmissionActivityRecordEntity;
import greenbuildings.enterprise.repositories.AssetRepository;
import greenbuildings.enterprise.repositories.EmissionActivityRecordRepository;
import greenbuildings.enterprise.repositories.EmissionActivityRepository;
import greenbuildings.enterprise.repositories.RecordFileRepository;
import greenbuildings.enterprise.services.impl.EmissionActivityRecordServiceImpl;
import greenbuildings.enterprise.services.impl.MinioService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public class EmissionActivityRecordServiceTest extends TestcontainersConfigs {
    
    @Mock
    MinioService minioService;
    
    @Autowired
    private EmissionActivityRecordRepository recordRepository;
    @Autowired
    private RecordFileRepository fileRepository;
    @Autowired
    private CalculationService calculationService;
    @Autowired
    private AssetRepository assetRepository;
    @Autowired
    private EmissionActivityRepository emissionActivityRepository;
    
    EmissionActivityRecordService emissionActivityRecordService;
    
    @Override
    protected String getBaseUrl() {
        return "";
    }
    
    @BeforeEach
    void setUp() {
        emissionActivityRecordService = new EmissionActivityRecordServiceImpl(
                recordRepository,
                minioService,
                fileRepository,
                calculationService,
                assetRepository,
                emissionActivityRepository
        );
    }
    
    @Test
    void search() {
        var emissionActivityEntity = insertActivity(true);
        var startDate = LocalDate.now().minusMonths(60);
        for (int i = 0; i < 10; i++) {
            var endDate = startDate.plusMonths(i);
            insertRecord(emissionActivityEntity, startDate, endDate);
            startDate = endDate.plusDays(1);
        }
        var criteria = new EmissionActivityRecordCriteria(emissionActivityEntity.getId());
        var searchCriteria = SearchCriteriaDTO.of(PageDTO.of(10, 0), null, criteria);
        
        var searchResults = emissionActivityRecordService.search(searchCriteria);
        
        Assertions.assertEquals(10, searchResults.getTotalElements());
    }
    
    @Test
    void createWithFile() {
        var emissionActivity = insertActivity(true);
        var record = new EmissionActivityRecordEntity();
        var emissionUnit = emissionActivity.getEmissionFactorEntity().isDirectEmission()
                           ? emissionActivity.getEmissionFactorEntity().getEmissionUnitDenominator()
                           : emissionActivity.getEmissionFactorEntity().getEmissionUnitNumerator();
        record.setUnit(emissionUnit);
        record.setValue(new BigDecimal(RandomStringUtils.randomNumeric(6, 9)));
        record.setStartDate(LocalDate.now().minusMonths(1));
        record.setEndDate(LocalDate.now());
        record.setEmissionActivity(emissionActivity);
        
        var file = Mockito.mock(MultipartFile.class);
        Mockito.when(file.getOriginalFilename()).thenReturn(randomString());
        Mockito.when(file.getContentType()).thenReturn(randomString());
        Mockito.when(file.getSize()).thenReturn(randomLong(6));
        Mockito.when(minioService.uploadFile(Mockito.any(), Mockito.any())).thenReturn(randomString());
        Mockito.doNothing().when(minioService).deleteFile(Mockito.any());
        emissionActivityRecordService.createWithFile(record, file);
        
        Assertions.assertNotNull(record.getId());
        
        record = recordRepository.findById(record.getId()).orElseThrow();
        var newValue = new BigDecimal(RandomStringUtils.randomNumeric(6, 9));
        record.setValue(newValue);
        emissionActivityRecordService.createWithFile(record, file);
        
        Assertions.assertEquals(newValue, record.getValue());
        
        var fileId = recordRepository.findById(record.getId()).orElseThrow().getFile().getId();
        deleteFile(emissionActivityRecordService, record.getId(), fileId);
        
        Assertions.assertFalse(fileRepository.existsById(fileId));
        
        emissionActivityRecordService.deleteRecords(Set.of(record.getId()));
        
        Assertions.assertFalse(recordRepository.existsById(record.getId()));
    }
    
    void deleteFile(EmissionActivityRecordService emissionActivityRecordService, UUID recordId, UUID fileId) {
        emissionActivityRecordService.deleteFile(recordId, fileId);
    }
}
