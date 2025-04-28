import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {UUID} from '../../types/uuid';
import {AppRoutingConstants} from '../app-routing.constant';
import {
  PowerBiAuthority,
  PowerBiAuthorityGenerateResponse,
  PowerBiBuilding
} from '@models/power-bi-access-token';

@Injectable({
  providedIn: 'root'
})
export class PowerBiAccessTokenService {
  public readonly POWER_BI_AUTHORITY_URL: string = `${AppRoutingConstants.IDP_API_URL}/auth/power-bi`;
  public readonly ENTERPRISE_BUILDINGS_URL: string = `${AppRoutingConstants.ENTERPRISE_API_URL}/buildings`;

  constructor(private readonly httpClient: HttpClient) {}

  getSelectableBuildings(): Observable<PowerBiBuilding[]> {
    return this.httpClient.get<PowerBiBuilding[]>(
      `${this.ENTERPRISE_BUILDINGS_URL}/selectable`
    );
  }

  getMyAccessToken(): Observable<PowerBiAuthority[]> {
    return this.httpClient.get<PowerBiAuthority[]>(
      `${this.POWER_BI_AUTHORITY_URL}`
    );
  }

  findById(id: UUID): Observable<PowerBiAuthority> {
    return this.httpClient.get<PowerBiAuthority>(
      `${this.POWER_BI_AUTHORITY_URL}/${id}`
    );
  }

  regenerateAccessToken(
    id: UUID,
    expirationTime: Date
  ): Observable<PowerBiAuthorityGenerateResponse> {
    return this.httpClient.post<PowerBiAuthorityGenerateResponse>(
      `${this.POWER_BI_AUTHORITY_URL}/${id}/regenerate`,
      {expirationTime}
    );
  }

  deleteAccessToken(id: UUID): Observable<void> {
    return this.httpClient.delete<void>(`${this.POWER_BI_AUTHORITY_URL}/${id}`);
  }
}
