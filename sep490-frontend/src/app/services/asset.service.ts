import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {AssetView} from '@generated/models/asset-view';
import {SearchCriteriaDto, SearchResultDto, SelectableItem} from '@shared/models/base-models';
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

  getAssetById(id: UUID): Observable<AssetView> {
    return this.httpClient.get<AssetView>(`${AssetService.URL}/${id}`);
  }

  searchAssets(searchCriteria: SearchCriteriaDto<void>): Observable<SearchResultDto<AssetView>> {
    return this.httpClient.post<SearchResultDto<AssetView>>(`${AssetService.URL}/search`, searchCriteria);
  }

  deleteAsset(id: UUID): Observable<void> {
    return this.httpClient.delete<void>(`${AssetService.URL}/${id}`);
  }

  selectable(buildingId?: UUID, assetId?: UUID): Observable<SelectableItem<UUID>[]> {
    let url = buildingId ? `${AssetService.URL}/selectable?buildingId=${buildingId}` : `${AssetService.URL}/selectable`;
    if (assetId) {
      url += `${buildingId ? '&' : '?'}excludeId=${assetId}`;
    }
    return this.httpClient.get<SelectableItem<UUID>[]>(url);
  }
}
