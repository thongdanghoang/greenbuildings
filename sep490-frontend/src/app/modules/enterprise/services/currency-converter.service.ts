import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {AppRoutingConstants} from '../../../app-routing.constant';

@Injectable()
export class CurrencyConverterService {
  private readonly apiUrl = `${AppRoutingConstants.ENTERPRISE_API_URL}/currency-converter`;

  constructor(private readonly httpClient: HttpClient) {}

  convertToVndCurrency(
    amount: number,
    currency: 'VND' | 'USD' | 'CNY'
  ): Observable<number> {
    return this.httpClient.get<number>(
      `${this.apiUrl}/VND/${amount}/${currency}`
    );
  }
}
