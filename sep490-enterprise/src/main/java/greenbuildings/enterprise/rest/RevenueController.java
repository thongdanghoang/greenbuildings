package greenbuildings.enterprise.rest;

import greenbuildings.enterprise.dtos.RevenueReportDTO;
import greenbuildings.enterprise.services.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/revenue")
@RequiredArgsConstructor
public class RevenueController {
    
    private final RevenueService revenueService;
    
    @GetMapping("/generate-report")
    public ResponseEntity<RevenueReportDTO> generateReport() {
        return ResponseEntity.ok(revenueService.generateReport());
    }
    
    
    
}
