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
import {TableTemplateColumn} from '../../../shared/components/table-template/table-template.component';
import {ApplicationService} from '../../../core/services/application.service';

import {MessageService} from 'primeng/api';
import {ModalProvider} from '../../../shared/services/modal-provider';
import {TranslateService} from '@ngx-translate/core';
import {
  EmissionFactorDTO,
  EmissionSourceDTO,
  FuelDTO
} from '../../../shared/models/shared-models';
import {EmissionFactorService} from '../../services/emission_factor.service';
export interface EmissionFactorCriteria {
  criteria: string;
}
@Component({
  selector: 'app-emission-factor',
  templateUrl: './emission-factor.component.html',
  styleUrl: './emission-factor.component.css'
})
export class EmissionFactorComponent
  extends SubscriptionAwareComponent
  implements OnInit
{
  @ViewChild('nameTemplate', {static: true})
  nameTemplate!: TemplateRef<any>;
  @ViewChild('emissionSourceTemplate', {static: true})
  emissionSourceTemplate!: TemplateRef<any>;
  @ViewChild('energyConversionTemplate', {static: true})
  energyConversionTemplate!: TemplateRef<any>;
  @ViewChild('actionsTemplate', {static: true})
  actionsTemplate!: TemplateRef<any>;
  @ViewChild('isDirectEmissionTemplate', {static: true})
  isDirectEmissionTemplate!: TemplateRef<any>; // New template reference
  protected fetchEmissionFactor!: (
    criteria: SearchCriteriaDto<EmissionFactorCriteria>
  ) => Observable<SearchResultDto<EmissionFactorDTO>>;
  protected cols: TableTemplateColumn[] = [];
  protected readonly searchEvent: EventEmitter<void> = new EventEmitter();
  protected selected: EmissionFactorDTO[] = [];
  protected searchCriteria: EmissionFactorCriteria = {criteria: ''};
  constructor(
    protected readonly applicationService: ApplicationService,
    private readonly emissionFactorService: EmissionFactorService,
    private readonly messageService: MessageService,
    private readonly modalProvider: ModalProvider,
    public readonly translate: TranslateService
  ) {
    super();
  }

  ngOnInit(): void {
    this.buildCols();
    this.fetchEmissionFactor =
      this.emissionFactorService.getEmissionFactor.bind(
        this.emissionFactorService
      );
  }

  getLocalizedEmissionSourceName(
    source: EmissionSourceDTO | undefined
  ): string {
    if (!source) return '';

    let lang = this.translate.currentLang.toUpperCase(); // Changed from const to let
    if (lang === 'VI') {
      lang = 'VN'; // Now this works because lang is mutable
    }

    return (
      (source[`name${lang}` as keyof EmissionSourceDTO] as string) ||
      source.nameEN
    );
  }

  getLocalizedFuelName(source: FuelDTO | undefined): string {
    if (!source) return '';

    let lang = this.translate.currentLang.toUpperCase(); // Changed from const to let
    if (lang === 'VI') {
      lang = 'VN'; // Now this works because lang is mutable
    }

    return (source[`name${lang}` as keyof FuelDTO] as string) || source.nameEN;
  }
  getLocalizedHeader(): string {
    const lang = this.translate.currentLang.toUpperCase();
    switch (lang) {
      case 'VI':
      case 'VN':
        return 'Hệ số phát thải';
      case 'EN':
        return 'Emission factor';
      case 'ZH':
        return '排放因子';
      default:
        return 'Emission factor'; // Mặc định là tiếng Anh
    }
  }

  // Lấy tên hệ số phát thải theo ngôn ngữ hiện tại
  getLocalizedEmissionFactorName(emissionFactor: EmissionFactorDTO): string {
    if (!emissionFactor) return '';

    let lang = this.translate.currentLang.toUpperCase();
    if (lang === 'VI') {
      lang = 'VN';
    }

    return (
      (emissionFactor[`name${lang}` as keyof EmissionFactorDTO] as string) ||
      emissionFactor.nameEN // Mặc định trả về tên tiếng Anh
    );
  }
  // eslint-disable-next-line max-lines-per-function
  buildCols(): void {
    this.cols.push({
      field: 'name',
      header: 'enterprise.emission.activity.dialog.EmissionFactor',
      templateRef: this.nameTemplate // Tham chiếu đến template mới
    });
    this.cols.push({
      field: 'co2',
      header: 'Co2'
    });
    this.cols.push({
      field: 'ch4',
      header: 'Ch4'
    });
    this.cols.push({
      field: 'n2o',
      header: 'N2o'
    });
    this.cols.push({
      field: 'emissionUnitNumerator',
      header: 'admin.emissionFactor.numerator'
    });
    this.cols.push({
      field: 'emissionUnitDenominator',
      header: 'admin.emissionFactor.denominator'
    });
    this.cols.push({
      field: 'emissionSourceDTO',
      header: 'admin.emissionSource.name',
      templateRef: this.emissionSourceTemplate
    });
    this.cols.push({
      field: 'description',
      header: 'admin.emissionFactor.description'
    });
    this.cols.push({
      field: 'validFrom',
      header: 'enterprise.buildings.details.labels.validFromInclusive'
    });
    this.cols.push({
      field: 'validTo',
      header: 'enterprise.buildings.details.labels.validToInclusive'
    });
    this.cols.push({
      field: 'isDirectEmission',
      header: 'admin.emissionFactor.isDirectEmission',
      templateRef: this.isDirectEmissionTemplate
    });
    this.cols.push({
      field: 'energyConversionDTO',
      header: 'sidebar.admin.fuel',
      templateRef: this.energyConversionTemplate
    });
    this.cols.push({
      field: 'actions',
      header: '',
      templateRef: this.actionsTemplate
    });
  }

  onSelectionChange(selectedUsers: EmissionFactorDTO[]): void {
    this.selected = selectedUsers;
  }

  search(): void {
    this.searchEvent.emit();
  }

  // Edit an existing emission source
  onEdit(rowData: EmissionFactorDTO): void {
    this.selected = [rowData];
  }
  onDelete(rowData: EmissionFactorDTO): void {
    this.selected = [rowData];
  }
}
