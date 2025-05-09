package greenbuildings.enterprise.services;

import greenbuildings.commons.api.events.PowerBiAccessTokenAuthResult;
import greenbuildings.enterprise.dtos.DownloadReportDTO;

public interface ReportService {
    
    Object generateReport(PowerBiAccessTokenAuthResult contextData);
    
    byte[] generateReport(DownloadReportDTO downloadReport);
}
