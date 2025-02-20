package commons.springfw.impl.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class ExcelUtils {
    
    private ExcelUtils() {
        // private constructor for util class
    }
    
    public static final String XLS_MIME_TYPE = "application/vnd.ms-excel";
    public static final String XLSX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    
    public static boolean validateExcel(MultipartFile file) {
        if (file.isEmpty()) {
            return false;
        }
        String contentType = file.getContentType();
        return XLS_MIME_TYPE.equals(contentType) || XLSX_MIME_TYPE.equals(contentType);
    }
    
    
}
