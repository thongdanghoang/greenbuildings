import {
  Component,
  EventEmitter,
  OnInit,
  TemplateRef,
  ViewChild
} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import moment from 'moment';
import {
  DialogService,
  DynamicDialogConfig,
  DynamicDialogRef
} from 'primeng/dynamicdialog';
import {Observable, Observer, filter, map, switchMap, takeUntil} from 'rxjs';
import {validate} from 'uuid';
import {UUID} from '../../../../../types/uuid';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {BuildingDetails, EmissionActivity} from '../../../../models/enterprise';
import {BuildingService} from '../../../../services/building.service';
import {
  ActivitySearchCriteria,
  EmissionActivityService
} from '../../../../services/emission-activity.service';
import {ApplicationService} from '../../../core/services/application.service';
import {SubscriptionAwareComponent} from '../../../core/subscription-aware.component';
import {TableTemplateColumn} from '../../../shared/components/table-template/table-template.component';
import {
  SearchCriteriaDto,
  SearchResultDto
} from '../../../shared/models/base-models';
import {ModalProvider} from '../../../shared/services/modal-provider';
import {ToastProvider} from '../../../shared/services/toast-provider';
import {NewActivityDialogComponent} from '../../dialog/new-activity-dialog/new-activity-dialog.component';
import {ReportDialogComponent} from '../../dialog/report-dialog/report-dialog.component';

