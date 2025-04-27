import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AppRoutingConstants} from '../../../app-routing.constant';

export interface EnterpriseDetailDTO {
  id: string;
  version: number;
  name: string;
  email: string;
  hotline: string;
}

@Injectable({
  providedIn: 'root'
})
export class EnterpriseService {
  private readonly apiUrl = `${AppRoutingConstants.ENTERPRISE_API_URL}/enterprise`;

  constructor(private readonly http: HttpClient) {}

  /**
   * Get enterprise profile details
   */
  getEnterpriseProfile(): Observable<EnterpriseDetailDTO> {
    return this.http.get<EnterpriseDetailDTO>(`${this.apiUrl}/profile`);
  }

  /**
   * Update enterprise profile details
   * @param profile The updated enterprise profile
   */
  updateEnterpriseProfile(profile: EnterpriseDetailDTO): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/profile`, profile);
  }
}
