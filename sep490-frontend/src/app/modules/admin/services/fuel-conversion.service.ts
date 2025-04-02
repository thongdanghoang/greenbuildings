import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {
  SearchCriteriaDto,
  SearchResultDto
} from '../../shared/models/base-models';
import {Observable} from 'rxjs';
import {AppRoutingConstants} from '../../../app-routing.constant';
import {FuelConversionCriteria} from '../components/fuel-conversion/fuel-conversion.component';
import {EnergyConversionDTO} from '../../shared/models/shared-models';

@Injectable()
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

  public getFuelConversionById(
    fuelId: string
  ): Observable<EnergyConversionDTO> {
    return this.httpClient.get<EnergyConversionDTO>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/energy-conversion/${fuelId}`
    );
  }
  public get createOrUpdateFuelConversionURL(): string {
    return `${AppRoutingConstants.ENTERPRISE_API_URL}/energy-conversion`;
  }
}
