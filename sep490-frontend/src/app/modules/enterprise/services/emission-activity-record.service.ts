import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {UUID} from '../../../../types/uuid';
import {AppRoutingConstants} from '../../../app-routing.constant';
import {
  SearchCriteriaDto,
  SearchResultDto
} from '../../shared/models/base-models';
import {EmissionActivityRecord} from '../models/enterprise.dto';

export interface EmissionActivityRecordCriteria {
  emissionActivityId: UUID;
}

@Injectable({
  providedIn: 'root'
})
export class EmissionActivityRecordService {
  public readonly EMISSION_ACTIVITY_RECORD_PATH: string =
    'emission-activity-record';

  public readonly newRecordURL: string = `${AppRoutingConstants.ENTERPRISE_API_URL}/${this.EMISSION_ACTIVITY_RECORD_PATH}`;

  constructor(private readonly http: HttpClient) {}

  search(
    criteria: SearchCriteriaDto<EmissionActivityRecordCriteria>
  ): Observable<SearchResultDto<EmissionActivityRecord>> {
    return this.http.post<SearchResultDto<EmissionActivityRecord>>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/${this.EMISSION_ACTIVITY_RECORD_PATH}/search`,
      criteria
    );
  }

  public deleteRecords(ids: UUID[]): Observable<void> {
    return this.http.delete<void>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/${this.EMISSION_ACTIVITY_RECORD_PATH}`,
      {
        body: ids
      }
    );
  }

  public deleteRecordFile(recordId: UUID, fileId: UUID): Observable<void> {
    return this.http.delete<void>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/${this.EMISSION_ACTIVITY_RECORD_PATH}/${recordId}/file/${fileId}`
    );
  }

  getFileUrl(recordId: string, fileId: string): Observable<any> {
    return this.http.get<any>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/${this.EMISSION_ACTIVITY_RECORD_PATH}/${recordId}/file/${fileId}/url`
    );
  }
}
