import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {ActivityCriteria} from '@generated/models/activity-criteria';
import {DateRangeView} from '@generated/models/date-range-view';
import {EmissionActivityView} from '@generated/models/emission-activity-view';
import {ActivitySearchCriteria} from '@models/emission-activity';
import {EmissionActivityDetails, EmissionActivityTableView} from '@models/enterprise';
import {SearchCriteriaDto, SearchResultDto, SelectableItem} from '@shared/models/base-models';
import {Observable} from 'rxjs';
import {UUID} from '../../types/uuid';
import {AppRoutingConstants} from '../app-routing.constant';

@Injectable({
  providedIn: 'root'
})
export class EmissionActivityService {
  public readonly PATH: string = 'emission-activity';
  public readonly URL: string = `${AppRoutingConstants.ENTERPRISE_API_URL}/${this.PATH}`;

  constructor(private readonly httpClient: HttpClient) {}

  public fetchActivityOfBuilding(
    criteria: SearchCriteriaDto<ActivitySearchCriteria>
  ): Observable<SearchResultDto<EmissionActivityTableView>> {
    return this.httpClient.post<SearchResultDto<EmissionActivityTableView>>(`${this.URL}/building`, criteria);
  }

  public search(criteria: SearchCriteriaDto<ActivityCriteria>): Observable<SearchResultDto<EmissionActivityView>> {
    return this.httpClient.post<SearchResultDto<EmissionActivityView>>(`${this.URL}/search`, criteria);
  }

  public getSelectableBuildings(): Observable<SelectableItem<UUID>[]> {
    return this.httpClient.get<SelectableItem<UUID>[]>(`${this.URL}/selectable-buildings`);
  }

  public getSelectableFactors(language: string): Observable<SelectableItem<UUID>[]> {
    return this.httpClient.get<SelectableItem<UUID>[]>(`${this.URL}/selectable-factors?language=${language}`);
  }

  public getRecordedDateRanges(activityId: UUID, excludeRecordId?: UUID, assetId?: UUID): Observable<DateRangeView[]> {
    let url = `${this.URL}/${activityId}/recorded-date-ranges`;
    if (excludeRecordId) {
      url += `?excludeRecordId=${excludeRecordId}`;
    }
    if (assetId) {
      url += `${excludeRecordId ? '&' : '?'}assetId=${assetId}`;
    }
    return this.httpClient.get<DateRangeView[]>(url);
  }

  public getCreateNewActivityURL(): string {
    return `${this.URL}`;
  }

  public deleteActivities(ids: UUID[]): Observable<void> {
    return this.httpClient.delete<void>(`${this.URL}`, {
      body: ids
    });
  }

  public getActivityDetails(activityId: UUID): Observable<EmissionActivityDetails> {
    return this.httpClient.get<EmissionActivityDetails>(`${this.URL}/${activityId}`);
  }
}
