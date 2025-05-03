package greenbuildings.enterprise.entities;

import commons.springfw.impl.entities.AbstractAuditableEntity;
import greenbuildings.enterprise.enums.ImportExcelType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "excel_import_files")
@Getter
@Setter
@NoArgsConstructor
public class ExcelImportFileEntity extends AbstractAuditableEntity {
    
    @Column(name = "file_name")
    private String fileName;
    
    @Column(name = "content_type")
    private String contentType;
    
    @Column(name = "file_size")
    private Long fileSize;
    
    @Column(name = "minio_path")
    private String minioPath;
    
    @Column(name = "upload_date")
    private LocalDateTime uploadDate;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type")
    private ImportExcelType type;

}