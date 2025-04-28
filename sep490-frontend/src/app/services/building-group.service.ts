import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {BuildingGroupCriteria} from '@models/building-group';
import {Observable} from 'rxjs';
import {UUID} from '../../types/uuid';
import {AppRoutingConstants} from '../app-routing.constant';
import {BuildingGroup} from '@models/enterprise';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';

@Injectable({
  providedIn: 'root'
})
export class BuildingGroupService {
  private readonly baseUrl = `${AppRoutingConstants.ENTERPRISE_API_URL}/building-groups`;

  constructor(private readonly http: HttpClient) {}

  get newBuildingGroupUrl(): string {
    return `${this.baseUrl}`;
  }

  get inviteTenantUrl(): string {
    return `${this.baseUrl}/invite`;
  }

  getAllWithBuilding(): Observable<BuildingGroup[]> {
    return this.http.get<BuildingGroup[]>(
      `${this.baseUrl}/search/with-buildings`
    );
  }

  getById(id: UUID): Observable<BuildingGroup> {
    return this.http.get<BuildingGroup>(`${this.baseUrl}/${id}`);
  }

  getByTenant(): Observable<BuildingGroup[]> {
    return this.http.get<BuildingGroup[]>(`${this.baseUrl}/tenant`);
  }

  create(buildingGroup: BuildingGroup): Observable<BuildingGroup> {
    return this.http.post<BuildingGroup>(this.baseUrl, buildingGroup);
  }

  search(
    criteria: SearchCriteriaDto<BuildingGroupCriteria>
  ): Observable<SearchResultDto<BuildingGroup>> {
    return this.http.post<SearchResultDto<BuildingGroup>>(
      `${this.baseUrl}/search`,
      criteria
    );
  }

  deleteGroups(ids: UUID[]): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}`, {
      body: ids
    });
  }

  getAvailableBuildingGroups(buildingId: UUID): Observable<BuildingGroup[]> {
    return this.http.get<BuildingGroup[]>(
      `${this.baseUrl}/building/${buildingId}/available`
    );
  }
}
