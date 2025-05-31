import {isPlatformBrowser} from '@angular/common';
import {HttpClient} from '@angular/common/http';
import {ChangeDetectorRef, Component, OnDestroy, OnInit, PLATFORM_ID, inject} from '@angular/core';
import {DomSanitizer, SafeResourceUrl} from '@angular/platform-browser';
import {Router} from '@angular/router';
import {BuildingGhgEmission} from '@generated/models/building-ghg-emission';
import {DefaultChartView} from '@generated/models/default-chart-view';
import {DistributionEmissionSource} from '@generated/models/distribution-emission-source';
import {TranslateService} from '@ngx-translate/core';
import {EmissionActivityService} from '@services/emission-activity.service';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';
import {BaseDTO, SelectableItem} from '@shared/models/base-models';
import {ToastProvider} from '@shared/services/toast-provider';
import {DialogService, DynamicDialogRef} from 'primeng/dynamicdialog';
import {SelectChangeEvent} from 'primeng/select';
import {Observable, filter, switchMap, take, takeUntil, tap} from 'rxjs';
import {AppRoutingConstants} from '../../app-routing.constant';
import {CreateDashboardComponent} from './create-dashboard/create-dashboard.component';

interface EnterpriseDashboardDTO extends BaseDTO {
  title: string;
  source: string;
}

