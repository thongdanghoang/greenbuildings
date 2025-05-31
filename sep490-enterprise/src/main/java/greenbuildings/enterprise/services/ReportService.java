package greenbuildings.enterprise.services;

import greenbuildings.enterprise.dtos.DownloadReportDTO;

public interface ReportService {
    
    byte[] generateReport(DownloadReportDTO downloadReport);
    
}
