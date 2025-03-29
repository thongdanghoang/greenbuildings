import {
  Component,
  EventEmitter,
  OnInit,
  TemplateRef,
  ViewChild
} from '@angular/core';
import {SubscriptionAwareComponent} from '../../../core/subscription-aware.component';
import {ApplicationService} from '../../../core/services/application.service';
import {MessageService} from 'primeng/api';
import {ModalProvider} from '../../../shared/services/modal-provider';
import {TranslateService} from '@ngx-translate/core';
import {TableTemplateColumn} from '../../../shared/components/table-template/table-template.component';
import {EmissionSource} from '../../../enterprise/models/enterprise.dto';
import {
  SearchCriteriaDto,
  SearchResultDto
} from '../../../shared/models/base-models';
import {Observable} from 'rxjs';
import {EmissionSourceService} from '../../services/emission-source.service';
import {
  DialogService,
  DynamicDialogConfig,
  DynamicDialogRef
} from 'primeng/dynamicdialog';
import {UUID} from '../../../../../types/uuid';
import {EmissionSourceDialogComponent} from '../../dialog/emission-source-dialog/emission-source-dialog.component';
export interface EmissionSourceCriteria {
  criteria: string;
}
@Component({
  selector: 'app-emission-source',
  templateUrl: './emission-source.component.html',
  styleUrl: './emission-source.component.css'
})
export class EmissionSourceComponent
  extends SubscriptionAwareComponent
  implements OnInit
{
  @ViewChild('actionsTemplate', {static: true})
  actionsTemplate!: TemplateRef<any>;
  ref: DynamicDialogRef | undefined;
  protected fetchEmissionSource!: (
    criteria: SearchCriteriaDto<EmissionSourceCriteria>
  ) => Observable<SearchResultDto<EmissionSource>>;
  protected cols: TableTemplateColumn[] = [];
  protected readonly searchEvent: EventEmitter<void> = new EventEmitter();
  protected selected: EmissionSource[] = [];
  protected searchCriteria: EmissionSourceCriteria = {criteria: ''};
  constructor(
    protected readonly applicationService: ApplicationService,
    private readonly emissionSourceService: EmissionSourceService,
    private readonly messageService: MessageService,
    private readonly modalProvider: ModalProvider,
    private readonly translate: TranslateService,
    private readonly dialogService: DialogService
  ) {
    super();
  }

  ngOnInit(): void {
    this.buildCols();
    this.fetchEmissionSource =
      this.emissionSourceService.getEmissionSource.bind(
        this.emissionSourceService
      );
  }
  buildCols(): void {
    this.cols.push({
      field: 'nameVN',
      header: 'Nguồn phát thải'
    });
    this.cols.push({
      field: 'nameEN',
      header: 'Emission source'
    });
    this.cols.push({
      field: 'nameZH',
      header: '排放源'
    });
    this.cols.push({
      field: 'actions',
      header: '',
      templateRef: this.actionsTemplate
    });
  }

  onSelectionChange(selectedUsers: EmissionSource[]): void {
    this.selected = selectedUsers;
  }

  search(): void {
    this.searchEvent.emit();
  }

  openNewActivityDialog(emissionId?: UUID): void {
    // Made emissionId optional with ?
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
    this.ref = this.dialogService.open(EmissionSourceDialogComponent, config);
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
  onEdit(rowData: EmissionSource): void {
    this.selected = [rowData];
    const emissionId = this.selected[0].id; // Fixed typo: emisstionId -> emissionId
    this.openNewActivityDialog(emissionId);
  }
  onDelete(rowData: EmissionSource): void {
    this.selected = [rowData];
  }
}
