import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {InvitationDTO} from '@models/enterprise';
import {InvitationResponse, InvitationSearchCriteria} from '@models/tenant';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';
import {Observable} from 'rxjs';
import {AppRoutingConstants} from '../app-routing.constant';

@Injectable({
  providedIn: 'root'
})
export class InvitationService {
  private readonly baseUrl = `${AppRoutingConstants.ENTERPRISE_API_URL}/invitation`;

  constructor(private readonly http: HttpClient) {}

  public findAllPendingByEmail(): Observable<InvitationDTO[]> {
    return this.http.get<InvitationDTO[]>(`${this.baseUrl}/find-by-email`);
  }

  public updateStatus(
    invitationResponse: InvitationResponse
  ): Observable<void> {
    return this.http.put<void>(
      `${this.baseUrl}/update-status`,
      invitationResponse
    );
  }

  public fetchInvitations(
    criteria: SearchCriteriaDto<InvitationSearchCriteria>
  ): Observable<SearchResultDto<InvitationDTO>> {
    return this.http.post<SearchResultDto<InvitationDTO>>(
      `${this.baseUrl}/search`,
      criteria
    );
  }
}
