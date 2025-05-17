import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {EnterpriseUserDetails} from '@models/enterprise-user';
import {UserConfigs} from '@models/user-configs';
import {UserLanguage} from '@shared/enums/user-language.enum';
import {Observable} from 'rxjs';
import {AppRoutingConstants} from '../app-routing.constant';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  static readonly USER_API_URL = 'user';

  constructor(private readonly httpClient: HttpClient) {}

  get userConfigs(): Observable<UserConfigs> {
    return this.httpClient.get<UserConfigs>(`${AppRoutingConstants.IDP_API_URL}/${UserService.USER_API_URL}/language`);
  }

  changeLanguage(language: UserLanguage): Observable<void> {
    return this.httpClient.put<void>(
      `${AppRoutingConstants.IDP_API_URL}/${UserService.USER_API_URL}/language`,
      {language},
      {}
    );
  }

  public getUserInfo(): Observable<EnterpriseUserDetails> {
    return this.httpClient.get<EnterpriseUserDetails>(`${AppRoutingConstants.IDP_API_URL}/${UserService.USER_API_URL}`);
  }
}
