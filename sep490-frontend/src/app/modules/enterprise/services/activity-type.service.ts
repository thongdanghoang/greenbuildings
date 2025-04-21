import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {UUID} from '../../../../types/uuid';
import {AppRoutingConstants} from '../../../app-routing.constant';
import {ActivityType} from '../models/enterprise.dto';
import {
  SearchCriteriaDto,
  SearchResultDto
} from '../../shared/models/base-models';

export interface ActivityTypeCriteria {
  tenantId: UUID;
  criteria?: string;
}

@Injectable({
  providedIn: 'root'
})
export class ActivityTypeService {
  private readonly baseUrl = `${AppRoutingConstants.ENTERPRISE_API_URL}/activity-types`;

  constructor(private readonly http: HttpClient) {}

  getAll(): Observable<ActivityType[]> {
    return this.http.get<ActivityType[]>(this.baseUrl);
  }

  getByEnterpriseId(enterpriseId: UUID): Observable<ActivityType[]> {
    return this.http.get<ActivityType[]>(
      `${this.baseUrl}?enterpriseId=${enterpriseId}`
    );
  }

  create(activityType: ActivityType): Observable<ActivityType> {
    return this.http.post<ActivityType>(this.baseUrl, activityType);
  }
  get createNewURL(): string {
    return `${this.baseUrl}/create`;
  }

  public getActivityType(
    criteria: SearchCriteriaDto<ActivityTypeCriteria>
  ): Observable<SearchResultDto<ActivityType>> {
    return this.http.post<SearchResultDto<ActivityType>>(
      `${this.baseUrl}/search`,
      criteria
    );
  }

  public getActivityTypeById(typeId: string): Observable<ActivityType> {
    return this.http.get<ActivityType>(`${this.baseUrl}/${typeId}`);
  }

  public deleteActivityType(userIds: UUID[]): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}`, {
      body: userIds
    });
  }
}
