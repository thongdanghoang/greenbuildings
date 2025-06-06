import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {UUID} from '../../types/uuid';
import {AppRoutingConstants} from '../app-routing.constant';
import {CreditPackage, CreditPackageAdmin} from '@models/enterprise';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';

@Injectable({
  providedIn: 'root'
})
export class PackageCreditService {
  private readonly CREDIT_PACKAGE_ENDPOINT: string = 'credit-package';
  constructor(private readonly httpClient: HttpClient) {}
  public getCreditPackages(criteria: SearchCriteriaDto<void>): Observable<SearchResultDto<CreditPackageAdmin>> {
    return this.httpClient.post<SearchResultDto<CreditPackageAdmin>>(
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
    return this.httpClient.delete<void>(`${AppRoutingConstants.ENTERPRISE_API_URL}/${this.CREDIT_PACKAGE_ENDPOINT}`, {
      body: pkgIds
    });
  }
}
