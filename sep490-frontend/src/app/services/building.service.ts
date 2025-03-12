import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {UUID} from '../../types/uuid';
import {AppRoutingConstants} from '../app-routing.constant';
import {
  Building,
  BuildingDetails
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

  getBuildingDetails(id: UUID): Observable<BuildingDetails> {
    return this.httpClient.get<BuildingDetails>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/${AppRoutingConstants.BUILDING_PATH}/${id}`
    );
  }

  searchBuildings(
    searchCriteria: SearchCriteriaDto<void>
  ): Observable<SearchResultDto<Building>> {
    return this.httpClient.post<SearchResultDto<Building>>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/${AppRoutingConstants.BUILDING_PATH}/search`,
      searchCriteria
    );
  }
}
