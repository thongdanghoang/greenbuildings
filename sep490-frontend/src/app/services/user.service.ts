import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {AppRoutingConstants} from '../app-routing.constant';
import {EnterpriseUserDetails} from '../modules/authorization/models/enterprise-user';
import {UserLocale} from '../modules/shared/enums/user-language.enum';
import {UserConfigs} from '../modules/shared/models/user-configs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  static readonly USER_API_URL = 'user';

  constructor(private readonly httpClient: HttpClient) {}

  get userConfigs(): Observable<UserConfigs> {
    return this.httpClient.get<UserConfigs>(
      `${AppRoutingConstants.IDP_API_URL}/${UserService.USER_API_URL}/locale`
    );
  }

  changeLanguage(locale: UserLocale): Observable<void> {
    return this.httpClient.put<void>(
      `${AppRoutingConstants.IDP_API_URL}/${UserService.USER_API_URL}/locale/${locale}`,
      {}
    );
  }

  public getUserInfo(): Observable<EnterpriseUserDetails> {
    return this.httpClient.get<EnterpriseUserDetails>(
      `${AppRoutingConstants.IDP_API_URL}/${UserService.USER_API_URL}`
    );
  }
}
