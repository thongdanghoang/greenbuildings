package greenbuildings.enterprise.rest;

import commons.springfw.impl.mappers.CommonMapper;
import greenbuildings.commons.api.dto.SearchCriteriaDTO;
import greenbuildings.commons.api.dto.SearchResultDTO;
import greenbuildings.enterprise.dtos.*;
import greenbuildings.enterprise.entities.EmissionSourceEntity;
import greenbuildings.enterprise.entities.EnergyConversionEntity;
import greenbuildings.enterprise.mappers.EnergyConversionMapper;
import greenbuildings.enterprise.mappers.ExcelImportMapper;
import greenbuildings.enterprise.services.EnergyConversionService;
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
@RequestMapping("/energy-conversion")
@RequiredArgsConstructor
public class EnergyConversionController {
    
    private final EnergyConversionService energyConversionService;
    private final EnergyConversionMapper energyConversionMapper;
    private final MinioService minioService;
    private final ExcelImportMapper excelImportMapper;
    
    @GetMapping
    public List<EnergyConversionDTO> findAll() {
        return energyConversionService.findAll().stream().map(energyConversionMapper::toDTO).toList();
    }
    @GetMapping("/{id}")
    public ResponseEntity<EnergyConversionDTO> findById(@PathVariable UUID id) {
        EnergyConversionDTO energyConversionDTO = energyConversionService.findById(id)
                .map(energyConversionMapper::toDTO)
                .orElseThrow();
        return ResponseEntity.ok(energyConversionDTO);
    }

    @PostMapping("/search")
    public ResponseEntity<SearchResultDTO<EnergyConversionDTO>> searchFuel(@RequestBody SearchCriteriaDTO<FuelCriteriaDTO> searchCriteria) {
        var pageable = CommonMapper.toPageable(searchCriteria.page(), searchCriteria.sort());
        var searchResults = energyConversionService.search(searchCriteria, pageable);
        return ResponseEntity.ok(
                CommonMapper.toSearchResultDTO(
                        searchResults,
                        energyConversionMapper::toDTO));
    }

    @PostMapping()
    public ResponseEntity<Void> createOrUpdateEnergyConversion(@RequestBody EnergyConversionDTO energyConversionDTO) {
        if (Objects.isNull(energyConversionDTO.id())) {
            return createNewEmissionSource(energyConversionDTO);
        }

        return updateExistingEmissionSource(energyConversionDTO)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private ResponseEntity<Void> createNewEmissionSource(EnergyConversionDTO energyConversionDTO) {
        var emissionConversionEntity = energyConversionMapper.createNewEnergyConversion(energyConversionDTO);
        return saveAndReturnResponse(emissionConversionEntity, HttpStatus.CREATED);
    }

    private Optional<ResponseEntity<Void>> updateExistingEmissionSource(EnergyConversionDTO energyConversionDTO) {
        return energyConversionService.findById(energyConversionDTO.id())
                .map(existingEntity -> {
                    var updatedEntity = energyConversionMapper.updateEnergyConversion(existingEntity, energyConversionDTO);
                    return saveAndReturnResponse(updatedEntity, HttpStatus.OK);
                });
    }


    private ResponseEntity<Void> saveAndReturnResponse(EnergyConversionEntity entity, HttpStatus status) {
        energyConversionService.createOrUpdate(entity);
        return ResponseEntity.status(status).build();
    }

    // upload data
    @PostMapping("/excel")
    public ResponseEntity<Void> importExcel(@RequestParam("file") MultipartFile file) {
        energyConversionService.importFuelFromExcel(file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    //download template excel
    @GetMapping("/excel")
    public ResponseEntity<byte[]> getFileUrl() throws IOException {
        var excelImportFile = energyConversionService.getExcelImportFile();
        var inputStream = minioService.getFile(excelImportFile.getMinioPath());
        return ResponseEntity.ok().contentType(MediaType.valueOf(excelImportFile.getContentType())).body(inputStream.readAllBytes());
    }

    // get excel import file entity
    @GetMapping("/excel-import")
    public ResponseEntity<ExcelImportDTO> getExcelImport(){
        var excelImportFile = energyConversionService.getExcelImportFile();
        return ResponseEntity.ok(excelImportMapper.toDto(excelImportFile));
    }

    // upload template excel to minio and save to database
    @PostMapping("/upload-excel")
    public ResponseEntity<Void> uploadExcelToMinio(@RequestParam("file") MultipartFile file) {
        energyConversionService.uploadExcelToMinio(file);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
