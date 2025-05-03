package greenbuildings.enterprise.services;

import greenbuildings.commons.api.exceptions.TechnicalException;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Throwable.class)
@Slf4j
@RequiredArgsConstructor
public class MinioService {
    
    private final MinioClient minioClient;
    
    @Value("${minio.bucket.name}")
    private String bucketName;
    
    public String upload(MultipartFile file, String bucketName) throws Exception {
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
        var originalFilename = file.getOriginalFilename();
        var extension = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf("."));
        var fileName = UUID.randomUUID() + extension;
        minioClient.putObject(
                PutObjectArgs.builder()
                             .bucket(bucketName)
                             .object(fileName)
                             .stream(file.getInputStream(), file.getSize(), -1)
                             .contentType(file.getContentType())
                             .build()
                             );
        return fileName;
    }
    
    public InputStream getFile(String bucketName, String objectName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                                 .bucket(bucketName)
                                 .object(objectName)
                                 .build());
        } catch (Exception e) {
            throw new TechnicalException("Failed to get file", e);
        }
    }
    
    public String uploadFile(MultipartFile file, String recordId) {
        try {
            // Create bucket if it doesn't exist
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            
            // Generate unique file name
            String originalFilename = file.getOriginalFilename();
            String extension = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf("."));
            String fileName = UUID.randomUUID() + extension;
            
            // Create path in MinIO
            String minioPath = String.format("records/%s/%s", recordId, fileName);
            
            // Upload file
            minioClient.putObject(
                    PutObjectArgs.builder()
                                 .bucket(bucketName)
                                 .object(minioPath)
                                 .stream(file.getInputStream(), file.getSize(), -1)
                                 .contentType(file.getContentType())
                                 .build()
                                 );
            
            return minioPath;
        } catch (Exception e) {
            log.error("Error uploading file to MinIO", e);
            throw new TechnicalException("Failed to upload file", e);
        }
    }
    
    public InputStream getFile(String minioPath) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                                 .bucket(bucketName)
                                 .object(minioPath)
                                 .build());
        } catch (Exception e) {
            throw new TechnicalException("Failed to get file", e);
        }
    }
    
    public String generatePresignedUrl(String bucketName, String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                                             .method(Method.GET)
                                             .bucket(bucketName)
                                             .object(objectName)
                                             .expiry(60 * 10) // 10 minutes
                                             .build());
        } catch (Exception e) {
            log.error("Error generating presigned url for file: {}", objectName, e);
            throw new TechnicalException("Failed to generate presigned url", e);
        }
    }
    
    public void deleteFile(String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                                    .bucket(bucketName)
                                    .object(objectName)
                                    .build()
                                    );
        } catch (Exception e) {
            log.error("Error deleting file from MinIO", e);
            throw new TechnicalException("Failed to delete file", e);
        }
    }
} 