import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {AppRoutingConstants} from '../app-routing.constant';
import {RevenueReportDTO} from '@generated/models/revenue-report-dto';

@Injectable({
  providedIn: 'root'
})
export class RevenueReportService {
  private readonly baseUrl = `${AppRoutingConstants.ENTERPRISE_API_URL}/revenue`;

  constructor(private readonly http: HttpClient) {}

  public getReport(): Observable<RevenueReportDTO> {
    return this.http.get<RevenueReportDTO>(`${this.baseUrl}/generate-report`);
  }
}
