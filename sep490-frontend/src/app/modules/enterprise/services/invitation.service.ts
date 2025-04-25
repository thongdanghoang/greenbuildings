import {Injectable} from '@angular/core';
import {UUID} from '../../../../types/uuid';
import {AppRoutingConstants} from '../../../app-routing.constant';
import {Observable} from 'rxjs';
import {
  SearchCriteriaDto,
  SearchResultDto
} from '../../shared/models/base-models';
import {InvitationDTO, InvitationStatus} from '../models/enterprise.dto';
import {HttpClient} from '@angular/common/http';

export interface InvitationResponse {
  id: UUID;
  status: InvitationStatus;
}

export interface InvitationSearchCriteria {
  enterpriseId: UUID;
  buildingId?: UUID;
  buildingGroupId?: UUID;
  tenantEmail: string;
  status?: InvitationStatus;
}

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
