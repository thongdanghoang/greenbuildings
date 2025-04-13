import {
  Component,
  EventEmitter,
  OnInit,
  TemplateRef,
  ViewChild
} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {
  DialogService,
  DynamicDialogConfig,
  DynamicDialogRef
} from 'primeng/dynamicdialog';
import {Observable, takeUntil} from 'rxjs';
import {UUID} from '../../../../../types/uuid';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {ApplicationService} from '../../../core/services/application.service';
import {SubscriptionAwareComponent} from '../../../core/subscription-aware.component';
import {TableTemplateColumn} from '../../../shared/components/table-template/table-template.component';
import {
  SearchCriteriaDto,
  SearchResultDto
} from '../../../shared/models/base-models';
import {ModalProvider} from '../../../shared/services/modal-provider';
import {ToastProvider} from '../../../shared/services/toast-provider';
import {ActivityTypeDialogComponent} from '../../dialog/activity-type-dialog/activity-type-dialog.component';
import {ActivityType} from '../../models/enterprise.dto';
import {ActivityTypeService} from '../../services/activity-type.service';

export interface ActivityTypeCriteria {
  criteria: string;
}
@Component({
  selector: 'app-activity-type',
  templateUrl: './activity-type.component.html',
  styleUrl: './activity-type.component.css'
})
export class ActivityTypeComponent
  extends SubscriptionAwareComponent
  implements OnInit
{
  @ViewChild('scopeTemplate', {static: true})
  scopeTemplate!: TemplateRef<any>;
  @ViewChild('actionsTemplate', {static: true})
  actionsTemplate!: TemplateRef<any>;
  ref: DynamicDialogRef | undefined;
  protected readonly AppRoutingConstants = AppRoutingConstants;
  protected fetchUsers!: (
    criteria: SearchCriteriaDto<ActivityTypeCriteria>
  ) => Observable<SearchResultDto<ActivityType>>;
  protected cols: TableTemplateColumn[] = [];
  protected readonly searchEvent: EventEmitter<void> = new EventEmitter();
  protected readonly clearSelectedEvent: EventEmitter<void> =
    new EventEmitter();
  protected selected: ActivityType[] = [];
  protected searchCriteria: ActivityTypeCriteria = {criteria: ''};

  constructor(
    protected readonly applicationService: ApplicationService,
    private readonly userService: ActivityTypeService,
    private readonly messageService: ToastProvider,
    private readonly modalProvider: ModalProvider,
    private readonly translate: TranslateService,
    private readonly dialogService: DialogService
  ) {
    super();
  }

  ngOnInit(): void {
    this.buildCols();
    this.fetchUsers = this.userService.getActivityType.bind(this.userService);
  }

  buildCols(): void {
    this.cols.push({
      field: 'name',
      header: 'enterprise.Users.table.name',
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

  openNewActivityDialog(emissionId?: UUID): void {
    // Made emissionId optional with ?
    this.selected = []; // Clear local selection
    this.clearSelectedEvent.emit();
    const config: DynamicDialogConfig<UUID | undefined> = {
      // Allow undefined in config.data
      data: emissionId, // Will be undefined for add, UUID for edit
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
  addEmissionSource(): void {
    this.openNewActivityDialog(undefined); // Explicitly pass undefined for clarity
  }

  // Edit an existing emission source
  onEdit(rowData: ActivityType): void {
    this.selected = [rowData];
    const emissionId = this.selected[0].id; // Fixed typo: emisstionId -> emissionId
    this.openNewActivityDialog(emissionId);
  }

  onSelectionChange(selectedUsers: ActivityType[]): void {
    this.selected = selectedUsers;
  }

  search(): void {
    this.searchEvent.emit();
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
          this.deleteUsers();
        }
      });
  }

  private deleteUsers(): void {
    const userIds: UUID[] = this.selected.map(user => user.id);

    this.userService.deleteActivityType(userIds).subscribe({
      next: () => {
        this.messageService.success({
          summary: this.translate.instant(
            'enterprise.type.message.success.summary'
          ),
          detail: this.translate.instant(
            'enterprise.type.message.success.detail'
          )
        });
        this.selected = []; // Clear local selection
        this.searchEvent.emit(); // Refresh table
        this.clearSelectedEvent.emit();
      },
      error: () => {
        this.messageService.businessError({
          summary: this.translate.instant(
            'enterprise.Users.message.error.summary'
          ),
          detail: this.translate.instant(
            'enterprise.Users.message.error.detail'
          )
        });
      }
    });
  }
}
