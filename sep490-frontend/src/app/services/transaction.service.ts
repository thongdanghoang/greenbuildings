import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {TransactionAdminCriteria, TransactionCriteria, TransactionEnterpriseAdminDTO} from '@models/transactions';
import {Observable} from 'rxjs';
import {AppRoutingConstants} from '../app-routing.constant';
import {UUID} from '../../types/uuid';
import {TransactionDTO} from '@models/enterprise';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {
  constructor(private readonly httpClient: HttpClient) {}

  public search(
    criteria: SearchCriteriaDto<TransactionCriteria>,
    id: UUID | undefined
  ): Observable<SearchResultDto<TransactionDTO>> {
    return this.httpClient.post<SearchResultDto<TransactionDTO>>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/transaction/search/building/${id}`,
      criteria
    );
  }

  public searchTransactionAdmin(
    criteria: SearchCriteriaDto<TransactionAdminCriteria>
  ): Observable<SearchResultDto<TransactionEnterpriseAdminDTO>> {
    return this.httpClient.post<SearchResultDto<TransactionEnterpriseAdminDTO>>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/transaction/search/enterprise`,
      criteria
    );
  }
}
