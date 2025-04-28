import {HttpClient} from '@angular/common/http';
import {
  Component,
  EventEmitter,
  OnInit,
  TemplateRef,
  ViewChild
} from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  Validators
} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {EmissionActivityRecordCriteria} from '@models/emission-activity-record';
import {TranslateService} from '@ngx-translate/core';
import {DialogService, DynamicDialogConfig} from 'primeng/dynamicdialog';
import {Observable, Observer, filter, map, switchMap, takeUntil} from 'rxjs';
import {validate} from 'uuid';
import {UUID} from '../../../../../types/uuid';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {
  ActivityType,
  EmissionActivityDetails,
  EmissionActivityRecord
} from '@models/enterprise';
import {ApplicationService} from '@services/application.service';
import {AbstractFormComponent} from '@shared/components/form/abstract-form-component';
import {TableTemplateColumn} from '@shared/components/table-template/table-template.component';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';
import {ToastProvider} from '@shared/services/toast-provider';
import {
  NewActivityRecordDialogComponent,
  NewActivityRecordDialogConfig
} from '../../dialog/new-activity-record-dialog/new-activity-record-dialog.component';
import {ActivityTypeService} from '@services/activity-type.service';
import {EmissionActivityRecordService} from '@services/emission-activity-record.service';
import {EmissionActivityService} from '@services/emission-activity.service';

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
    buildingId: new FormControl<null | UUID>(null, [Validators.required]),
    buildingGroupId: new FormControl<null | UUID>(null),
    emissionFactorID: new FormControl<null | UUID>(null, [Validators.required]),
    type: new FormControl(),
    category: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required]
    }),
    description: new FormControl(''),
    records: new FormControl([])
  };

  activity!: EmissionActivityDetails;
  selected: EmissionActivityRecord[] = [];
  activityTypes: ActivityType[] = [];
  searchCriteria: EmissionActivityRecordCriteria = {
    emissionActivityId: '' as UUID
  };
  @ViewChild('fileTemplate', {static: true})
  fileTemplate!: TemplateRef<any>;
  @ViewChild('actionsTemplate', {static: true})
  actionsTemplate!: TemplateRef<any>;
  @ViewChild('ghgTemplate', {static: true})
  ghgTemplate!: TemplateRef<any>;
  searchEvent = new EventEmitter<void>();
  clearSelectedEvent = new EventEmitter<void>();
  fetchRecords!: (
    criteria: SearchCriteriaDto<EmissionActivityRecordCriteria>
  ) => Observable<SearchResultDto<EmissionActivityRecord>>;

  cols: TableTemplateColumn[] = [];

  private readonly fetchActivityObserver: Observer<EmissionActivityDetails> = {
    next: (activity: EmissionActivityDetails) => {
      if (!activity) {
        this.notificationService.businessError({
          summary: this.translate.instant('http.error.status.403.title')
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
    protected override readonly notificationService: ToastProvider,
    protected override readonly translate: TranslateService,
    protected readonly applicationService: ApplicationService,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly emissionActivityService: EmissionActivityService,
    private readonly emissionActivityRecordService: EmissionActivityRecordService,
    private readonly dialogService: DialogService,
    private readonly activityTypeService: ActivityTypeService
  ) {
    super(httpClient, formBuilder, notificationService, translate);
  }

  override ngOnInit(): void {
    this.fetchRecords = this.emissionActivityRecordService.search.bind(
      this.emissionActivityRecordService
    );
    this.validateAndFetchDetail();
    super.ngOnInit();
  }

  updateFormStructureData(): void {
    this.formStructure.id.setValue(this.activity.id.toString());
    this.formStructure.version.setValue(this.activity.version);
    this.formStructure.buildingId.setValue(this.activity.buildingId);
    this.formStructure.buildingGroupId.setValue(
      this.activity.buildingGroup?.id
    );
    this.formStructure.emissionFactorID.setValue(
      this.activity.emissionFactor.id
    );
    this.formStructure.name.setValue(this.activity.name);
    if (this.activity.type) {
      this.formStructure.type.setValue(this.activity?.type.id);
    }
    this.formStructure.category.setValue(this.activity.category);
    this.formStructure.description.setValue(this.activity.description);
  }

  handleAfterSuccessValidation(activity: EmissionActivityDetails): void {
    this.activity = activity;
    this.searchCriteria.emissionActivityId = activity.id;
    this.searchEvent.emit();
    this.buildCols();
    this.loadActivityTypes();
    this.updateFormStructureData();
  }

  // eslint-disable-next-line max-lines-per-function
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
        field: 'quantity',
        header: 'enterprise.emission.activity.record.table.quantity',
        sortable: true
      },
      {
        field: 'ghg',
        header: 'enterprise.emission.activity.record.table.ghg',
        sortable: true,
        sortField: 'quantity',
        templateRef: this.ghgTemplate
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
      },
      {
        field: 'file',
        header: 'enterprise.emission.activity.record.table.file.title',
        sortable: false,
        templateRef: this.fileTemplate
      },
      {
        field: 'actions',
        header: '',
        templateRef: this.actionsTemplate
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
        this.notificationService.success({
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
      AppRoutingConstants.BUILDING_GROUP_PATH,
      this.activity.buildingGroup.id
    ]);
  }

  onDownloadFile(record: EmissionActivityRecord): void {
    this.emissionActivityRecordService
      .getFileUrl(record.id as string, record.file.id as string)
      .subscribe((result: any) => {
        const url = result.url;
        window.open(url, '_blank'); // Open in a new tab
      });
  }

  removeFile(record: EmissionActivityRecord): void {
    this.emissionActivityRecordService
      .deleteRecordFile(record.id, record.file.id)
      .subscribe({
        next: () => {
          this.notificationService.success({
            summary: this.translate.instant('common.success')
          });
          this.searchEvent.emit();
        }
      });
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

  openEditRecordDialog(record: EmissionActivityRecord): void {
    const config: DynamicDialogConfig<NewActivityRecordDialogConfig> = {
      data: {
        activityId: this.activity.id.toString(),
        factor: this.activity.emissionFactor,
        editRecord: record
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
    this.loadActivityTypes();
    this.updateFormStructureData();
  }

  private loadActivityTypes(): void {
    this.applicationService.UserData.pipe(takeUntil(this.destroy$)).subscribe(
      u => {
        this.activityTypeService
          .getByEnterpriseId(u.enterpriseId)
          .subscribe(types => {
            this.activityTypes = types;
          });
      }
    );
  }
}
