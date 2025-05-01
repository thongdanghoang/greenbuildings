import {Location} from '@angular/common';
import {Component, EventEmitter, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ActivitySearchCriteria} from '@models/emission-activity';
import {BuildingDetails, EmissionActivity} from '@models/enterprise';
import {TranslateService} from '@ngx-translate/core';
import {BuildingService} from '@services/building.service';
import {EmissionActivityService} from '@services/emission-activity.service';
import {TableTemplateColumn} from '@shared/components/table-template/table-template.component';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';
import {ModalProvider} from '@shared/services/modal-provider';
import {ToastProvider} from '@shared/services/toast-provider';
import {DialogService, DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {Observable, filter, map, switchMap} from 'rxjs';
import {takeUntil} from 'rxjs/operators';
import {validate} from 'uuid';
import {UUID} from '../../../../../types/uuid';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {NewActivityDialogComponent} from '../../dialog/new-activity-dialog/new-activity-dialog.component';

@Component({
  selector: 'app-manage-common-activity',
  templateUrl: './manage-common-activity.component.html',
  styleUrl: './manage-common-activity.component.css'
})
export class ManageCommonActivityComponent extends SubscriptionAwareComponent implements OnInit {
  building!: BuildingDetails;

  @ViewChild('typeTemplate', {static: true})
  typeTemplate!: TemplateRef<any>;
  @ViewChild('actionsTemplate', {static: true})
  actionsTemplate!: TemplateRef<any>;
  ref: DynamicDialogRef | undefined;

  protected fetchActivity!: (
    criteria: SearchCriteriaDto<ActivitySearchCriteria>
  ) => Observable<SearchResultDto<EmissionActivity>>;

  protected searchCriteria!: ActivitySearchCriteria;

  protected cols: TableTemplateColumn[] = [];
  protected readonly searchEvent: EventEmitter<void> = new EventEmitter();
  protected readonly clearSelectedEvent: EventEmitter<void> = new EventEmitter();
  protected selected: EmissionActivity[] = [];

  constructor(
    private readonly activatedRoute: ActivatedRoute,
    private readonly router: Router,
    private readonly activityService: EmissionActivityService,
    private readonly buildingService: BuildingService,
    private readonly dialogService: DialogService,
    private readonly translate: TranslateService,
    private readonly modalProvider: ModalProvider,
    private readonly msgService: ToastProvider,
    private readonly location: Location
  ) {
    super();
  }

  ngOnInit(): void {
    this.buildCols();
    this.fetchBuildingDetails();
    this.searchCriteria = {buildingId: '' as UUID};
    this.fetchActivity = this.activityService.fetchActivityOfBuilding.bind(this.activityService);
  }

  fetchBuildingDetails(): void {
    this.activatedRoute.paramMap
      .pipe(
        takeUntil(this.destroy$),
        map(params => params.get('buildingId')),
        filter((idParam): idParam is string => !!idParam),
        filter(id => validate(id)),
        switchMap(id => this.buildingService.getBuildingDetails(id as UUID))
      )
      .subscribe(building => {
        this.building = building;
        this.searchCriteria.buildingId = this.building.id;
        this.searchEvent.emit();
      });
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

  openNewActivityDialog(): void {
    const config: DynamicDialogConfig = {
      data: {
        buildingId: this.building.id
      },
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

  onSelectionChange(selected: EmissionActivity[]): void {
    this.selected = selected;
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

  goBack(): void {
    this.location.back();
  }

  openActivityDetailsDialog(activity: EmissionActivity): void {
    void this.router.navigate([
      AppRoutingConstants.ENTERPRISE_PATH,
      AppRoutingConstants.EMISSION_ACTIVITY_DETAIL_PATH,
      activity.id
    ]);
  }
}
