import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {AppRoutingConstants} from '../../../app-routing.constant';
import {EmissionSourceDTO} from '../models/shared-models';

@Injectable()
export class EmissionSourceService {
  private readonly EMISSION_SOURCE_ENDPOINT: string = 'emission-source';

  constructor(private readonly httpClient: HttpClient) {}

  public findAll(): Observable<EmissionSourceDTO[]> {
    return this.httpClient.get<EmissionSourceDTO[]>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/${this.EMISSION_SOURCE_ENDPOINT}`
    );
  }
}
