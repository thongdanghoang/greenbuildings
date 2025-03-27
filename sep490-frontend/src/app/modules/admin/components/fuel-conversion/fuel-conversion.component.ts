import {
  Component,
  EventEmitter,
  OnInit,
  TemplateRef,
  ViewChild
} from '@angular/core';
import {SubscriptionAwareComponent} from '../../../core/subscription-aware.component';
import {
  SearchCriteriaDto,
  SearchResultDto
} from '../../../shared/models/base-models';
import {Observable} from 'rxjs';
import {FuelConversion} from '../../../enterprise/models/enterprise.dto';
import {TableTemplateColumn} from '../../../shared/components/table-template/table-template.component';
import {ApplicationService} from '../../../core/services/application.service';
import {MessageService} from 'primeng/api';
import {ModalProvider} from '../../../shared/services/modal-provider';
import {TranslateService} from '@ngx-translate/core';
import {FuelConversionService} from '../../services/fuel-conversion.service';
import {
  DialogService,
  DynamicDialogConfig,
  DynamicDialogRef
} from 'primeng/dynamicdialog';
import {UUID} from '../../../../../types/uuid';
import {FuelDialogComponent} from '../../dialog/fuel-dialog/fuel-dialog.component';
export interface FuelConversionCriteria {
  criteria: string;
}
@Component({
  selector: 'app-fuel-conversion',
  templateUrl: './fuel-conversion.component.html',
  styleUrl: './fuel-conversion.component.css'
})
export class FuelConversionComponent
  extends SubscriptionAwareComponent
  implements OnInit
{
  @ViewChild('actionsTemplate', {static: true})
  actionsTemplate!: TemplateRef<any>;
  ref: DynamicDialogRef | undefined;
  protected fetchFuel!: (
    criteria: SearchCriteriaDto<FuelConversionCriteria>
  ) => Observable<SearchResultDto<FuelConversion>>;
  protected cols: TableTemplateColumn[] = [];
  protected readonly searchEvent: EventEmitter<void> = new EventEmitter();
  protected selected: FuelConversion[] = [];
  protected searchCriteria: FuelConversionCriteria = {criteria: ''};
  constructor(
    protected readonly applicationService: ApplicationService,
    private readonly fuelConversionService: FuelConversionService,
    private readonly messageService: MessageService,
    private readonly modalProvider: ModalProvider,
    private readonly translate: TranslateService,
    private readonly dialogService: DialogService
  ) {
    super();
  }

  ngOnInit(): void {
    this.buildCols();
    this.fetchFuel = this.fuelConversionService.getFuelConversion.bind(
      this.fuelConversionService
    );
  }
  buildCols(): void {
    this.cols.push({
      field: 'nameVN',
      header: 'Nhiên liệu'
    });
    this.cols.push({
      field: 'nameEN',
      header: 'Fuel'
    });
    this.cols.push({
      field: 'nameZH',
      header: '燃料'
    });
    this.cols.push({
      field: 'actions',
      header: '',
      templateRef: this.actionsTemplate
    });
  }

  onSelectionChange(selectedUsers: FuelConversion[]): void {
    this.selected = selectedUsers;
  }

  search(): void {
    this.searchEvent.emit();
  }

  openNewActivityDialog(fuelId?: UUID): void {
    // Made emissionId optional with ?
    const config: DynamicDialogConfig<UUID | undefined> = {
      // Allow undefined in config.data
      data: fuelId, // Will be undefined for add, UUID for edit
      closeOnEscape: true,
      dismissableMask: true,
      showHeader: false
    };

    // Clean up previous dialog if it exists
    if (this.ref) {
      this.ref.close();
    }

    // Open the dialog with EmissionSourceDialogComponent
    this.ref = this.dialogService.open(FuelDialogComponent, config);
    this.ref.onClose.subscribe((result: boolean | undefined) => {
      if (result) {
        this.searchEvent.emit(); // Refresh the list if dialog closed with a result
      }
    });
  }

  // Add a new emission source
  addFuel(): void {
    this.openNewActivityDialog(undefined); // Explicitly pass undefined for clarity
  }

  // Edit an existing emission source
  onEdit(rowData: FuelConversion): void {
    this.selected = [rowData];
    const fuelId = this.selected[0].id; // Fixed typo: emisstionId -> emissionId
    this.openNewActivityDialog(fuelId);
  }
  onDelete(rowData: FuelConversion): void {
    this.selected = [rowData];
  }
}
