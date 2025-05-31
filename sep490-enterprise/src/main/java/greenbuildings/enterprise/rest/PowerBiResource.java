package greenbuildings.enterprise.rest;

import greenbuildings.commons.api.security.PowerBiScope;
import greenbuildings.enterprise.entities.EmissionActivityRecordEntity;
import greenbuildings.enterprise.mappers.PowerBiMapper;
import greenbuildings.enterprise.securities.PowerBiAuthenticationFacade;
import greenbuildings.enterprise.services.EmissionActivityService;
import greenbuildings.enterprise.views.powerbi.EmissionActivityGhg;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/power-bi/{apiKey}")
@RequiredArgsConstructor
@Slf4j
public class PowerBiResource {
    
    private final PowerBiAuthenticationFacade authenticationFacade;
    private final EmissionActivityService emissionActivityService;
    private final PowerBiMapper mapper;
    
    @GetMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@powerBiSecurityCheckerBean.checkIfUserHasPermission(#apiKey)")
    public ResponseEntity<List<EmissionActivityGhg>> emissionActivities(@PathVariable @NotBlank String apiKey) {
        var contextData = authenticationFacade.getAuthentication().getPrincipal();
        var enterpriseId = contextData.enterpriseId();
        var buildings = contextData.buildings();
        var activities = contextData.scope() == PowerBiScope.BUILDING
                         ? emissionActivityService.findByEnterpriseAndBuildingsFetchRecords(enterpriseId, buildings)
                         : emissionActivityService.findByEnterpriseIdFetchRecords(enterpriseId);
        var results = emissionActivityService
                .calculationActivitiesTotalGhg(activities)
                .stream()
                .peek(e -> e.setTotalEmission(
                        e.getRecords()
                         .stream()
                         .map(EmissionActivityRecordEntity::getGhg)
                         .filter(Objects::nonNull)
                         .reduce(BigDecimal.ZERO, BigDecimal::add)))
                .map(e -> this.mapper
                        .toEmissionActivityGhg(e)
                        .toBuilder()
                        .ghg(e.getTotalEmission().setScale(0, RoundingMode.DOWN))
                        .build())
                .toList();
        return ResponseEntity.ok(results);
    }
}
