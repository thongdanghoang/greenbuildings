import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {AppRoutingConstants} from '../app-routing.constant';

@Injectable({
  providedIn: 'root'
})
export class WalletService {
  constructor(private readonly httpClient: HttpClient) {}

  public getWalletBalance(): Observable<number> {
    return this.httpClient.get<number>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/wallet/balance`
    );
  }
}
