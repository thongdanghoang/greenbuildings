import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {EnterpriseDetailDTO} from '@generated/models/enterprise-detail-dto';
import {AppRoutingConstants} from '../app-routing.constant';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';
import {EnterpriseDTO} from '@generated/models/enterprise-dto';
import {EnterpriseSearchCriteria} from '@generated/models/enterprise-search-criteria';
import {Observable} from 'rxjs';
import {UUID} from 'src/types/uuid';

@Injectable({
  providedIn: 'root'
})
export class EnterpriseService {
  private readonly baseUrl = `${AppRoutingConstants.ENTERPRISE_API_URL}/enterprise`;
  constructor(private readonly http: HttpClient) {}

  search(criteria: SearchCriteriaDto<EnterpriseSearchCriteria>): Observable<SearchResultDto<EnterpriseDTO>> {
    return this.http.post<SearchResultDto<EnterpriseDTO>>(`${this.baseUrl}/search`, criteria);
  }

  getEnterpriseDetailsByBuildingId(buildingId: UUID): Observable<EnterpriseDetailDTO> {
    return this.http.get<EnterpriseDetailDTO>(`${this.baseUrl}/search-by-building/${buildingId}`);
  }
}
