import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {EnergyConversionDTO} from '@models/shared-models';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';
import {Observable} from 'rxjs';
import {AppRoutingConstants} from '../app-routing.constant';
import {FuelConversionCriteria} from '../modules/admin/components/fuel-conversion/fuel-conversion.component';

@Injectable({
  providedIn: 'root'
})
export class FuelConversionService {
  constructor(private readonly httpClient: HttpClient) {}

  public getFuelConversion(
    criteria: SearchCriteriaDto<FuelConversionCriteria>
  ): Observable<SearchResultDto<EnergyConversionDTO>> {
    return this.httpClient.post<SearchResultDto<EnergyConversionDTO>>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/energy-conversion/search`,
      criteria
    );
  }

  public getFuelConversionById(fuelId: string): Observable<EnergyConversionDTO> {
    return this.httpClient.get<EnergyConversionDTO>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/energy-conversion/${fuelId}`
    );
  }
  public get createOrUpdateFuelConversionURL(): string {
    return `${AppRoutingConstants.ENTERPRISE_API_URL}/energy-conversion`;
  }
}
