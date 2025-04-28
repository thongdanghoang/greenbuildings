import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import * as L from 'leaflet';
import {PopupService} from './popup.service';

@Injectable()
export class MarkerService {
  private readonly CAPITALS_PATH: string = '/assets/data/usa-capitals.geojson';

  constructor(
    private readonly http: HttpClient,
    private readonly popupService: PopupService
  ) {}

  makeCapitalMarkers(map: L.Map): void {
    this.http.get(this.CAPITALS_PATH).subscribe((res: any) => {
      for (const c of res.features) {
        const lon = c.geometry.coordinates[0];
        const lat = c.geometry.coordinates[1];
        const marker = L.marker([lat, lon]);
        marker.bindPopup(this.popupService.makeCapitalPopup(c.properties) as string);
        marker.addTo(map);
      }
    });
  }
}
