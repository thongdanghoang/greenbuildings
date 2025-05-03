import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {TenantDetailDTO} from '@generated/models/tenant-detail-dto';
import {Tenant} from '@models/enterprise';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';
import {Observable} from 'rxjs';
import {UUID} from '../../types/uuid';
import {AppRoutingConstants} from '../app-routing.constant';

export interface SearchTenantCriteria {
  email: string;
}

export interface TenantTableView {
  id: UUID;
  name: string;
  email: string;
  hotline: string;
  buildings: {
    id: UUID;
    name: string;
    buildingGroup: {
      id: UUID;
      name: string;
    };
  }[];
}

@Injectable({
  providedIn: 'root'
})
export class TenantService {
  private readonly baseUrl = `${AppRoutingConstants.ENTERPRISE_API_URL}/tenants`;

  constructor(private readonly http: HttpClient) {}

  getTenantDetail(id: UUID): Observable<TenantDetailDTO> {
    return this.http.get<TenantDetailDTO>(`${this.baseUrl}/${id}/detail`);
  }

  updateTenantDetail(id: UUID, tenantDetail: TenantDetailDTO): Observable<TenantDetailDTO> {
    return this.http.put<TenantDetailDTO>(`${this.baseUrl}/${id}/detail`, tenantDetail);
  }

  create(tenant: Tenant): Observable<Tenant> {
    return this.http.post<Tenant>(this.baseUrl, tenant);
  }

  update(id: UUID, tenant: Tenant): Observable<Tenant> {
    return this.http.put<Tenant>(`${this.baseUrl}/${id}`, tenant);
  }

  delete(id: UUID): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  search(criteria: SearchCriteriaDto<SearchTenantCriteria>): Observable<SearchResultDto<TenantTableView>> {
    return this.http.post<SearchResultDto<TenantTableView>>(`${this.baseUrl}/search`, criteria);
  }

  searchByAdmin(criteria: SearchCriteriaDto<SearchTenantCriteria>): Observable<SearchResultDto<TenantTableView>> {
    return this.http.post<SearchResultDto<TenantTableView>>(`${this.baseUrl}/admin-search`, criteria);
  }
}
