package greenbuildings.enterprise.entities;

import greenbuildings.commons.springfw.impl.entities.AbstractAuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "record_files")
@Getter
@Setter
@NoArgsConstructor
public class RecordFileEntity extends AbstractAuditableEntity {
    
    @OneToOne(mappedBy = "file")
    private EmissionActivityRecordEntity record;
    
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
}