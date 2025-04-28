import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class FuelService {
  private readonly FUEL_ENDPOINT: string = 'fuel';

  constructor(private readonly httpClient: HttpClient) {}
}
