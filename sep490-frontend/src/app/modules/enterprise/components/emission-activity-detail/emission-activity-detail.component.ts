import {Component, EventEmitter, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {AbstractControl, FormControl, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {EmissionActivityRecordCriteria} from '@models/emission-activity-record';
import {ActivityType, EmissionActivityDetails, EmissionActivityRecord} from '@models/enterprise';
import {ActivityTypeService} from '@services/activity-type.service';
import {ApplicationService} from '@services/application.service';
import {EmissionActivityRecordService} from '@services/emission-activity-record.service';
import {EmissionActivityService} from '@services/emission-activity.service';
import {AbstractFormComponent} from '@shared/components/form/abstract-form-component';
import {TableTemplateColumn} from '@shared/components/table-template/table-template.component';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';
import {ModalProvider} from '@shared/services/modal-provider';
import {Observable, Observer, filter, map, of, switchMap, takeUntil} from 'rxjs';
import {validate} from 'uuid';
import {UUID} from '../../../../../types/uuid';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {NewActivityRecordDialogComponent} from '../../dialog/new-activity-record-dialog/new-activity-record-dialog.component';

@Component({
  selector: 'app-emission-activity-detail',
  templateUrl: './emission-activity-detail.component.html',
  styleUrl: './emission-activity-detail.component.css'
})
export class EmissionActivityDetailComponent extends AbstractFormComponent<EmissionActivityDetails> implements OnInit {
  formStructure = {
    id: new FormControl<null | UUID>(null, Validators.required),
    version: new FormControl(0, Validators.required),
    name: new FormControl<null | string>(null, [Validators.required]),
    buildingId: new FormControl<null | UUID>(null, [Validators.required]),
    buildingGroupID: new FormControl<null | UUID>(null),
    emissionFactorID: new FormControl<null | UUID>(null, [Validators.required]),
    type: new FormControl(),
    category: new FormControl<null | string>(null, [Validators.required]),
    description: new FormControl<null | string>(null),
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
  @ViewChild('unitTemplate', {static: true})
  unitTemplate!: TemplateRef<any>;
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
        void this.router.navigate([AppRoutingConstants.ENTERPRISE_PATH, AppRoutingConstants.BUILDING_PATH]);
      } else {
        this.handleAfterSuccessValidation(activity);
      }
    },
    error: () => {
      void this.router.navigate([AppRoutingConstants.ENTERPRISE_PATH, AppRoutingConstants.BUILDING_PATH]);
    },
    complete: () => {}
  };

  constructor(
    protected readonly applicationService: ApplicationService,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly emissionActivityService: EmissionActivityService,
    private readonly emissionActivityRecordService: EmissionActivityRecordService,
    private readonly activityTypeService: ActivityTypeService,
    private readonly modalProvider: ModalProvider
  ) {
    super();
  }

  override ngOnInit(): void {
    this.fetchRecords = this.emissionActivityRecordService.search.bind(this.emissionActivityRecordService);
    this.validateAndFetchDetail();
    super.ngOnInit();
  }

  updateFormStructureData(): void {
    this.formStructure.id.setValue(this.activity.id);
    this.formStructure.version.setValue(this.activity.version);
    this.formStructure.buildingId.setValue(this.activity.buildingId);
    this.formStructure.buildingGroupID.setValue(this.activity.buildingGroup?.id);
    this.formStructure.emissionFactorID.setValue(this.activity.emissionFactor.id);
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
        sortable: true,
        templateRef: this.unitTemplate
      },
      {
        field: 'quantity',
        header: 'enterprise.emission.activity.record.table.quantity',
        sortable: true
      },
      {
        field: 'ghg',
        header: 'enterprise.emission.activity.record.table.ghg',
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
    this.modalProvider
      .showDefaultConfirm()
      .pipe(
        takeUntil(this.destroy$),
        switchMap((result: boolean) => {
          if (!result) {
            return of(null); // Skip deletion if modal is cancelled
          }
          return this.emissionActivityRecordService.deleteRecords(ids).pipe(
            switchMap(() => {
              this.notificationService.success({
                summary: this.translate.instant('common.success')
              });
              this.selected = []; // Clear local selection
              this.searchEvent.emit(); // Refresh table
              this.clearSelectedEvent.emit();
              return of(null); // Complete the stream
            })
          );
        })
      )
      .subscribe();
  }

  onDownloadFile(record: EmissionActivityRecord): void {
    this.emissionActivityRecordService
      .getFile(record.id as string, record.file.id as string)
      .subscribe((fileData: Blob) => {
        const link = document.createElement('a');
        link.href = URL.createObjectURL(fileData);
        link.download = record.file.fileName;
        link.click();
        URL.revokeObjectURL(link.href);
      });
  }

  removeFile(record: EmissionActivityRecord): void {
    this.modalProvider
      .showDefaultConfirm()
      .pipe(
        takeUntil(this.destroy$),
        switchMap((result: boolean) => {
          if (!result) {
            return of(null);
          }
          return this.emissionActivityRecordService.deleteRecordFile(record.id, record.file.id).pipe(
            switchMap(() => {
              this.notificationService.success({
                summary: this.translate.instant('common.success')
              });
              this.searchEvent.emit();
              return of(null);
            })
          );
        })
      )
      .subscribe();
  }

  onNewRecord(): void {
    this.modalProvider
      .openDynamicDialog(NewActivityRecordDialogComponent, {
        buildingId: this.activity.buildingId,
        activityId: this.activity.id,
        factor: this.activity.emissionFactor
      })
      .subscribe(result => {
        if (result) {
          this.searchEvent.emit();
        }
      });
  }

  openEditRecordDialog(record: EmissionActivityRecord): void {
    this.modalProvider
      .openDynamicDialog(NewActivityRecordDialogComponent, {
        buildingId: this.activity.buildingId,
        activityId: this.activity.id,
        factor: this.activity.emissionFactor,
        editRecord: record
      })
      .subscribe(result => {
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
        switchMap(id => this.emissionActivityService.getActivityDetails(id as UUID))
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

  protected override onSubmitFormDataSuccess(result: EmissionActivityDetails): void {
    this.activity = result;
    this.loadActivityTypes();
    this.updateFormStructureData();
  }

  private loadActivityTypes(): void {
    this.activityTypeService.getByBuildingId(this.activity.buildingId).subscribe(types => {
      this.activityTypes = types;
    });
  }
}
