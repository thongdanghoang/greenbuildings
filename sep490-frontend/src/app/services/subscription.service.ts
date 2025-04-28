import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {AppRoutingConstants} from '../app-routing.constant';
import {CreditConvertRatio, SubscribeRequest} from '@models/enterprise';

@Injectable({
  providedIn: 'root'
})
export class SubscriptionService {
  public subscribeRequestURL = `${AppRoutingConstants.ENTERPRISE_API_URL}/subscription`;

  constructor(private readonly httpClient: HttpClient) {}

  public getCreditConvertRatio(): Observable<CreditConvertRatio[]> {
    return this.httpClient.get<CreditConvertRatio[]>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/subscription/convert-ratio`
    );
  }

  public subscribe(body: SubscribeRequest): Observable<void> {
    return this.httpClient.post<void>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/subscription`,
      body
    );
  }
}
