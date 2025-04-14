import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {UUID} from '../../../../types/uuid';
import {AppRoutingConstants} from '../../../app-routing.constant';
import {Tenant} from '../models/enterprise.dto';
import {
  SearchCriteriaDto,
  SearchResultDto
} from '../../shared/models/base-models';

@Injectable({
  providedIn: 'root'
})
export class TenantService {
  private readonly baseUrl = `${AppRoutingConstants.ENTERPRISE_API_URL}/tenants`;

  constructor(private readonly http: HttpClient) {}

  getAll(): Observable<Tenant[]> {
    return this.http.get<Tenant[]>(this.baseUrl);
  }

  getById(id: UUID): Observable<Tenant> {
    return this.http.get<Tenant>(`${this.baseUrl}/${id}`);
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

  search(
    criteria: SearchCriteriaDto<void>
  ): Observable<SearchResultDto<Tenant>> {
    return this.http.post<SearchResultDto<Tenant>>(
      `${this.baseUrl}/search`,
      criteria
    );
  }
}
