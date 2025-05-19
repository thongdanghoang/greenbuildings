package greenbuildings.enterprise.rest;

import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.dto.SearchResultDTO;
import greenbuildings.commons.springfw.impl.mappers.CommonMapper;
import greenbuildings.enterprise.dtos.EmissionFactorCriteriaDTO;
import greenbuildings.enterprise.dtos.EmissionFactorDTO;
import greenbuildings.enterprise.dtos.ExcelImportDTO;
import greenbuildings.enterprise.entities.EmissionFactorEntity;
import greenbuildings.enterprise.mappers.EmissionFactorMapper;
import greenbuildings.enterprise.mappers.ExcelImportMapper;
import greenbuildings.enterprise.services.EmissionFactorService;
import greenbuildings.enterprise.services.impl.MinioService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/emission-factor")
@RequiredArgsConstructor
public class EmissionFactorController {
    
    private final EmissionFactorService emissionFactorService;
    private final EmissionFactorMapper emissionFactorMapper;
    private final MinioService minioService;
    private final ExcelImportMapper excelImportMapper;
    
    @GetMapping
    public ResponseEntity<List<EmissionFactorDTO>> findAll() {
        return ResponseEntity.ok(emissionFactorService.findAllAvailable().stream().map(emissionFactorMapper::toDTO).toList());
    }
    
    @GetMapping("/find-by-source/{sourceId}")
    public List<EmissionFactorDTO> findBySource(@PathVariable UUID sourceId) {
        return emissionFactorService.findBySource(sourceId).stream().map(emissionFactorMapper::toDTO).toList();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EmissionFactorDTO> findById(@PathVariable UUID id) {
        EmissionFactorDTO emissionFactorDTO = emissionFactorService.findById(id)
                                                                   .map(emissionFactorMapper::toDTO)
                                                                   .orElseThrow();
        return ResponseEntity.ok(emissionFactorDTO);
    }
    
    @PostMapping("/search")
    public ResponseEntity<SearchResultDTO<EmissionFactorDTO>> searchEmissionFactor(@RequestBody SearchCriteriaDTO<EmissionFactorCriteriaDTO> searchCriteria) {
        var pageable = CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort());
        var searchResults = emissionFactorService.search(searchCriteria, pageable);
        return ResponseEntity.ok(
                CommonMapper.toSearchResultDTO(
                        searchResults,
                        emissionFactorMapper::toDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmissionFactor(@PathVariable UUID id) {
        emissionFactorService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping()
    public ResponseEntity<Void> createOrEmissionFactor(@RequestBody EmissionFactorDTO emissionFactorDTO) {
        if (Objects.isNull(emissionFactorDTO.id())) {
            return createNewEmissionSource(emissionFactorDTO);
        }
        
        return updateExistingEmissionSource(emissionFactorDTO)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    private ResponseEntity<Void> createNewEmissionSource(EmissionFactorDTO emissionFactorDTO) {
        var emissionConversionEntity = emissionFactorMapper.toEntity(emissionFactorDTO);
        return saveAndReturnResponse(emissionConversionEntity, HttpStatus.CREATED);
    }
    
    private Optional<ResponseEntity<Void>> updateExistingEmissionSource(EmissionFactorDTO emissionFactorDTO) {
        return emissionFactorService.findById(emissionFactorDTO.id())
                                    .map(existingEntity -> {
                                        var updatedEntity = emissionFactorMapper.toEntityUpdate(existingEntity, emissionFactorDTO);
                                        return saveAndReturnResponse(updatedEntity, HttpStatus.OK);
                                    });
    }
    
    
    private ResponseEntity<Void> saveAndReturnResponse(EmissionFactorEntity entity, HttpStatus status) {
        emissionFactorService.createOrUpdate(entity);
        return ResponseEntity.status(status).build();
    }
    
    // upload data
    @PostMapping("/excel")
    public ResponseEntity<Void> importExcel(@RequestParam("file") MultipartFile file) {
        emissionFactorService.importFactorFromExcel(file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    //download template excel
    @GetMapping("/excel")
    public ResponseEntity<byte[]> getFileUrl() throws IOException {
        var excelImportFile = emissionFactorService.getExcelImportFile();
        var inputStream = minioService.getFile(excelImportFile.getMinioPath());
        return ResponseEntity.ok().contentType(MediaType.valueOf(excelImportFile.getContentType())).body(inputStream.readAllBytes());
    }
    
    // get excel import file entity
    @GetMapping("/excel-import")
    public ResponseEntity<ExcelImportDTO> getExcelImport() {
        var excelImportFile = emissionFactorService.getExcelImportFile();
        return ResponseEntity.ok(excelImportMapper.toDto(excelImportFile));
    }
    
    // upload template excel to minio and save to database
    @PostMapping("/upload-excel")
    public ResponseEntity<Void> uploadExcelToMinio(@RequestParam("file") MultipartFile file) {
        emissionFactorService.uploadExcelToMinio(file);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
    
}
