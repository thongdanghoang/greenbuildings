import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AppRoutingConstants} from '../app-routing.constant';
import {UUID} from '../../types/uuid';
import {CreditConvertRatio} from '../models/enterprise';

@Injectable({
  providedIn: 'root'
})
export class CreditConvertRatioService {
  private readonly SUBSCRIPTION_ENDPOINT: string = 'subscription';
  constructor(private readonly httpClient: HttpClient) {}

  public getAllCreditConvertRatio(): Observable<CreditConvertRatio[]> {
    return this.httpClient.get<CreditConvertRatio[]>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/${this.SUBSCRIPTION_ENDPOINT}/convert-ratio`
    );
  }
  public getCreditConvertRatio(id: UUID): Observable<CreditConvertRatio> {
    return this.httpClient.get<CreditConvertRatio>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/${this.SUBSCRIPTION_ENDPOINT}/convert-ratio/${id}`
    );
  }
  public get updateRatioURL(): string {
    return `${AppRoutingConstants.ENTERPRISE_API_URL}/${this.SUBSCRIPTION_ENDPOINT}/convert-ratio/update`;
  }
}
