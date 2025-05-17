import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {AssetDTO} from '@generated/models/asset-dto';
import {AssetView} from '@generated/models/asset-view';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';
import {Observable} from 'rxjs';
import {UUID} from '../../types/uuid';
import {AppRoutingConstants} from '../app-routing.constant';

@Injectable({
  providedIn: 'root'
})
export class AssetService {
  public static readonly PATH: string = `${AppRoutingConstants.ASSET_MANAGEMENT}`;
  public static readonly URL: string = `${AppRoutingConstants.ENTERPRISE_API_URL}/${AssetService.PATH}`;

  constructor(private readonly httpClient: HttpClient) {}

  saveAsset(assetDTO: AssetDTO): Observable<AssetView> {
    return this.httpClient.post(AssetService.URL, assetDTO);
  }

  getAssetById(id: UUID): Observable<AssetView> {
    return this.httpClient.get<AssetView>(`${AssetService.URL}/${id}`);
  }

  searchAssets(searchCriteria: SearchCriteriaDto<void>): Observable<SearchResultDto<AssetView>> {
    return this.httpClient.post<SearchResultDto<AssetView>>(`${AssetService.URL}/search`, searchCriteria);
  }

  deleteAsset(id: UUID): Observable<void> {
    return this.httpClient.delete<void>(`${AssetService.URL}/${id}`);
  }
}
