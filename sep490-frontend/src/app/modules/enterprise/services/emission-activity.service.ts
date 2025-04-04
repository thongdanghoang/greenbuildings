import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {
  EmissionActivity,
  EmissionActivityDetails
} from '../models/enterprise.dto';
import {AppRoutingConstants} from '../../../app-routing.constant';
import {UUID} from '../../../../types/uuid';
import {
  SearchCriteriaDto,
  SearchResultDto
} from '../../shared/models/base-models';

export interface ActivitySearchCriteria {
  buildingId: UUID;
  name?: string;
}

@Injectable()
export class EmissionActivityService {
  public readonly EMISSION_ACTIVITY_PATH: string = 'emission-activity';

  constructor(private readonly httpClient: HttpClient) {}

  public fetchActivityOfBuilding(
    criteria: SearchCriteriaDto<ActivitySearchCriteria>
  ): Observable<SearchResultDto<EmissionActivity>> {
    return this.httpClient.post<SearchResultDto<EmissionActivity>>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/${this.EMISSION_ACTIVITY_PATH}/building`,
      criteria
    );
  }

  public getCreateNewActivityURL(): string {
    return `${AppRoutingConstants.ENTERPRISE_API_URL}/${this.EMISSION_ACTIVITY_PATH}`;
  }

  public deleteActivities(ids: UUID[]): Observable<void> {
    return this.httpClient.delete<void>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/${this.EMISSION_ACTIVITY_PATH}`,
      {
        body: ids
      }
    );
  }

  public getActivityDetails(
    activityId: UUID
  ): Observable<EmissionActivityDetails> {
    return this.httpClient.get<EmissionActivityDetails>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/${this.EMISSION_ACTIVITY_PATH}/${activityId}`
    );
  }

  public getAllActivitiesByBuildingId(
    buildingId: UUID
  ): Observable<EmissionActivity[]> {
    return this.httpClient.get<EmissionActivity[]>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/${this.EMISSION_ACTIVITY_PATH}/building/${buildingId}`
    );
  }
}
