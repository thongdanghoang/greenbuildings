import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import * as geojson from 'geojson';

@Injectable()
export class RegionService {
  private readonly DEMO_STATES_DATA_PATH = '/assets/data/gz_2010_us_050_00_5m.json';

  constructor(private readonly http: HttpClient) {}

  getStateShapes(): Observable<geojson.GeoJsonObject | geojson.GeoJsonObject[] | null> {
    return this.http.get<geojson.GeoJsonObject | geojson.GeoJsonObject[] | null>(this.DEMO_STATES_DATA_PATH);
  }
}
