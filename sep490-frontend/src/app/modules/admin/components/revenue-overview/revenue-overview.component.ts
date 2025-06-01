import {Component, OnInit} from '@angular/core';
import {RevenueReportDTO} from '@generated/models/revenue-report-dto';
import {TranslateService} from '@ngx-translate/core';
import {RevenueReportService} from '@services/revenue-report.service';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';

@Component({
  selector: 'app-revenue-overview',
  templateUrl: './revenue-overview.component.html',
  styleUrl: './revenue-overview.component.css'
})
export class RevenueOverviewComponent extends SubscriptionAwareComponent implements OnInit {
  report!: RevenueReportDTO;
  documentStyle!: CSSStyleDeclaration;
  textColor!: string;
  barData: any;
  barOptions: any;

  doughnutData: any;
  doughnutOptions: any;

  constructor(
    private readonly revenueService: RevenueReportService,
    private readonly translate: TranslateService
  ) {
    super();
  }

  ngOnInit(): void {
    this.documentStyle = getComputedStyle(document.documentElement);
    this.textColor = this.documentStyle.getPropertyValue('--p-text-color');
    this.revenueService.getReport().subscribe(rs => {
      this.report = rs;
      this.initChart();
    });
  }

  // eslint-disable-next-line max-lines-per-function
  initChart(): void {
    this.initBasicChart();
    this.initDoughnutChart();
  }

  private initBasicChart(): void {
    const quarters = Object.keys(this.report.revenueByQuarter!);
    const revenues = quarters.map(q => this.report.revenueByQuarter![q].revenue);
    this.barData = {
      labels: quarters,
      datasets: [
        {
          label: this.translate.instant('admin.revenue.title'),
          data: revenues,
          backgroundColor: [
            this.documentStyle.getPropertyValue('--p-primary-500'),
            this.documentStyle.getPropertyValue('--p-red-500'),
            this.documentStyle.getPropertyValue('--p-yellow-500'),
            this.documentStyle.getPropertyValue('--p-sky-500')
          ],
          borderColor: [
            this.documentStyle.getPropertyValue('--p-primary-700'),
            this.documentStyle.getPropertyValue('--p-red-700'),
            this.documentStyle.getPropertyValue('--p-yellow-700'),
            this.documentStyle.getPropertyValue('--p-sky-700')
          ],
          borderWidth: 1
        }
      ]
    };
    this.barOptions = {
      plugins: {
        legend: {
          labels: {
            color: this.textColor
          }
        }
      }
    };
  }

  private initDoughnutChart(): void {
    const types = Object.keys(this.report.numberOfTransactionByType!);
    const revenues = types.map(q => this.report.numberOfTransactionByType![q].count);
    this.doughnutData = {
      labels: types,
      datasets: [
        {
          data: revenues,
          backgroundColor: [
            this.documentStyle.getPropertyValue('--p-primary-500'),
            this.documentStyle.getPropertyValue('--p-red-500'),
            this.documentStyle.getPropertyValue('--p-yellow-500'),
            this.documentStyle.getPropertyValue('--p-sky-500')
          ],
          hoverBackgroundColor: [
            this.documentStyle.getPropertyValue('--p-primary-400'),
            this.documentStyle.getPropertyValue('--p-sky-400'),
            this.documentStyle.getPropertyValue('--p-red-400'),
            this.documentStyle.getPropertyValue('--p-yellow-400')
          ]
        }
      ]
    };

    this.doughnutOptions = {
      cutout: '60%',
      plugins: {
        legend: {
          labels: {
            color: this.textColor
          }
        }
      }
    };
  }
}
