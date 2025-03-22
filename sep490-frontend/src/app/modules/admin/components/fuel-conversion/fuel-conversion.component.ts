import {Component, EventEmitter, OnInit} from '@angular/core';
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
    private readonly translate: TranslateService
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
  }

  onSelectionChange(selectedUsers: FuelConversion[]): void {
    this.selected = selectedUsers;
  }

  search(): void {
    this.searchEvent.emit();
  }
}
