import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {UUID} from '../../../../types/uuid';
import {AppRoutingConstants} from '../../../app-routing.constant';
import {ActivityType} from '../models/enterprise.dto';

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
}
