import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {
  SearchCriteriaDto,
  SearchResultDto
} from '../../shared/models/base-models';
import {Observable} from 'rxjs';
import {
  EmissionSource,
  FuelConversion
} from '../../enterprise/models/enterprise.dto';
import {AppRoutingConstants} from '../../../app-routing.constant';
import {FuelConversionCriteria} from '../components/fuel-conversion/fuel-conversion.component';

@Injectable()
export class FuelConversionService {
  constructor(private readonly httpClient: HttpClient) {}

  public getFuelConversion(
    criteria: SearchCriteriaDto<FuelConversionCriteria>
  ): Observable<SearchResultDto<FuelConversion>> {
    return this.httpClient.post<SearchResultDto<FuelConversion>>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/fuel/search`,
      criteria
    );
  }

  public getFuelConversionById(fuelId: string): Observable<EmissionSource> {
    return this.httpClient.get<FuelConversion>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/fuel/${fuelId}`
    );
  }
  public get createOrUpdateFuelConversionURL(): string {
    return `${AppRoutingConstants.ENTERPRISE_API_URL}/fuel`;
  }
}
