import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {AppRoutingConstants} from '../app-routing.constant';

export interface AddressSuggestion {
  displayName: string;
  road: string;
  quarter: string;
  suburb: string;
  city: string;
  postcode: string;
  country: string;
  countryCode: string;
}

@Injectable({
  providedIn: 'root'
})
export class GeocodingService {
  private readonly GEOCODING_API_URL: string = `${AppRoutingConstants.ENTERPRISE_API_URL}/${AppRoutingConstants.GEOCODING_PATH}`;
  private readonly apiKey = 'MGjVW7pUXfGtN0ygOnivcBob2qNp0rwGWwzQdx7A'; // Thay bằng API key của bạn
  private readonly baseUrl = 'https://rsapi.goong.io/Place/AutoComplete';
  constructor(private readonly httpClient: HttpClient) {}

  reverse(latitude: number, longitude: number): Observable<AddressSuggestion> {
    return this.httpClient.get<AddressSuggestion>(
      `${this.GEOCODING_API_URL}/reverse?latitude=${latitude}&longitude=${longitude}`
    );
  }

  getPlaceAutocomplete(input: string, location?: string): Observable<any> {
    const url = `${this.baseUrl}?api_key=${this.apiKey}&input=${encodeURIComponent(input)}${
      location ? `&location=${location}` : ''
    }`;
    return this.httpClient.get(url);
  }

  getPlaceDetail(placeId: string): Observable<any> {
    const url = `https://rsapi.goong.io/Place/Detail?place_id=${placeId}&api_key=${this.apiKey}`;
    return this.httpClient.get(url);
  }
}