export interface CreateEnterpriseDashboardDTO {
  title: string;
  source: string;
}

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent extends SubscriptionAwareComponent implements OnInit, OnDestroy {
  safeUrl!: SafeResourceUrl;
  selectableDashboards: SelectableItem<EnterpriseDashboardDTO>[] | undefined;
  selectedDashboard: SelectableItem<EnterpriseDashboardDTO> | undefined;
  ref: DynamicDialogRef | undefined;

  platformId = inject(PLATFORM_ID);

  documentStyle = getComputedStyle(document.documentElement);
  textColor = this.documentStyle.getPropertyValue('--p-text-color');
  surfaceBorder = this.documentStyle.getPropertyValue('--p-content-border-color');
  textColorSecondary = this.documentStyle.getPropertyValue('--p-text-muted-color');

  totalEmissions = 0;
  dataPieArea: any;
  optionsPieOptions = {
    plugins: {
      legend: {
        labels: {
          usePointStyle: true,
          color: this.textColor
        }
      }
    }
  };
  basicData: any;
  basicOptions = {
    plugins: {
      legend: {
        labels: {
          color: this.textColor
        }
      }
    },
    scales: {
      x: {
        ticks: {
          color: this.textColorSecondary
        },
        grid: {
          color: this.surfaceBorder
        }
      },
      y: {
        beginAtZero: true,
        ticks: {
          color: this.textColorSecondary
        },
        grid: {
          color: this.surfaceBorder
        }
      }
    }
  };

  protected dashboards: EnterpriseDashboardDTO[] = [];

  constructor(
    private readonly sanitizer: DomSanitizer,
    private readonly httpClient: HttpClient,
    private readonly dialogService: DialogService,
    private readonly messageService: ToastProvider,
    public readonly translate: TranslateService,
    private readonly cd: ChangeDetectorRef,
    protected readonly emissionActivityService: EmissionActivityService,
    private readonly toastProvider: ToastProvider,
    private readonly router: Router
  ) {
    super();
  }

  ngOnInit(): void {
    this.checkAnyActivatedBuildings();
    this.initChart();
    this.getDashboardData()
      .pipe(
        takeUntil(this.destroy$),
        tap(dashboards => {
          this.selectableDashboards = dashboards.map(
            (dashboard: EnterpriseDashboardDTO): SelectableItem<EnterpriseDashboardDTO> => ({
              label: dashboard.title,
              value: dashboard
            })
          );
        }),
        tap(dashboards => {
          if (dashboards.length > 0) {
            this.selectedDashboard = {
              label: dashboards[0].title,
              value: dashboards[0]
            };
            this.safeUrl = this.sanitizer.bypassSecurityTrustResourceUrl(dashboards[0].source);
          }
        })
      )
      .subscribe(dashboards => (this.dashboards = dashboards));
    this.translate.onLangChange.pipe(takeUntil(this.destroy$)).subscribe((): void => {
      this.initChart();
    });
  }

  checkAnyActivatedBuildings(): void {
    this.emissionActivityService
      .getSelectableBuildings()
      .pipe(
        tap(selectableBuildings => {
          if (selectableBuildings.length === 0) {
            this.toastProvider.warn({
              summary: this.translate.instant('emissions.warning.noBuildingsActivated.summary'),
              detail: this.translate.instant('emissions.warning.noBuildingsActivated.detail')
            });
            void this.router.navigate(['/enterprise/buildings']);
          }
        }),
        takeUntil(this.destroy$)
      )
      .subscribe();
  }

  // eslint-disable-next-line max-lines-per-function
  initChart(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.httpClient
        .get(`${AppRoutingConstants.ENTERPRISE_API_URL}/dashboards/default`)
        .subscribe((result: DefaultChartView): void => {
          this.totalEmissions = result.totalEnterpriseEmissions ?? 0;
          this.dataPieArea = this.distributionEmissionSourcesToChartData(result.distributionEmissionSources);
          this.basicData = this.buildingGhgEmissionsToChartData(result.buildingGhgEmissions);
          this.cd.markForCheck();
        });
    }
  }

  distributionEmissionSourcesToChartData(data?: DistributionEmissionSource[]): any {
    return {
      datasets: [
        {
          label: 'My dataset',
          data: data?.map(source => source.totalGhgEmission),
          backgroundColor: [
            this.documentStyle.getPropertyValue('--p-primary-500'),
            this.documentStyle.getPropertyValue('--p-red-500'),
            this.documentStyle.getPropertyValue('--p-yellow-500'),
            this.documentStyle.getPropertyValue('--p-sky-500'),
            this.documentStyle.getPropertyValue('--p-neutral-500')
          ]
        }
      ],
      labels: data?.map(source => source.sourceName)
    };
  }

  buildingGhgEmissionsToChartData(data?: BuildingGhgEmission[]): any {
    return {
      labels: data?.map(building => building.buildingName),
      datasets: [
        {
          label: this.translate.instant('emissions.chart.totalEmissions'),
          data: data?.map(building => building.totalGhgEmission),
          backgroundColor: [
            this.documentStyle.getPropertyValue('--p-primary-500'),
            this.documentStyle.getPropertyValue('--p-red-500'),
            this.documentStyle.getPropertyValue('--p-yellow-500'),
            this.documentStyle.getPropertyValue('--p-sky-500'),
            this.documentStyle.getPropertyValue('--p-neutral-500')
          ],
          borderWidth: 1
        }
      ]
    };
  }

  override ngOnDestroy(): void {
    super.ngOnDestroy();
    if (this.ref) {
      this.ref.close();
    }
  }

  onChange(event: SelectChangeEvent): void {
    this.safeUrl = this.sanitizer.bypassSecurityTrustResourceUrl(event.value.value.source);
  }

  openCreateDashboardModal(): void {
    this.ref = this.dialogService.open(CreateDashboardComponent, {
      data: {},
      header: this.translate.instant('powerBi.addNewDashBoard'),
      modal: true
    });
    this.ref.onClose
      .pipe(
        take(1),
        filter(dashboard => !!dashboard),
        switchMap(dashboard => this.createDashboardData(dashboard)),
        filter(dashboard => !!dashboard)
      )
      .subscribe({
        next: (dashboard: EnterpriseDashboardDTO): void => {
          this.messageService.success({
            summary: 'Dashboard Created',
            detail: `Created new dashboard ${dashboard.id} successfully`
          });
          this.ngOnInit();
        },
        error: err => {
          this.messageService.businessError({
            summary: 'Dashboard Creation Failed',
            detail: err.error.i18nKey
          });
        }
      });
  }

  private getDashboardData(): Observable<EnterpriseDashboardDTO[]> {
    return this.httpClient.get<EnterpriseDashboardDTO[]>(`${AppRoutingConstants.ENTERPRISE_API_URL}/dashboards`);
  }

  private createDashboardData(payload: CreateEnterpriseDashboardDTO): Observable<EnterpriseDashboardDTO> {
    return this.httpClient.post<EnterpriseDashboardDTO>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/dashboards`,
      payload
    );
  }
}
