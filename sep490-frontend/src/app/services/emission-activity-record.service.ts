import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {EmissionActivityRecordCriteria} from '@models/emission-activity-record';
import {EmissionActivityRecord} from '@models/enterprise';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';
import {Observable} from 'rxjs';
import {UUID} from '../../types/uuid';
import {AppRoutingConstants} from '../app-routing.constant';

@Injectable({
  providedIn: 'root'
})
export class EmissionActivityRecordService {
  public readonly PATH: string = 'emission-activity-record';
  public readonly URL: string = `${AppRoutingConstants.ENTERPRISE_API_URL}/${this.PATH}`;

  public readonly newRecordURL: string = `${AppRoutingConstants.ENTERPRISE_API_URL}/${this.PATH}`;

  constructor(private readonly http: HttpClient) {}

  search(
    criteria: SearchCriteriaDto<EmissionActivityRecordCriteria>
  ): Observable<SearchResultDto<EmissionActivityRecord>> {
    return this.http.post<SearchResultDto<EmissionActivityRecord>>(`${this.URL}/search`, criteria);
  }

  public submitRecord(formData: FormData): Observable<void> {
    return this.http.post<void>(this.newRecordURL, formData);
  }

  public deleteRecords(ids: UUID[]): Observable<void> {
    return this.http.delete<void>(`${AppRoutingConstants.ENTERPRISE_API_URL}/${this.PATH}`, {
      body: ids
    });
  }

  public deleteRecordFile(recordId: UUID, fileId: UUID): Observable<void> {
    return this.http.delete<void>(`${this.URL}/${recordId}/file/${fileId}`);
  }

  getFile(recordId: string, fileId: string): Observable<Blob> {
    return this.http.get(`${this.URL}/${recordId}/file/${fileId}`, {
      responseType: 'blob'
    });
  }
}
