import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {AppRoutingConstants} from '../app-routing.constant';
import {EmissionFactorDTO} from '../models/shared-models';

@Injectable({
  providedIn: 'root'
})
export class EmissionFactorService {
  private readonly EMISSION_FACTOR_ENDPOINT: string = 'emission-factor';

  constructor(private readonly httpClient: HttpClient) {}

  public findAll(): Observable<EmissionFactorDTO[]> {
    return this.httpClient.get<EmissionFactorDTO[]>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/${this.EMISSION_FACTOR_ENDPOINT}`
    );
  }
}
