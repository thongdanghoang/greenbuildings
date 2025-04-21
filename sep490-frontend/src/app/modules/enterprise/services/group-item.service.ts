import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {UUID} from '../../../../types/uuid';
import {AppRoutingConstants} from '../../../app-routing.constant';
import {GroupItem} from '../models/enterprise.dto';
import {
  SearchCriteriaDto,
  SearchResultDto
} from '../../shared/models/base-models';

export interface NewGroupItemDTO {
  id: UUID;
  version: number;
  name: string;
  description: string;
  buildingGroupId: UUID;
}

@Injectable({
  providedIn: 'root'
})
export class GroupItemService {
  private readonly baseUrl = `${AppRoutingConstants.ENTERPRISE_API_URL}/group-items`;

  constructor(private readonly http: HttpClient) {}

  getAll(): Observable<GroupItem[]> {
    return this.http.get<GroupItem[]>(this.baseUrl);
  }

  getById(id: UUID): Observable<GroupItem> {
    return this.http.get<GroupItem>(`${this.baseUrl}/${id}`);
  }

  getByBuildingGroupId(buildingGroupId: UUID): Observable<GroupItem[]> {
    return this.http.get<GroupItem[]>(
      `${this.baseUrl}/building-group/${buildingGroupId}`
    );
  }

  create(groupItem: GroupItem): Observable<GroupItem> {
    return this.http.post<GroupItem>(this.baseUrl, groupItem);
  }

  get getCreateNewGroupItemUrl(): string {
    return `${this.baseUrl}`;
  }

  update(id: UUID, groupItem: GroupItem): Observable<GroupItem> {
    return this.http.put<GroupItem>(`${this.baseUrl}/${id}`, groupItem);
  }

  delete(id: UUID): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  search(
    criteria: SearchCriteriaDto<void>
  ): Observable<SearchResultDto<GroupItem>> {
    return this.http.post<SearchResultDto<GroupItem>>(
      `${this.baseUrl}/search`,
      criteria
    );
  }
}
