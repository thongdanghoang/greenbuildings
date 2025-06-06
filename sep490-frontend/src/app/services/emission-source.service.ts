import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {AppRoutingConstants} from '../app-routing.constant';
import {EmissionSource, ExcelImportDTO} from '@models/enterprise';
import {EmissionSourceCriteria} from '../modules/admin/components/emission-source/emission-source.component';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';

@Injectable({
  providedIn: 'root'
})
export class EmissionSourceService {
  constructor(private readonly httpClient: HttpClient) {}

  public getEmissionSource(
    criteria: SearchCriteriaDto<EmissionSourceCriteria>
  ): Observable<SearchResultDto<EmissionSource>> {
    return this.httpClient.post<SearchResultDto<EmissionSource>>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/emission-source/search`,
      criteria
    );
  }

  public getEmissionSourceById(emssionSourceId: string): Observable<EmissionSource> {
    return this.httpClient.get<EmissionSource>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/emission-source/${emssionSourceId}`
    );
  }
  public get createOrUpdateEmissionSourceURL(): string {
    return `${AppRoutingConstants.ENTERPRISE_API_URL}/emission-source`;
  }

  public importEmissionSources(file: File): Observable<void> {
    const formData = new FormData();
    formData.append('file', file);

    return this.httpClient.post<void>(`${AppRoutingConstants.ENTERPRISE_API_URL}/emission-source/excel`, formData);
  }

  getFile(): Observable<Blob> {
    return this.httpClient.get(`${AppRoutingConstants.ENTERPRISE_API_URL}/emission-source/excel`, {
      responseType: 'blob'
    });
  }

  public getExcelImportDTO(): Observable<ExcelImportDTO> {
    return this.httpClient.get<ExcelImportDTO>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/emission-source/excel-import`
    );
  }

  public uploadExcelToMinio(file: File): Observable<void> {
    const formData = new FormData();
    formData.append('file', file);

    return this.httpClient.post<void>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/emission-source/upload-excel`,
      formData
    );
  }
}
