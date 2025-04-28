import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';
import {Observable} from 'rxjs';
import {AppRoutingConstants} from '../app-routing.constant';
import {EmissionFactorCriteria} from '../modules/admin/components/emission-factor/emission-factor.component';
import {EmissionFactorDTO} from '@models/shared-models';

@Injectable({
  providedIn: 'root'
})
export class EmissionFactorService {
  constructor(private readonly httpClient: HttpClient) {}

  public getEmissionFactor(
    criteria: SearchCriteriaDto<EmissionFactorCriteria>
  ): Observable<SearchResultDto<EmissionFactorDTO>> {
    return this.httpClient.post<SearchResultDto<EmissionFactorDTO>>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/emission-factor/search`,
      criteria
    );
  }

  public deleteEmissionFactor(factorId: string): Observable<void> {
    return this.httpClient.delete<void>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/emission-factor/${factorId}`
    );
  }

  public get createOrUpdateURL(): string {
    return `${AppRoutingConstants.ENTERPRISE_API_URL}/emission-factor`;
  }

  public getEmissionFactorById(
    emssionFactorId: string
  ): Observable<EmissionFactorDTO> {
    return this.httpClient.get<EmissionFactorDTO>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/emission-factor/${emssionFactorId}`
    );
  }
}