@Component({
  selector: 'app-emission-activity',
  templateUrl: './emission-activity.component.html',
  styleUrl: './emission-activity.component.css'
})
export class EmissionActivityComponent
  extends SubscriptionAwareComponent
  implements OnInit
{
  ref: DynamicDialogRef | undefined;
  @ViewChild('actionsTemplate', {static: true})
  actionsTemplate!: TemplateRef<any>;
  @ViewChild('typeTemplate', {static: true})
  typeTemplate!: TemplateRef<any>;
  protected fetchActivity!: (
    criteria: SearchCriteriaDto<ActivitySearchCriteria>
  ) => Observable<SearchResultDto<EmissionActivity>>;
  protected searchCriteria!: ActivitySearchCriteria;
  protected cols: TableTemplateColumn[] = [];
  protected readonly searchEvent: EventEmitter<void> = new EventEmitter();
  protected readonly clearSelectedEvent: EventEmitter<void> =
    new EventEmitter();

  protected buildingDetail!: BuildingDetails;
  protected selected: EmissionActivity[] = [];
  private readonly fetchBuildingObserver: Observer<BuildingDetails> = {
    next: building => {
      if (!building.subscriptionDTO) {
        this.msgService.businessError({
          summary: this.translate.instant('http.error.status.403.title')
        });
        void this.router.navigate([
          AppRoutingConstants.ENTERPRISE_PATH,
          AppRoutingConstants.BUILDING_PATH
        ]);
      } else {
        this.handleAfterSuccessValidation(building);
      }
    },
    error: () => {
      void this.router.navigate([
        AppRoutingConstants.ENTERPRISE_PATH,
        AppRoutingConstants.BUILDING_PATH
      ]);
    },
    complete: () => {}
  };

  constructor(
    private readonly activatedRoute: ActivatedRoute,
    private readonly msgService: ToastProvider,
    private readonly router: Router,
    private readonly translate: TranslateService,
    private readonly buildingService: BuildingService,
    private readonly activityService: EmissionActivityService,
    private readonly dialogService: DialogService,
    private readonly modalProvider: ModalProvider,
    protected readonly applicationService: ApplicationService
  ) {
    super();
  }

  ngOnInit(): void {
    this.fetchAndValidateBuilding();
    this.fetchActivity = this.activityService.fetchActivityOfBuilding.bind(
      this.activityService
    );
    this.searchCriteria = {buildingGroupId: '' as UUID};
  }

  handleAfterSuccessValidation(building: BuildingDetails): void {
    this.buildingDetail = building;
    this.searchCriteria.buildingGroupId = this.buildingDetail.id;
    this.buildCols();
    this.searchEvent.emit();
  }

  onSelectionChange(selected: EmissionActivity[]): void {
    this.selected = selected;
  }

  goBack(): void {
    void this.router.navigate([
      AppRoutingConstants.ENTERPRISE_PATH,
      AppRoutingConstants.BUILDING_PATH
    ]);
  }

  buildCols(): void {
    this.cols.push({
      header: 'enterprise.emission.activity.table.name',
      field: 'name',
      sortable: true
    });
    this.cols.push({
      header: 'enterprise.emission.activity.table.type',
      field: 'type',
      sortable: true,
      templateRef: this.typeTemplate
    });
    this.cols.push({
      header: 'enterprise.emission.activity.table.category',
      field: 'category',
      sortable: true
    });
    this.cols.push({
      header: 'enterprise.emission.activity.table.description',
      field: 'description',
      sortable: true
    });
    this.cols.push({
      field: 'actions',
      header: '',
      templateRef: this.actionsTemplate
    });
  }

  fetchAndValidateBuilding(): void {
    this.activatedRoute.paramMap
      .pipe(
        takeUntil(this.destroy$),
        map(params => params.get('buildingId')),
        filter((idParam): idParam is string => !!idParam),
        filter(id => validate(id)),
        switchMap(id => this.buildingService.getBuildingDetails(id as UUID))
      )
      .subscribe(this.fetchBuildingObserver);
  }

  getRemainingDays(): number | string {
    if (!this.buildingDetail?.subscriptionDTO?.endDate) {
      return 'N/A'; // Handle missing date
    }
    const endDate = moment(this.buildingDetail.subscriptionDTO.endDate);
    const today = moment();
    const diff = endDate.diff(today, 'days'); // Difference in days
    return diff >= 0 ? diff : 'Expired';
  }

  openNewActivityDialog(): void {
    const config: DynamicDialogConfig<UUID> = {
      data: this.buildingDetail.id,
      closeOnEscape: true,
      dismissableMask: true,
      showHeader: false
    };
    this.ref = this.dialogService.open(NewActivityDialogComponent, config);
    this.ref.onClose.subscribe(rs => {
      if (rs) {
        this.searchEvent.emit();
      }
    });
  }

  generateReport(): void {
    const config: DynamicDialogConfig<UUID> = {
      data: this.buildingDetail.id,
      closeOnEscape: true,
      closable: true,
      dismissableMask: true,
      showHeader: true,
      header: this.translate.instant(
        'enterprise.emission.activity.generateReport'
      )
    };
    this.ref = this.dialogService.open(ReportDialogComponent, config);
    this.ref.onClose.subscribe(rs => {
      if (rs) {
        this.searchEvent.emit();
      }
    });
  }

  openActivityDetailsDialog(activity: EmissionActivity): void {
    void this.router.navigate([
      AppRoutingConstants.ENTERPRISE_PATH,
      AppRoutingConstants.EMISSION_ACTIVITY_DETAIL_PATH,
      activity.id
    ]);
  }

  confirmDelete(): void {
    this.modalProvider
      .showConfirm({
        message: this.translate.instant('common.defaultConfirmMessage'),
        header: this.translate.instant('common.confirmHeader'),
        icon: 'pi pi-info-circle',
        acceptButtonStyleClass: 'p-button-danger p-button-text min-w-20',
        rejectButtonStyleClass: 'p-button-contrast p-button-text min-w-20',
        acceptIcon: 'none',
        acceptLabel: this.translate.instant('common.accept'),
        rejectIcon: 'none',
        rejectLabel: this.translate.instant('common.reject')
      })
      .pipe(takeUntil(this.destroy$))
      .subscribe((result: boolean): void => {
        if (result) {
          this.deleteActivites();
        }
      });
  }

  private deleteActivites(): void {
    const ids: UUID[] = this.selected.map(activity => activity.id);

    this.activityService.deleteActivities(ids).subscribe({
      next: () => {
        this.msgService.success({
          summary: this.translate.instant('common.success')
        });
        this.selected = []; // Clear local selection
        this.searchEvent.emit(); // Refresh table
        this.clearSelectedEvent.emit();
      }
    });
  }
}
