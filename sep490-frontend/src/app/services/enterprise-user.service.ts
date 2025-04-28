import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {EnterpriseUser, EnterpriseUserDetails} from '@models/enterprise-user';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';
import {Observable} from 'rxjs';
import {UUID} from '../../types/uuid';
import {AppRoutingConstants} from '../app-routing.constant';
import {UserCriteria} from '../modules/authorization/components/users/users.component';

@Injectable({
  providedIn: 'root'
})
export class EnterpriseUserService {
  static readonly ENTERPRISE_USER_API_URL = 'enterprise-user';

  constructor(private readonly httpClient: HttpClient) {}

  get createNewEnterpriseURL(): string {
    return `${AppRoutingConstants.IDP_API_URL}/${EnterpriseUserService.ENTERPRISE_USER_API_URL}/new-enterprise`;
  }

  public getUsers(
    criteria: SearchCriteriaDto<UserCriteria>
  ): Observable<SearchResultDto<EnterpriseUser>> {
    return this.httpClient.post<SearchResultDto<EnterpriseUser>>(
      `${AppRoutingConstants.IDP_API_URL}/${EnterpriseUserService.ENTERPRISE_USER_API_URL}/search`,
      criteria
    );
  }

  public get createOrUpdateUserURL(): string {
    return `${AppRoutingConstants.IDP_API_URL}/${EnterpriseUserService.ENTERPRISE_USER_API_URL}`;
  }

  public get updateBasicUser(): string {
    return `${AppRoutingConstants.IDP_API_URL}/${EnterpriseUserService.ENTERPRISE_USER_API_URL}/self-update`;
  }

  public deleteUsers(userIds: UUID[]): Observable<void> {
    return this.httpClient.delete<void>(
      `${AppRoutingConstants.IDP_API_URL}/${EnterpriseUserService.ENTERPRISE_USER_API_URL}`,
      {
        body: userIds
      }
    );
  }

  public getUserById(userId: string): Observable<EnterpriseUserDetails> {
    return this.httpClient.get<EnterpriseUserDetails>(
      `${AppRoutingConstants.IDP_API_URL}/${EnterpriseUserService.ENTERPRISE_USER_API_URL}/${userId}`
    );
  }

  public getUserOwner(): Observable<EnterpriseUserDetails> {
    return this.httpClient.get<EnterpriseUserDetails>(
      `${AppRoutingConstants.IDP_API_URL}/${EnterpriseUserService.ENTERPRISE_USER_API_URL}/enterprise-owner`
    );
  }

  public sendOTP(): Observable<void> {
    return this.httpClient.get<void>(
      `${AppRoutingConstants.IDP_API_URL}/${EnterpriseUserService.ENTERPRISE_USER_API_URL}/requestOTP`
    );
  }

  public get getValidateOTPURL(): string {
    return `${AppRoutingConstants.IDP_API_URL}/${EnterpriseUserService.ENTERPRISE_USER_API_URL}/validateOTP`;
  }
}
