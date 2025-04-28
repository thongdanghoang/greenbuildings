import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {AppRoutingConstants} from '../app-routing.constant';
import {EmissionSourceDTO} from '../models/shared-models';

@Injectable({
  providedIn: 'root'
})
export class EnergyConversionService {
  private readonly ENERGY_CONVERSION_ENDPOINT: string = 'energy-conversion';

  constructor(private readonly httpClient: HttpClient) {}

  public findAll(): Observable<EmissionSourceDTO[]> {
    return this.httpClient.get<EmissionSourceDTO[]>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/${this.ENERGY_CONVERSION_ENDPOINT}`
    );
  }
}
