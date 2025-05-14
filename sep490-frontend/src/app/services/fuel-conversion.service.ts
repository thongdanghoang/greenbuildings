import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {EnergyConversionDTO} from '@models/shared-models';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';
import {Observable} from 'rxjs';
import {UUID} from '../../types/uuid';
import {AppRoutingConstants} from '../app-routing.constant';
import {FuelConversionCriteria} from '../modules/admin/components/fuel-conversion/fuel-conversion.component';
import {ExcelImportDTO} from '@models/enterprise';

@Injectable({
  providedIn: 'root'
})
export class FuelConversionService {
  constructor(private readonly httpClient: HttpClient) {}

  public getFuelConversion(
    criteria: SearchCriteriaDto<FuelConversionCriteria>
  ): Observable<SearchResultDto<EnergyConversionDTO>> {
    return this.httpClient.post<SearchResultDto<EnergyConversionDTO>>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/energy-conversion/search`,
      criteria
    );
  }

  public getFuelConversionByFactorId(factorId: UUID): Observable<EnergyConversionDTO> {
    return this.httpClient.get<EnergyConversionDTO>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/energy-conversion/find-by-factor/${factorId}`
    );
  }

  public getFuelConversionById(fuelId: string): Observable<EnergyConversionDTO> {
    return this.httpClient.get<EnergyConversionDTO>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/energy-conversion/${fuelId}`
    );
  }
  public get createOrUpdateFuelConversionURL(): string {
    return `${AppRoutingConstants.ENTERPRISE_API_URL}/energy-conversion`;
  }

  public importFuelExcel(file: File): Observable<void> {
    const formData = new FormData();
    formData.append('file', file);

    return this.httpClient.post<void>(`${AppRoutingConstants.ENTERPRISE_API_URL}/energy-conversion/excel`, formData);
  }

  getFile(): Observable<Blob> {
    return this.httpClient.get(`${AppRoutingConstants.ENTERPRISE_API_URL}/energy-conversion/excel`, {
      responseType: 'blob'
    });
  }

  public getExcelImportDTO(): Observable<ExcelImportDTO> {
    return this.httpClient.get<ExcelImportDTO>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/energy-conversion/excel-import`
    );
  }

  public uploadExcelToMinio(file: File): Observable<void> {
    const formData = new FormData();
    formData.append('file', file);

    return this.httpClient.post<void>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/energy-conversion/upload-excel`,
      formData
    );
  }
}
