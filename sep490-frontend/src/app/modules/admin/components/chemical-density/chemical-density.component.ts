import {
  Component,
  EventEmitter,
  OnInit,
  TemplateRef,
  ViewChild,
  inject
} from '@angular/core';
import {SubscriptionAwareComponent} from '../../../core/subscription-aware.component';
import {
  DialogService,
  DynamicDialogConfig,
  DynamicDialogRef
} from 'primeng/dynamicdialog';
import {
  SearchCriteriaDto,
  SearchResultDto
} from '../../../shared/models/base-models';
import {Observable, takeUntil} from 'rxjs';
import {TableTemplateColumn} from '../../../shared/components/table-template/table-template.component';
import {Router} from '@angular/router';
import {ApplicationService} from '../../../core/services/application.service';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {MessageService} from 'primeng/api';
import {ModalProvider} from '../../../shared/services/modal-provider';
import {TranslateService} from '@ngx-translate/core';
import {UUID} from '../../../../../types/uuid';
import {ChemicalDensityDTO} from '../../../shared/models/shared-models';
import {ChemicalDensityService} from '../../services/chemical-density.service';
import {ChemicalDensityDialogComponent} from '../../dialog/chemical-density-dialog/chemical-density-dialog.component';
export interface ChemicalDensityCriteria {
  criteria: string;
}
@Component({
  selector: 'app-chemical-density',
  templateUrl: './chemical-density.component.html',
  styleUrl: './chemical-density.component.css'
})
export class ChemicalDensityComponent
  extends SubscriptionAwareComponent
  implements OnInit
{
  @ViewChild('actionsTemplate', {static: true})
  actionsTemplate!: TemplateRef<any>;
  ref: DynamicDialogRef | undefined;
  protected readonly AppRoutingConstants = AppRoutingConstants;
  protected fetchUsers!: (
    criteria: SearchCriteriaDto<ChemicalDensityCriteria>
  ) => Observable<SearchResultDto<ChemicalDensityDTO>>;
  protected cols: TableTemplateColumn[] = [];
  protected readonly searchEvent: EventEmitter<void> = new EventEmitter();
  protected readonly clearSelectedEvent: EventEmitter<void> =
    new EventEmitter();
  protected selected: ChemicalDensityDTO[] = [];
  protected searchCriteria: ChemicalDensityCriteria = {criteria: ''};
  private readonly router = inject(Router);

  constructor(
    protected readonly applicationService: ApplicationService,
    private readonly userService: ChemicalDensityService,
    private readonly messageService: MessageService,
    private readonly modalProvider: ModalProvider,
    private readonly translate: TranslateService,
    private readonly dialogService: DialogService
  ) {
    super();
  }

  ngOnInit(): void {
    this.buildCols();
    this.fetchUsers = this.userService.getChemicalDensity.bind(
      this.userService
    );
  }

  buildCols(): void {
    this.cols.push({
      field: 'chemicalFormula',
      header: 'admin.chemicalDensity.formal'
    });
    this.cols.push({
      field: 'value',
      header: 'admin.chemicalDensity.value'
    });
    this.cols.push({
      field: 'unitNumerator',
      header: 'admin.chemicalDensity.unitNumerator'
    });
    this.cols.push({
      field: 'unitDenominator',
      header: 'admin.chemicalDensity.unitDenominator'
    });
    this.cols.push({
      field: 'actions',
      header: '',
      templateRef: this.actionsTemplate
    });
  }

  onDelete(rowData: ChemicalDensityDTO): void {
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
    this.ref = this.dialogService.open(ChemicalDensityDialogComponent, config);
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
  onEdit(rowData: ChemicalDensityDTO): void {
    this.selected = [rowData];
    const emissionId = this.selected[0].id; // Fixed typo: emisstionId -> emissionId
    this.openNewActivityDialog(emissionId);
  }

  onSelectionChange(selectedUsers: ChemicalDensityDTO[]): void {
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

    this.userService.deleteChemicalDensity(userIds).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
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
        this.messageService.add({
          severity: 'error',
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
