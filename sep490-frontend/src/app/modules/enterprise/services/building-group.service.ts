import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {UUID} from '../../../../types/uuid';
import {AppRoutingConstants} from '../../../app-routing.constant';
import {BuildingGroup} from '../models/enterprise.dto';
import {
  SearchCriteriaDto,
  SearchResultDto
} from '../../shared/models/base-models';

export interface BuildingGroupCriteria {
  buildingId: UUID;
  name?: string;
}

export interface CreateBuildingGroupDTO {
  name: string;
  description: string;
  buildingId: UUID;
}

export interface InviteTenantToBuildingGroup {
  buildingId: UUID;
  email: string;
  buildingGroupIds: UUID[];
}

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

  getAll(): Observable<BuildingGroup[]> {
    return this.http.get<BuildingGroup[]>(this.baseUrl);
  }

  getById(id: UUID): Observable<BuildingGroup> {
    return this.http.get<BuildingGroup>(`${this.baseUrl}/${id}`);
  }

  getByBuildingId(buildingId: UUID): Observable<BuildingGroup[]> {
    return this.http.get<BuildingGroup[]>(
      `${this.baseUrl}/building/${buildingId}`
    );
  }

  getByTenantId(tenantId: UUID): Observable<BuildingGroup[]> {
    return this.http.get<BuildingGroup[]>(`${this.baseUrl}/tenant/${tenantId}`);
  }

  create(buildingGroup: BuildingGroup): Observable<BuildingGroup> {
    return this.http.post<BuildingGroup>(this.baseUrl, buildingGroup);
  }

  update(id: UUID, buildingGroup: BuildingGroup): Observable<BuildingGroup> {
    return this.http.put<BuildingGroup>(`${this.baseUrl}/${id}`, buildingGroup);
  }

  delete(id: UUID): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  search(
    criteria: SearchCriteriaDto<BuildingGroupCriteria>
  ): Observable<SearchResultDto<BuildingGroup>> {
    return this.http.post<SearchResultDto<BuildingGroup>>(
      `${this.baseUrl}/search`,
      criteria
    );
  }

  public fetchGroupOfBuilding(
    criteria: SearchCriteriaDto<BuildingGroupCriteria>
  ): Observable<SearchResultDto<BuildingGroup>> {
    return this.http.post<SearchResultDto<BuildingGroup>>(
      `${this.baseUrl}/building`,
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
