package greenbuildings.enterprise.rest;

import commons.springfw.impl.mappers.CommonMapper;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.dto.SearchResultDTO;
import greenbuildings.commons.api.exceptions.BusinessException;
import greenbuildings.enterprise.dtos.EmissionSourceCriteriaDTO;
import greenbuildings.enterprise.dtos.EmissionSourceDTO;
import greenbuildings.enterprise.dtos.ExcelImportDTO;
import greenbuildings.enterprise.entities.EmissionSourceEntity;
import greenbuildings.enterprise.entities.ExcelImportFileEntity;
import greenbuildings.enterprise.mappers.EmissionSourceMapper;
import greenbuildings.enterprise.mappers.ExcelImportMapper;
import greenbuildings.enterprise.services.EmissionSourceService;

import greenbuildings.enterprise.services.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/emission-source")
@RequiredArgsConstructor
public class EmissionSourceController {
    
    private final EmissionSourceService emissionSourceService;
    private final EmissionSourceMapper emissionSourceMapper;
    private final MinioService minioService;
    private final ExcelImportMapper excelImportMapper;
    
    @GetMapping
    public List<EmissionSourceDTO> findAll() {
        return emissionSourceService.findAll().stream().map(emissionSourceMapper::toDTO).toList();
    }

    @PostMapping("/search")
    public ResponseEntity<SearchResultDTO<EmissionSourceDTO>> searchEmissionSource(@RequestBody SearchCriteriaDTO<EmissionSourceCriteriaDTO> searchCriteria) {
        var pageable = CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort());
        var searchResults = emissionSourceService.search(searchCriteria, pageable);
        return ResponseEntity.ok(
                CommonMapper.toSearchResultDTO(
                        searchResults,
                        emissionSourceMapper::toDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmissionSourceDTO> findById(@PathVariable UUID id) {
        EmissionSourceDTO emissionSourceDTO = emissionSourceService.findById(id)
                .map(emissionSourceMapper::toDTO)
                .orElseThrow();
        return ResponseEntity.ok(emissionSourceDTO);
    }

    @PostMapping()
    public ResponseEntity<Void> createOrUpdateEmissionSource(@RequestBody EmissionSourceDTO emissionSourceDTO) {
        if (Objects.isNull(emissionSourceDTO.id())) {
            return createNewEmissionSource(emissionSourceDTO);
        }

        return updateExistingEmissionSource(emissionSourceDTO)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private ResponseEntity<Void> createNewEmissionSource(EmissionSourceDTO emissionSourceDTO) {
        var emissionSourceEntity = emissionSourceMapper.createNewEmissionSource(emissionSourceDTO);
        return saveAndReturnResponse(emissionSourceEntity, HttpStatus.CREATED);
    }

    private Optional<ResponseEntity<Void>> updateExistingEmissionSource(EmissionSourceDTO emissionSourceDTO) {
        return emissionSourceService.findById(emissionSourceDTO.id())
                .map(existingEntity -> {
                    var updatedEntity = emissionSourceMapper.updateEmissionSource(existingEntity, emissionSourceDTO);
                    return saveAndReturnResponse(updatedEntity, HttpStatus.OK);
                });
    }


    private ResponseEntity<Void> saveAndReturnResponse(EmissionSourceEntity entity, HttpStatus status) {
        emissionSourceService.createOrUpdate(entity);
        return ResponseEntity.status(status).build();
    }

    // upload data
    @PostMapping("/excel")
    public ResponseEntity<Void> importExcel(@RequestParam("file") MultipartFile file) {
            emissionSourceService.importFromExcel(file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //download template excel
    @GetMapping("/excel")
    public ResponseEntity<byte[]> getFileUrl() throws IOException {
        var excelImportFile = emissionSourceService.getExcelImportFile();
        var inputStream = minioService.getFile(excelImportFile.getMinioPath());
        return ResponseEntity.ok().contentType(MediaType.valueOf(excelImportFile.getContentType())).body(inputStream.readAllBytes());
    }

    // get excel import file entity
    @GetMapping("/excel-import")
    public ResponseEntity<ExcelImportDTO> getExcelImport(){
        var excelImportFile = emissionSourceService.getExcelImportFile();
        return ResponseEntity.ok(excelImportMapper.toDto(excelImportFile));
    }

    // upload template excel to minio and save to database
    @PostMapping("/upload-excel")
    public ResponseEntity<Void> uploadExcelToMinio(@RequestParam("file") MultipartFile file) {
            emissionSourceService.uploadExcelToMinio(file);
            return ResponseEntity.status(HttpStatus.OK).build();
    }
}
