import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {BusinessLicenseUploadView} from '@generated/models/business-license-upload-view';
import {FileURLResult} from '@generated/models/file-url-result';
import {Observable} from 'rxjs';
import {AppRoutingConstants} from '../app-routing.constant';

@Injectable({
  providedIn: 'root'
})
export class StorageService {
  public readonly STORAGE_URL: string = `${AppRoutingConstants.ENTERPRISE_API_URL}/storage`;

  constructor(private readonly httpClient: HttpClient) {}

  uploadBusinessLicense(license: File): Observable<BusinessLicenseUploadView> {
    const formData = new FormData();
    formData.append('license', license);
    return this.httpClient.post<BusinessLicenseUploadView>(`${this.STORAGE_URL}/business-license`, formData);
  }

  downloadBusinessLicense(fileName: string): Observable<Blob> {
    return this.httpClient.get<Blob>(`${this.STORAGE_URL}/business-license/${fileName}`);
  }

  getBusinessLicenseUrl(fileName: string): Observable<FileURLResult> {
    return this.httpClient.get<FileURLResult>(`${this.STORAGE_URL}/business-license/url/${fileName}`);
  }
}
