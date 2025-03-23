import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {
  SearchCriteriaDto,
  SearchResultDto
} from '../../shared/models/base-models';
import {Observable} from 'rxjs';
import {AppRoutingConstants} from '../../../app-routing.constant';
import {EmissionSourceCriteria} from '../components/emission-source/emission-source.component';
import {EmissionSource} from '../../enterprise/models/enterprise.dto';

@Injectable()
export class EmissionSourceService {
  constructor(private readonly httpClient: HttpClient) {}

  public getEmissionSource(
    criteria: SearchCriteriaDto<EmissionSourceCriteria>
  ): Observable<SearchResultDto<EmissionSource>> {
    return this.httpClient.post<SearchResultDto<EmissionSource>>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/emission-source/search`,
      criteria
    );
  }
}
