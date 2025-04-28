import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';
import {Observable} from 'rxjs';
import {AppRoutingConstants} from '../app-routing.constant';
import {ChemicalDensityCriteria} from '../modules/admin/components/chemical-density/chemical-density.component';
import {ChemicalDensityDTO} from '@models/shared-models';
import {UUID} from '../../types/uuid';

@Injectable({
  providedIn: 'root'
})
export class ChemicalDensityService {
  constructor(private readonly httpClient: HttpClient) {}

  public getChemicalDensity(
    criteria: SearchCriteriaDto<ChemicalDensityCriteria>
  ): Observable<SearchResultDto<ChemicalDensityDTO>> {
    return this.httpClient.post<SearchResultDto<ChemicalDensityDTO>>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/chemical-density/search`,
      criteria
    );
  }

  public getById(chemicalDensityId: string): Observable<ChemicalDensityDTO> {
    return this.httpClient.get<ChemicalDensityDTO>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/chemical-density/${chemicalDensityId}`
    );
  }
  public get createOrUpdateURL(): string {
    return `${AppRoutingConstants.ENTERPRISE_API_URL}/chemical-density`;
  }

  public deleteChemicalDensity(chemicalIds: UUID[]): Observable<void> {
    return this.httpClient.delete<void>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/chemical-density`,
      {
        body: chemicalIds
      }
    );
  }
}
