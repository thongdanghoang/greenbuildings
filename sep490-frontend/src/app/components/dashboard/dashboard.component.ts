import {HttpClient} from '@angular/common/http';
import {Component, OnDestroy, OnInit} from '@angular/core';
import {DomSanitizer, SafeResourceUrl} from '@angular/platform-browser';
import {DialogService, DynamicDialogRef} from 'primeng/dynamicdialog';
import {SelectChangeEvent} from 'primeng/select';
import {Observable, filter, switchMap, take, takeUntil, tap} from 'rxjs';
import {AppRoutingConstants} from '../../app-routing.constant';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';
import {BaseDTO, SelectableItem} from '@shared/models/base-models';
import {ToastProvider} from '@shared/services/toast-provider';
import {CreateDashboardComponent} from './create-dashboard/create-dashboard.component';
import {TranslateService} from '@ngx-translate/core';

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
export class DashboardComponent
  extends SubscriptionAwareComponent
  implements OnInit, OnDestroy
{
  safeUrl!: SafeResourceUrl;
  selectableDashboards: SelectableItem<EnterpriseDashboardDTO>[] | undefined;
  selectedDashboard: SelectableItem<EnterpriseDashboardDTO> | undefined;
  ref: DynamicDialogRef | undefined;
  protected dashboards: EnterpriseDashboardDTO[] = [];

  constructor(
    private readonly sanitizer: DomSanitizer,
    private readonly httpClient: HttpClient,
    private readonly dialogService: DialogService,
    private readonly messageService: ToastProvider,
    public readonly translate: TranslateService
  ) {
    super();
  }

  ngOnInit(): void {
    this.getDashboardData()
      .pipe(
        takeUntil(this.destroy$),
        tap(dashboards => {
          this.selectableDashboards = dashboards.map(
            (
              dashboard: EnterpriseDashboardDTO
            ): SelectableItem<EnterpriseDashboardDTO> => ({
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
            this.safeUrl = this.sanitizer.bypassSecurityTrustResourceUrl(
              dashboards[0].source
            );
          }
        })
      )
      .subscribe(dashboards => (this.dashboards = dashboards));
  }

  override ngOnDestroy(): void {
    super.ngOnDestroy();
    if (this.ref) {
      this.ref.close();
    }
  }

  onChange(event: SelectChangeEvent): void {
    this.safeUrl = this.sanitizer.bypassSecurityTrustResourceUrl(
      event.value.value.source
    );
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
    return this.httpClient.get<EnterpriseDashboardDTO[]>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/dashboards`
    );
  }

  private createDashboardData(
    payload: CreateEnterpriseDashboardDTO
  ): Observable<EnterpriseDashboardDTO> {
    return this.httpClient.post<EnterpriseDashboardDTO>(
      `${AppRoutingConstants.ENTERPRISE_API_URL}/dashboards`,
      payload
    );
  }
}
