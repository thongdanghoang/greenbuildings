import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {UUID} from '../../../../types/uuid';
import {
  SearchCriteriaDto,
  SearchResultDto
} from '../../shared/models/base-models';
import {Observable} from 'rxjs';
import {AppRoutingConstants} from '../../../app-routing.constant';
import {PaymentDTO, PaymentDetailDTO} from '../models/payment';
import {PaymentCriteria} from '../components/payment/payment.component';

@Injectable()
export class PaymentService {
  private readonly PAYMENT_ENDPOINT: string = 'payment';
  constructor(private readonly httpClient: HttpClient) {}

  public getPayments(
    criteria: SearchCriteriaDto<PaymentCriteria>
  ): Observable<SearchResultDto<PaymentDTO>> {
    return this.httpClient.post<SearchResultDto<PaymentDTO>>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/${this.PAYMENT_ENDPOINT}/search`,
      criteria
    );
  }

  public getPaymentDetail(
    paymentDetailId: string
  ): Observable<PaymentDetailDTO> {
    return this.httpClient.get<PaymentDetailDTO>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/${this.PAYMENT_ENDPOINT}/${paymentDetailId}`
    );
  }

  public createPayment(id: UUID): Observable<PaymentDTO> {
    return this.httpClient.post<PaymentDTO>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/${this.PAYMENT_ENDPOINT}/${id}`,
      null
    );
  }

  public updatePayment(orderCode: number): Observable<void> {
    return this.httpClient.put<void>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/${this.PAYMENT_ENDPOINT}/${orderCode}`,
      null
    );
  }
}
