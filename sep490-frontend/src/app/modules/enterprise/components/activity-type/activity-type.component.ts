import {Component, EventEmitter, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ActivityTypeCriteria} from '@models/emission-activity';
import {TranslateService} from '@ngx-translate/core';
import {DialogService, DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {Observable, takeUntil} from 'rxjs';
import {UUID} from '../../../../../types/uuid';
import {Location} from '@angular/common';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {ActivityType} from '@models/enterprise';
import {ActivityTypeService} from '@services/activity-type.service';
import {ApplicationService} from '@services/application.service';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';
import {TableTemplateColumn} from '@shared/components/table-template/table-template.component';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';
import {ModalProvider} from '@shared/services/modal-provider';
import {ToastProvider} from '@shared/services/toast-provider';
import {
  ActivityTypeDialogComponent,
  ActivityTypeDialogData
} from '../../dialog/activity-type-dialog/activity-type-dialog.component';

@Component({
  selector: 'app-activity-type',
  templateUrl: './activity-type.component.html',
  styleUrl: './activity-type.component.css'
})
export class ActivityTypeComponent extends SubscriptionAwareComponent implements OnInit {
  @ViewChild('actionsTemplate', {static: true})
  actionsTemplate!: TemplateRef<any>;
  ref: DynamicDialogRef | undefined;
  protected readonly AppRoutingConstants = AppRoutingConstants;
  protected fetchActivityTypes!: (
    criteria: SearchCriteriaDto<ActivityTypeCriteria>
  ) => Observable<SearchResultDto<ActivityType>>;
  protected cols: TableTemplateColumn[] = [];
  protected readonly searchEvent: EventEmitter<void> = new EventEmitter();
  protected readonly clearSelectedEvent: EventEmitter<void> = new EventEmitter();
  protected selected: ActivityType[] = [];
  protected searchCriteria!: ActivityTypeCriteria;
  protected buildingId!: UUID;

  constructor(
    protected readonly applicationService: ApplicationService,
    private readonly activityTypeService: ActivityTypeService,
    private readonly messageService: ToastProvider,
    private readonly modalProvider: ModalProvider,
    private readonly location: Location,
    private readonly activatedRoute: ActivatedRoute,
    private readonly translate: TranslateService,
    private readonly dialogService: DialogService
  ) {
    super();
  }

  ngOnInit(): void {
    this.buildCols();
    this.fetchActivityTypes = this.activityTypeService.getActivityType.bind(this.activityTypeService);
    this.getBuildingId();
  }

  getBuildingId(): void {
    this.activatedRoute.paramMap.pipe(takeUntil(this.destroy$)).subscribe(params => {
      const buildingId = params.get('buildingId');
      if (buildingId) {
        this.buildingId = buildingId as UUID;
        this.searchCriteria = {buildingId: buildingId as UUID, name: ''};
        this.searchEvent.emit();
      }
    });
  }

  buildCols(): void {
    this.cols.push({
      field: 'name',
      header: 'enterprise.Users.table.name',
      sortable: true
    });
    this.cols.push({
      field: 'description',
      header: 'admin.emissionFactor.description',
      sortable: true
    });
    this.cols.push({
      field: 'actions',
      header: '',
      templateRef: this.actionsTemplate
    });
  }

  onDelete(rowData: ActivityType): void {
    this.selected = [rowData];
    this.confirmDelete();
  }

  openNewActivityDialog(typeId?: UUID): void {
    const config: DynamicDialogConfig<ActivityTypeDialogData> = {
      data: {
        buildingId: this.buildingId,
        activityTypeId: typeId
      },
      closeOnEscape: true,
      dismissableMask: true,
      showHeader: false
    };

    // Clean up previous dialog if it exists
    if (this.ref) {
      this.ref.close();
    }

    // Open the dialog with EmissionSourceDialogComponent
    this.ref = this.dialogService.open(ActivityTypeDialogComponent, config);
    this.ref.onClose.subscribe((result: boolean | undefined) => {
      if (result) {
        this.searchEvent.emit(); // Refresh the list if dialog closed with a result
      }
    });
  }

  // Add a new emission source
  addActivityType(): void {
    this.openNewActivityDialog(undefined); // Explicitly pass undefined for clarity
  }

  goBack(): void {
    this.location.back();
  }

  // Edit an existing emission source
  onEdit(rowData: ActivityType): void {
    this.openNewActivityDialog(rowData.id);
  }

  onSelectionChange(selectedUsers: ActivityType[]): void {
    this.selected = selectedUsers;
  }

  confirmDelete(): void {
    this.modalProvider
      .showDefaultConfirm(undefined)
      .pipe(takeUntil(this.destroy$))
      .subscribe((result: boolean): void => {
        if (result) {
          this.deleteUsers();
        }
      });
  }

  private deleteUsers(): void {
    const userIds: UUID[] = this.selected.map(user => user.id);

    this.activityTypeService.deleteActivityType(userIds).subscribe({
      next: () => {
        this.messageService.success({
          summary: this.translate.instant('enterprise.type.message.success.summary'),
          detail: this.translate.instant('enterprise.type.message.success.detail')
        });
        this.selected = []; // Clear local selection
        this.searchEvent.emit(); // Refresh table
        this.clearSelectedEvent.emit();
      },
      error: () => {
        this.messageService.businessError({
          summary: this.translate.instant('enterprise.Users.message.error.summary'),
          detail: this.translate.instant('enterprise.Users.message.error.detail')
        });
      }
    });
  }
}
