package greenbuildings.enterprise.rest;

import greenbuildings.commons.api.security.UserRole;
import greenbuildings.enterprise.services.MinioService;
import greenbuildings.enterprise.views.file_upload.BusinessLicenseUploadView;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/storage")
@RequiredArgsConstructor
@RolesAllowed({UserRole.RoleNameConstant.BASIC_USER,
               UserRole.RoleNameConstant.TENANT,
               UserRole.RoleNameConstant.ENTERPRISE_OWNER,
               UserRole.RoleNameConstant.SYSTEM_ADMIN})
public class FileUploadResource {
    
    private final MinioService minioService;
    
    private static final String BUSINESS_LICENSE_BUCKET = "business-license";
    
    
    @GetMapping("/business-license/{name}")
    public ResponseEntity<byte[]> downloadBusinessLicense(@PathVariable String name) {
        try (var inputStream = minioService.getFile(BUSINESS_LICENSE_BUCKET, name)) {
            return ResponseEntity.ok().body(inputStream.readAllBytes());
            // .contentType(MediaType.IMAGE_JPEG)
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/business-license")
    public ResponseEntity<BusinessLicenseUploadView> uploadImage(@RequestParam("license") MultipartFile file) throws Exception {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BusinessLicenseUploadView.builder().path(minioService.upload(file, BUSINESS_LICENSE_BUCKET)).build());
    }
}
