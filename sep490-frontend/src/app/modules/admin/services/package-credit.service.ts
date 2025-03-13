import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {
  SearchCriteriaDto,
  SearchResultDto
} from '../../shared/models/base-models';
import {Observable} from 'rxjs';
import {AppRoutingConstants} from '../../../app-routing.constant';
import {CreditPackage} from '../../enterprise/models/enterprise.dto';
import {UUID} from '../../../../types/uuid';

@Injectable()
export class PackageCreditService {
  private readonly CREDIT_PACKAGE_ENDPOINT: string = 'credit-package';
  constructor(private readonly httpClient: HttpClient) {}
  public getCreditPackages(
    criteria: SearchCriteriaDto<void>
  ): Observable<SearchResultDto<CreditPackage>> {
    return this.httpClient.post<SearchResultDto<CreditPackage>>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/${this.CREDIT_PACKAGE_ENDPOINT}/search`,
      criteria
    );
  }

  public get createOrUpdatePackageURL(): string {
    return `${AppRoutingConstants.ENTERPRISE_API_URL}/${this.CREDIT_PACKAGE_ENDPOINT}`;
  }

  public getPackageById(pkgId: string): Observable<CreditPackage> {
    return this.httpClient.get<CreditPackage>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/${this.CREDIT_PACKAGE_ENDPOINT}/${pkgId}`
    );
  }

  public deletePackages(pkgIds: UUID[]): Observable<void> {
    return this.httpClient.delete<void>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/${this.CREDIT_PACKAGE_ENDPOINT}`,
      {
        body: pkgIds
      }
    );
  }
}
