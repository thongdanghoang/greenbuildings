import {Component, EventEmitter, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Observable, Observer, filter, map, switchMap, takeUntil} from 'rxjs';
import {validate} from 'uuid';
import {UUID} from '../../../../../types/uuid';
import {
  NewActivityRecordDialogComponent,
  NewActivityRecordDialogConfig
} from '../../dialog/new-activity-record-dialog/new-activity-record-dialog.component';
import {EmissionActivityService} from '../../services/emission-activity.service';
import {
  EmissionActivityRecordCriteria,
  EmissionActivityRecordService
} from '../../services/emission-activity-record.service';
import {
  EmissionActivityDetails,
  EmissionActivityRecord
} from '../../models/enterprise.dto';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {MessageService} from 'primeng/api';
import {TranslateService} from '@ngx-translate/core';
import {ApplicationService} from '../../../core/services/application.service';
import {
  SearchCriteriaDto,
  SearchResultDto
} from '../../../shared/models/base-models';
import {TableTemplateColumn} from '../../../shared/components/table-template/table-template.component';
import {AbstractFormComponent} from '../../../shared/components/form/abstract-form-component';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  Validators
} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {DialogService, DynamicDialogConfig} from 'primeng/dynamicdialog';

@Component({
  selector: 'app-emission-activity-detail',
  templateUrl: './emission-activity-detail.component.html',
  styleUrls: ['./emission-activity-detail.component.scss']
})
export class EmissionActivityDetailComponent
  extends AbstractFormComponent<EmissionActivityDetails>
  implements OnInit
{
  formStructure = {
    id: new FormControl('', Validators.required),
    version: new FormControl(0, Validators.required),
    name: new FormControl('', [Validators.required]),
    buildingID: new FormControl('', [Validators.required]),
    emissionFactorID: new FormControl('', [Validators.required]),
    type: new FormControl(''),
    category: new FormControl(''),
    quantity: new FormControl(0, [Validators.required, Validators.min(0)]),
    description: new FormControl(''),
    records: new FormControl([])
  };

  activity!: EmissionActivityDetails;
  selected: EmissionActivityRecord[] = [];
  searchCriteria: EmissionActivityRecordCriteria = {
    emissionActivityId: '' as UUID
  };
  searchEvent = new EventEmitter<void>();
  clearSelectedEvent = new EventEmitter<void>();
  fetchRecords!: (
    criteria: SearchCriteriaDto<EmissionActivityRecordCriteria>
  ) => Observable<SearchResultDto<EmissionActivityRecord>>;

  cols: TableTemplateColumn[] = [];

  private readonly fetchActivityObserver: Observer<EmissionActivityDetails> = {
    next: (activity: EmissionActivityDetails) => {
      if (!activity) {
        this.notificationService.add({
          severity: 'error',
          summary: this.translate.instant('http.error.status.403.title'),
          life: 3000,
          sticky: true
        });
        void this.router.navigate([
          AppRoutingConstants.ENTERPRISE_PATH,
          AppRoutingConstants.BUILDING_PATH
        ]);
      } else {
        this.handleAfterSuccessValidation(activity);
      }
    },
    error: () => {
      void this.router.navigate([
        AppRoutingConstants.ENTERPRISE_PATH,
        AppRoutingConstants.BUILDING_PATH
      ]);
    },
    complete: () => {}
  };

  constructor(
    protected override readonly httpClient: HttpClient,
    protected override readonly formBuilder: FormBuilder,
    protected override readonly notificationService: MessageService,
    protected override readonly translate: TranslateService,
    protected readonly applicationService: ApplicationService,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly emissionActivityService: EmissionActivityService,
    private readonly emissionActivityRecordService: EmissionActivityRecordService,
    private readonly dialogService: DialogService
  ) {
    super(httpClient, formBuilder, notificationService, translate);
  }

  updateFormStructureData(): void {
    this.formStructure.id.setValue(this.activity.id.toString());
    this.formStructure.version.setValue(this.activity.version);
    this.formStructure.buildingID.setValue(
      this.activity.building.id.toString()
    );
    this.formStructure.emissionFactorID.setValue(
      this.activity.emissionFactor.id.toString()
    );
    this.formStructure.name.setValue(this.activity.name);
    this.formStructure.type.setValue(this.activity.type);
    this.formStructure.category.setValue(this.activity.category);
    this.formStructure.quantity.setValue(this.activity.quantity);
    this.formStructure.description.setValue(this.activity.description);
  }

  override ngOnInit(): void {
    this.fetchRecords = this.emissionActivityRecordService.search.bind(
      this.emissionActivityRecordService
    );
    this.validateAndFetchDetail();
    super.ngOnInit();
  }

  handleAfterSuccessValidation(activity: EmissionActivityDetails): void {
    this.activity = activity;
    this.searchCriteria.emissionActivityId = activity.id;
    this.searchEvent.emit();
    this.buildCols();
    this.updateFormStructureData();
  }

  buildCols(): void {
    this.cols = [
      {
        field: 'value',
        header: 'enterprise.emission.activity.record.table.value',
        sortable: true
      },
      {
        field: 'unit',
        header: 'enterprise.emission.activity.record.table.unit',
        sortable: true
      },
      {
        field: 'startDate',
        header: 'enterprise.emission.activity.record.table.startDate',
        sortable: true
      },
      {
        field: 'endDate',
        header: 'enterprise.emission.activity.record.table.endDate',
        sortable: true
      }
    ];
  }

  onSelectionChange(selection: EmissionActivityRecord[]): void {
    this.selected = selection;
  }

  confirmDelete(): void {
    const ids: UUID[] = this.selected.map(record => record.id);

    this.emissionActivityRecordService.deleteRecords(ids).subscribe({
      next: () => {
        this.notificationService.add({
          severity: 'success',
          summary: this.translate.instant('common.success')
        });
        this.selected = []; // Clear local selection
        this.searchEvent.emit(); // Refresh table
        this.clearSelectedEvent.emit();
      }
    });
  }

  onBack(): void {
    void this.router.navigate([
      AppRoutingConstants.ENTERPRISE_PATH,
      AppRoutingConstants.EMISSION_ACTIVITY_PATH,
      this.activity.building.id
    ]);
  }

  onNewRecord(): void {
    const config: DynamicDialogConfig<NewActivityRecordDialogConfig> = {
      data: {
        activityId: this.activity.id.toString(),
        factor: this.activity.emissionFactor
      },
      closeOnEscape: true,
      dismissableMask: true,
      showHeader: false
    };
    const ref = this.dialogService.open(
      NewActivityRecordDialogComponent,
      config
    );
    ref.onClose.subscribe(result => {
      if (result) {
        this.searchEvent.emit();
      }
    });
  }

  validateAndFetchDetail(): void {
    this.route.paramMap
      .pipe(
        takeUntil(this.destroy$),
        map(params => params.get('emissionId')),
        filter((idParam): idParam is string => !!idParam),
        filter(id => validate(id)),
        switchMap(id =>
          this.emissionActivityService.getActivityDetails(id as UUID)
        )
      )
      .subscribe(this.fetchActivityObserver);
  }

  getFactorName(): string {
    if (!this.activity) {
      return '';
    } else if (this.translate.currentLang === 'en') {
      return this.activity.emissionFactor.nameEN;
    } else if (this.translate.currentLang === 'zh') {
      return this.activity.emissionFactor.nameZH;
    }
    return this.activity.emissionFactor.nameVN;
  }

  getSourceName(): string {
    if (!this.activity) {
      return '';
    } else if (this.translate.currentLang === 'en') {
      return this.activity.emissionFactor.emissionSourceDTO.nameEN;
    } else if (this.translate.currentLang === 'zh') {
      return this.activity.emissionFactor.emissionSourceDTO.nameZH;
    }
    return this.activity.emissionFactor.emissionSourceDTO.nameVN;
  }

  disableUpdateBtn(): boolean {
    return this.formGroup.pristine || !this.formGroup.valid;
  }

  protected override initializeFormControls(): {
    [key: string]: AbstractControl;
  } {
    return this.formStructure;
  }

  protected override initializeData(): void {}

  protected override submitFormDataUrl(): string {
    return this.emissionActivityService.getCreateNewActivityURL();
  }

  protected override onSubmitFormDataSuccess(
    result: EmissionActivityDetails
  ): void {
    this.activity = result;
    this.updateFormStructureData();
  }
}
