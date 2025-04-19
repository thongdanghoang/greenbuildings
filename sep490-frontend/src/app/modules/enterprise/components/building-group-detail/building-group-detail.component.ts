import {
  Component,
  EventEmitter,
  OnInit,
  TemplateRef,
  ViewChild
} from '@angular/core';
import {ActivatedRoute, ParamMap, Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import moment from 'moment/moment';
import {
  DialogService,
  DynamicDialogConfig,
  DynamicDialogRef
} from 'primeng/dynamicdialog';
import {Observable} from 'rxjs';
import {UUID} from '../../../../../types/uuid';
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
import {BuildingGroupService} from '../../services/building-group.service';
import {BuildingGroup, EmissionActivity} from '../../models/enterprise.dto';
import {takeUntil} from 'rxjs/operators';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {
  ActivitySearchCriteria,
  EmissionActivityService
} from '../../services/emission-activity.service';

@Component({
  selector: 'app-building-group-detail',
  templateUrl: './building-group-detail.component.html'
})
export class BuildingGroupDetailComponent
  extends SubscriptionAwareComponent
  implements OnInit
{
  buildingGroup!: BuildingGroup;
  ref: DynamicDialogRef | undefined;
  @ViewChild('typeTemplate', {static: true})
  typeTemplate!: TemplateRef<any>;
  @ViewChild('actionsTemplate', {static: true})
  actionsTemplate!: TemplateRef<any>;
  protected fetchActivity!: (
    criteria: SearchCriteriaDto<ActivitySearchCriteria>
  ) => Observable<SearchResultDto<EmissionActivity>>;
  protected searchCriteria!: ActivitySearchCriteria;
  protected cols: TableTemplateColumn[] = [];
  protected readonly searchEvent: EventEmitter<void> = new EventEmitter();
  protected readonly clearSelectedEvent: EventEmitter<void> =
    new EventEmitter();
  protected selected: EmissionActivity[] = [];

  constructor(
    private readonly activatedRoute: ActivatedRoute,
    private readonly buildingGroupService: BuildingGroupService,
    private readonly router: Router,
    private readonly activityService: EmissionActivityService,
    private readonly dialogService: DialogService,
    private readonly translate: TranslateService,
    private readonly modalProvider: ModalProvider,
    private readonly msgService: ToastProvider,
    protected readonly applicationService: ApplicationService
  ) {
    super();
  }

  ngOnInit(): void {
    this.buildCols();
    this.searchCriteria = {buildingGroupId: '' as UUID};
    this.fetchActivity = this.activityService.fetchActivityOfBuilding.bind(
      this.activityService
    );

    this.fetchBuildingGroupDetails();
  }

  goBack(): void {
    void this.router.navigate([
      AppRoutingConstants.ENTERPRISE_PATH,
      AppRoutingConstants.BUILDING_MANAGEMENT_PATH,
      this.buildingGroup.building.id
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

  onSelectionChange(selected: EmissionActivity[]): void {
    this.selected = selected;
  }

  getRemainingDays(): number | string {
    if (!this.buildingGroup.building?.subscriptionDTO?.endDate) {
      return 'N/A'; // Handle missing date
    }
    const endDate = moment(this.buildingGroup.building.subscriptionDTO.endDate);
    const today = moment();
    const diff = endDate.diff(today, 'days'); // Difference in days
    return diff >= 0 ? diff : 'Expired';
  }

  openNewActivityDialog(): void {
    const config: DynamicDialogConfig<UUID> = {
      data: this.buildingGroup.id,
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
      data: this.buildingGroup.building.id,
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

  fetchBuildingGroupDetails(): void {
    this.activatedRoute.paramMap
      .pipe(takeUntil(this.destroy$))
      .subscribe((params: ParamMap) => {
        const groupId = params.get('id');
        if (groupId) {
          this.buildingGroupService
            .getById(groupId as UUID)
            .subscribe((group: BuildingGroup) => {
              this.buildingGroup = group;
              this.searchCriteria.buildingGroupId = group.id;
              this.searchEvent.emit();
            });
        }
      });
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

  deleteActivites(): void {
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

  openActivityDetailsDialog(activity: EmissionActivity): void {
    void this.router.navigate([
      AppRoutingConstants.ENTERPRISE_PATH,
      AppRoutingConstants.EMISSION_ACTIVITY_DETAIL_PATH,
      activity.id
    ]);
  }
}
