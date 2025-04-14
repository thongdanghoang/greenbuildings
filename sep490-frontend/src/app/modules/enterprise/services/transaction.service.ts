import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AppRoutingConstants} from '../../../app-routing.constant';
import {TransactionDTO} from '../models/enterprise.dto';
import {UUID} from '../../../../types/uuid';
import {
  SearchCriteriaDto,
  SearchResultDto
} from '../../shared/models/base-models';
import {TransactionCriteria} from '../dialog/credit-deduction-history-dialog/credit-deduction-history-dialog.component';

@Injectable()
export class TransactionService {
  constructor(private readonly httpClient: HttpClient) {}

  public getTransactions(id: UUID): Observable<TransactionDTO> {
    return this.httpClient.get<TransactionDTO>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/transaction/building/${id}`
    );
  }

  public search(
    criteria: SearchCriteriaDto<TransactionCriteria>,
    id: UUID | undefined
  ): Observable<SearchResultDto<TransactionDTO>> {
    return this.httpClient.post<SearchResultDto<TransactionDTO>>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/transaction/search/building/${id}`,
      criteria
    );
  }
}
