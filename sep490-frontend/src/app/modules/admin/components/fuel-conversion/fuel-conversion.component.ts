import {Component, EventEmitter, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {DialogService, DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {Observable} from 'rxjs';
import {UUID} from '../../../../../types/uuid';
import {EnergyConversionDTO, FuelDTO} from '@models/shared-models';
import {ApplicationService} from '@services/application.service';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';
import {TableTemplateColumn} from '@shared/components/table-template/table-template.component';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';
import {FuelDialogComponent} from '../../dialog/fuel-dialog/fuel-dialog.component';
import {FuelConversionService} from '@services/fuel-conversion.service';

export interface FuelConversionCriteria {
  criteria: string;
}
@Component({
  selector: 'app-fuel-conversion',
  templateUrl: './fuel-conversion.component.html',
  styleUrl: './fuel-conversion.component.css'
})
export class FuelConversionComponent extends SubscriptionAwareComponent implements OnInit {
  @ViewChild('fuelTemplate', {static: true})
  fuelTemplate!: TemplateRef<any>;
  @ViewChild('actionsTemplate', {static: true})
  actionsTemplate!: TemplateRef<any>;
  ref: DynamicDialogRef | undefined;
  protected fetchFuel!: (
    criteria: SearchCriteriaDto<FuelConversionCriteria>
  ) => Observable<SearchResultDto<EnergyConversionDTO>>;
  protected cols: TableTemplateColumn[] = [];
  protected readonly searchEvent: EventEmitter<void> = new EventEmitter();
  protected selected: EnergyConversionDTO[] = [];
  protected searchCriteria: FuelConversionCriteria = {criteria: ''};
  constructor(
    protected readonly applicationService: ApplicationService,
    private readonly fuelConversionService: FuelConversionService,
    private readonly translate: TranslateService,
    private readonly dialogService: DialogService
  ) {
    super();
  }

  ngOnInit(): void {
    this.buildCols();
    this.fetchFuel = this.fuelConversionService.getFuelConversion.bind(this.fuelConversionService);
  }

  getLocalizedFuelName(source: FuelDTO | undefined): string {
    if (!source) return '';

    let lang = this.translate.currentLang.toUpperCase(); // Changed from const to let
    if (lang === 'VI') {
      lang = 'VN'; // Now this works because lang is mutable
    }

    return (source[`name${lang}` as keyof FuelDTO] as string) || source.nameEN;
  }

  buildCols(): void {
    this.cols.push({
      field: 'fuel',
      header: 'sidebar.admin.fuel',
      templateRef: this.fuelTemplate
    });
    this.cols.push({
      field: 'conversionValue',
      header: 'admin.energyConversion.conversionValue'
    });
    this.cols.push({
      field: 'conversionUnitNumerator',
      header: 'admin.energyConversion.conversionUnitNumerator'
    });
    this.cols.push({
      field: 'conversionUnitDenominator',
      header: 'admin.energyConversion.conversionUnitDenominator'
    });
    this.cols.push({
      field: 'actions',
      header: '',
      templateRef: this.actionsTemplate
    });
  }

  onSelectionChange(selectedUsers: EnergyConversionDTO[]): void {
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
  onEdit(rowData: EnergyConversionDTO): void {
    this.selected = [rowData];
    const fuelId = this.selected[0].id; // Fixed typo: emisstionId -> emissionId
    this.openNewActivityDialog(fuelId);
  }
}
