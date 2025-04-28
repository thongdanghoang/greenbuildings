import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {AppRoutingConstants} from '../app-routing.constant';
import {FuelDTO} from '../models/shared-models';

@Injectable({
  providedIn: 'root'
})
export class FuelService {
  private readonly FUEL_ENDPOINT: string = 'fuel';

  constructor(private readonly httpClient: HttpClient) {}

  public findAll(): Observable<FuelDTO[]> {
    return this.httpClient.get<FuelDTO[]>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/${this.FUEL_ENDPOINT}`
    );
  }
}
