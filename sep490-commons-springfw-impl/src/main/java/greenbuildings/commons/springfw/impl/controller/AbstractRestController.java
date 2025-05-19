package greenbuildings.commons.springfw.impl.controller;

import jakarta.validation.constraints.NotNull;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class AbstractRestController {
    
    // TODO: wrap file, name, type ... into WrapperClass
    protected ResponseEntity<ByteArrayResource> generateFileDownloadResponse(@NotNull ByteArrayResource file) {
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.xlsx");
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.PRAGMA, "no-cache");
        headers.add(HttpHeaders.EXPIRES, "0");
        
        return ResponseEntity.ok()
                             .headers(headers)
                             .contentLength(file.contentLength())
                             .contentType(MediaType.APPLICATION_OCTET_STREAM)
                             .body(file);
        
    }
}
