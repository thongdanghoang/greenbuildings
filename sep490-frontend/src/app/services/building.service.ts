import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {SearchCriteriaDTOSearchTenantCriteria} from '@generated/models/search-criteria-dto-search-tenant-criteria';
import {TenantView} from '@generated/models/tenant-view';
import {Building, BuildingDetails, OverviewBuildingDTO} from '@models/enterprise';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';
import {Observable, map} from 'rxjs';
import {UUID} from '../../types/uuid';
import {AppRoutingConstants} from '../app-routing.constant';

@Injectable({
  providedIn: 'root'
})
export class BuildingService {
  private readonly BUILDINGS_URL = `${AppRoutingConstants.ENTERPRISE_API_URL}/${AppRoutingConstants.BUILDING_PATH}`;

  constructor(private readonly httpClient: HttpClient) {}

  get createBuildingUrl(): string {
    return `${this.BUILDINGS_URL}`;
  }

  get generateReportUrl(): string {
    return `${this.BUILDINGS_URL}/generate-report`;
  }

  getBuildingDetails(id: UUID): Observable<BuildingDetails> {
    return this.httpClient.get<BuildingDetails>(`${this.BUILDINGS_URL}/${id}`);
  }

  public deleteBuildingById(id: UUID): Observable<void> {
    return this.httpClient.delete<void>(`${this.BUILDINGS_URL}/${id}`);
  }

  searchBuildings(searchCriteria: SearchCriteriaDto<void>): Observable<SearchResultDto<Building>> {
    return this.httpClient.post<SearchResultDto<Building>>(`${this.BUILDINGS_URL}/search`, searchCriteria).pipe(
      map(response => {
        return {
          ...response,
          results: response.results.sort((a, b) => {
            const aHasSubscription = a.subscriptionDTO ? 1 : 0;
            const bHasSubscription = b.subscriptionDTO ? 1 : 0;
            return bHasSubscription - aHasSubscription;
          })
        };
      })
    );
  }

  searchUserByBuilding(
    searchCriteria: SearchCriteriaDTOSearchTenantCriteria,
    buildingId: UUID
  ): Observable<SearchResultDto<TenantView>> {
    return this.httpClient.post<SearchResultDto<TenantView>>(
      `${this.BUILDINGS_URL}/${buildingId}/tenants/search`,
      searchCriteria
    );
  }

  getBuildingOverview(id: UUID): Observable<OverviewBuildingDTO> {
    return this.httpClient.get<OverviewBuildingDTO>(`${this.BUILDINGS_URL}/${id}/overview`);
  }

  submitReport(data: any): Observable<any> {
    return this.httpClient.post<any>(this.generateReportUrl, data, {
      headers: new HttpHeaders({
        // responseType: 'blob',
        contentType: 'application/json',
        accept: 'application/octet-stream'
      }),
      responseType: 'blob' as 'json',
      observe: 'response'
    });
  }
}
