import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {UUID} from '../../../../types/uuid';
import {AppRoutingConstants} from '../../../app-routing.constant';
import {SearchCriteriaDto, SearchResultDto} from '../../shared/models/models';
import {UserCriteria} from '../components/users/users.component';
import {EnterpriseUser, EnterpriseUserDetails} from '../models/enterprise-user';

@Injectable()
export class UserService {
  constructor(private readonly httpClient: HttpClient) {}

  public getUsers(
    criteria: SearchCriteriaDto<UserCriteria>
  ): Observable<SearchResultDto<EnterpriseUser>> {
    return this.httpClient.post<SearchResultDto<EnterpriseUser>>(
      `${AppRoutingConstants.IDP_API_URL}/enterprise-user/search`,
      criteria
    );
  }

  public get createOrUpdateUserURL(): string {
    return `${AppRoutingConstants.IDP_API_URL}/enterprise-user`;
  }

  public deleteUsers(userIds: UUID[]): Observable<void> {
    return this.httpClient.delete<void>(
      `${AppRoutingConstants.IDP_API_URL}/enterprise-user/delete`,
      {
        body: userIds
      }
    );
  }

  public getUserById(userId: string): Observable<EnterpriseUserDetails> {
    return this.httpClient.get<EnterpriseUserDetails>(
      `${AppRoutingConstants.IDP_API_URL}/enterprise-user/${userId}`
    );
  }
}
