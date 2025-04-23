import {Injectable} from '@angular/core';
import {AppRoutingConstants} from '../../../app-routing.constant';
import {Observable} from 'rxjs';
import {InvitationDTO} from '../models/enterprise.dto';
import {HttpClient} from '@angular/common/http';
@Injectable({
  providedIn: 'root'
})
export class InvitationService {
  private readonly baseUrl = `${AppRoutingConstants.ENTERPRISE_API_URL}/invitation`;

  constructor(private readonly http: HttpClient) {}

  public findAllPendingByEmail(): Observable<InvitationDTO[]> {
    return this.http.get<InvitationDTO[]>(`${this.baseUrl}/find-by-email`);
  }
}
