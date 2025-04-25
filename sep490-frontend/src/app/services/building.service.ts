import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable, map} from 'rxjs';
import {UUID} from '../../types/uuid';
import {AppRoutingConstants} from '../app-routing.constant';
import {
  Building,
  BuildingDetails,
  OverviewBuildingDTO,
  UserByBuilding
} from '../modules/enterprise/models/enterprise.dto';
import {
  SearchCriteriaDto,
  SearchResultDto
} from '../modules/shared/models/base-models';

@Injectable({
  providedIn: 'root'
})
export class BuildingService {
  constructor(private readonly httpClient: HttpClient) {}

  get createBuildingUrl(): string {
    return `${AppRoutingConstants.ENTERPRISE_API_URL}/${AppRoutingConstants.BUILDING_PATH}`;
  }

  get generateReportUrl(): string {
    return `${AppRoutingConstants.ENTERPRISE_API_URL}/${AppRoutingConstants.BUILDING_PATH}/generate-report`;
  }

  getBuildingDetails(id: UUID): Observable<BuildingDetails> {
    return this.httpClient.get<BuildingDetails>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/${AppRoutingConstants.BUILDING_PATH}/${id}`
    );
  }

  public deleteBuildingById(id: UUID): Observable<void> {
    return this.httpClient.delete<void>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/${AppRoutingConstants.BUILDING_PATH}/${id}`
    );
  }

  searchBuildings(
    searchCriteria: SearchCriteriaDto<void>
  ): Observable<SearchResultDto<Building>> {
    return this.httpClient
      .post<
        SearchResultDto<Building>
      >(`${AppRoutingConstants.ENTERPRISE_API_URL}/${AppRoutingConstants.BUILDING_PATH}/search`, searchCriteria)
      .pipe(
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
    searchCriteria: SearchCriteriaDto<void>,
    buildingId: UUID
  ): Observable<SearchResultDto<UserByBuilding>> {
    return this.httpClient.post<SearchResultDto<UserByBuilding>>(
      `${AppRoutingConstants.IDP_API_URL}/enterprise-user/search/${buildingId}`,
      searchCriteria
    );
  }

  getBuildingOverview(id: UUID): Observable<OverviewBuildingDTO> {
    return this.httpClient.get<OverviewBuildingDTO>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/${AppRoutingConstants.BUILDING_PATH}/${id}/overview`
    );
  }
}
