CREATE TABLE record_files (
    id UUID PRIMARY KEY,
    version INTEGER NOT NULL DEFAULT 0,
    created_date       TIMESTAMP,
    created_by         VARCHAR(255),
    last_modified_date TIMESTAMP,
    last_modified_by   VARCHAR(255),
    file_name VARCHAR(255) NOT NULL,
    content_type VARCHAR(100),
    file_size BIGINT NOT NULL,
    minio_path VARCHAR(512) NOT NULL,
    upload_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE activity_data_record ADD COLUMN file_id UUID;

ALTER TABLE activity_data_record ADD CONSTRAINT fk_activity_data_record_file FOREIGN KEY (file_id) REFERENCES record_files(id);