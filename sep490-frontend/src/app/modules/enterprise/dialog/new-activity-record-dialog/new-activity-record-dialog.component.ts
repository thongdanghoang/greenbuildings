import {HttpClient} from '@angular/common/http';
import {Component} from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  Validators
} from '@angular/forms';
import {TranslateService} from '@ngx-translate/core';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {takeUntil} from 'rxjs';
import {AbstractFormComponent} from '../../../shared/components/form/abstract-form-component';
import {
  EmissionFactorDTO,
  EmissionUnit
} from '../../../shared/models/shared-models';
import {ToastProvider} from '../../../shared/services/toast-provider';
import {UnitService} from '../../../shared/services/unit.service';
import {EmissionActivityRecord} from '../../models/enterprise.dto';
import {EmissionActivityRecordService} from '../../services/emission-activity-record.service';

export interface NewActivityRecordDialogConfig {
  activityId: string;
  factor: EmissionFactorDTO;
  editRecord?: EmissionActivityRecord;
}

@Component({
  selector: 'app-new-activity-record-dialog',
  templateUrl: './new-activity-record-dialog.component.html'
})
export class NewActivityRecordDialogComponent extends AbstractFormComponent<EmissionActivityRecord> {
  unitOptions: {label: string; value: EmissionUnit}[] = [];
  selectedFile: File | null = null;

  data?: NewActivityRecordDialogConfig;

  constructor(
    protected override readonly httpClient: HttpClient,
    protected override readonly formBuilder: FormBuilder,
    protected override readonly notificationService: ToastProvider,
    protected override readonly translate: TranslateService,
    private readonly unitService: UnitService,
    private readonly dialogRef: DynamicDialogRef,
    private readonly dialogConfig: DynamicDialogConfig<NewActivityRecordDialogConfig>,
    private readonly emissionActivityRecordService: EmissionActivityRecordService
  ) {
    super(httpClient, formBuilder, notificationService, translate);
    this.data = this.dialogConfig.data;
  }

  override initializeFormControls(): {[key: string]: AbstractControl} {
    return {
      activityId: new FormControl(this.data!.activityId, [Validators.required]),
      id: new FormControl(''),
      version: new FormControl(0),
      value: new FormControl(0, [Validators.required, Validators.min(0)]),
      unit: new FormControl('', [Validators.required]),
      startDate: new FormControl(new Date()),
      rangeDates: new FormControl(
        [],
        [Validators.required, Validators.min(2), Validators.max(2)]
      ),
      endDate: new FormControl(new Date())
    };
  }

  override initializeData(): void {
    this.initUnits();
    this.handleUpdate();
  }

  /* eslint-disable dot-notation */

  // eslint-disable-next-line max-lines-per-function
  override prepareDataBeforeSubmit(): void {
    if (
      this.formGroup.controls['rangeDates'].value.length !== 2 &&
      this.formGroup.controls['rangeDates'].value.length !== 1
    ) {
      this.formGroup.controls['rangeDates'].setErrors({
        invalid: true
      });
      return;
    }
    this.formGroup.controls['startDate'].setValue(
      this.formGroup.controls['rangeDates'].value[0]
    );
    if (this.formGroup.controls['rangeDates'].value[1]) {
      this.formGroup.controls['endDate'].setValue(
        this.formGroup.controls['rangeDates'].value[1]
      );
    } else {
      this.formGroup.controls['endDate'].setValue(
        this.formGroup.controls['rangeDates'].value[0]
      );
    }
    if (
      this.formGroup.controls['startDate'].value >
      this.formGroup.controls['endDate'].value
    ) {
      const temp = this.formGroup.controls['startDate'].value;
      this.formGroup.controls['startDate'].setValue(
        this.formGroup.controls['endDate'].value
      );
      this.formGroup.controls['endDate'].setValue(temp);
    }

    if (
      this.formGroup.controls['startDate'].value > Date.now() ||
      this.formGroup.controls['endDate'].value > Date.now()
    ) {
      this.notificationService.businessError({
        detail: this.translate.instant(
          'enterprise.emission.activity.record.dateWarning'
        ),
        summary: this.translate.instant('common.error.title')
      });
      this.formGroup.controls['rangeDates'].setValue([]);
    }
  }

  /* eslint-enable dot-notation */

  override submitForm(): void {
    if (this.formGroup.valid) {
      const formData = new FormData();

      // Add record data
      const recordData = this.formGroup.value;
      formData.append(
        'record',
        new Blob([JSON.stringify(recordData)], {type: 'application/json'})
      );

      // Add file if selected
      if (this.selectedFile) {
        formData.append('file', this.selectedFile);
      }

      // Submit both record and file
      this.httpClient
        .post(this.submitFormDataUrl(), formData)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: r => {
            this.onSubmitFormDataSuccess(r);
            this.enableSubmitBtn();
          },
          error: error => {
            this.displayFormResultErrors(error.error);
            this.onSubmitFormRequestError(error);
            this.enableSubmitBtn();
          }
        });
    }
  }

  override submitFormDataUrl(): string {
    return this.emissionActivityRecordService.newRecordURL;
  }

  /* eslint-disable dot-notation */

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  override onSubmitFormDataSuccess(result: any): void {
    this.notificationService.success({
      summary: this.translate.instant('common.success'),
      detail: this.translate.instant('common.saveSuccess')
    });
    this.dialogRef.close(true);
  }

  override onSubmitFormRequestError(error: any): void {
    if (error.error.field === 'rangeDates') {
      this.notificationService.businessError({
        summary: this.translate.instant('common.error.title'),
        detail: this.translate.instant(`validation.${error.error.i18nKey}`)
      });
    }
  }

  handleUpdate(): void {
    if (this.data?.editRecord) {
      this.formGroup.controls['startDate'].setValue(
        new Date(this.data.editRecord.startDate)
      );
      this.formGroup.controls['endDate'].setValue(
        new Date(this.data.editRecord.endDate)
      );
      this.formGroup.controls['rangeDates'].setValue([
        new Date(this.data.editRecord.startDate),
        new Date(this.data.editRecord.endDate)
      ]);
      this.formGroup.controls['value'].setValue(this.data.editRecord.value);
      this.formGroup.controls['unit'].setValue(this.data.editRecord.unit);
      this.formGroup.controls['id'].setValue(this.data.editRecord.id);
      this.formGroup.controls['version'].setValue(this.data.editRecord.version);
    }
  }

  onDownloadFile(): void {
    this.emissionActivityRecordService
      .getFileUrl(
        this.data?.editRecord!.id as string,
        this.data?.editRecord!.file.id as string
      )
      .subscribe((result: any) => {
        const url = result.url;
        window.open(url, '_blank'); // Open in a new tab
      });
  }

  removeFile(): void {
    //
  }

  /* eslint-enable dot-notation */

  onCancel(): void {
    this.dialogRef.close();
  }

  onFileSelect(event: any): void {
    const file: File = event.files[0];
    if (
      file.size > 5 * 1024 * 1024 ||
      (file.type !== 'application/pdf' && !file.type.startsWith('image/'))
    ) {
      this.notificationService.businessError({
        summary: this.translate.instant('common.error.title'),
        detail: this.translate.instant('common.error.fileSizeError')
      });
      return;
    }
    this.selectedFile = file;
  }

  getFactorName(): string {
    if (!this.data?.factor) {
      return '';
    } else if (this.translate.currentLang === 'en') {
      return this.data.factor.nameEN;
    } else if (this.translate.currentLang === 'zh') {
      return this.data.factor.nameZH;
    }
    return this.data.factor.nameVN;
  }

  getSourceName(): string {
    if (!this.data?.factor) {
      return '';
    } else if (this.translate.currentLang === 'en') {
      return this.data.factor.emissionSourceDTO.nameEN;
    } else if (this.translate.currentLang === 'zh') {
      return this.data.factor.emissionSourceDTO.nameZH;
    }
    return this.data.factor.emissionSourceDTO.nameVN;
  }

  initUnits(): void {
    let supportedUnits: EmissionUnit[] = [];

    if (this.data!.factor.directEmission) {
      supportedUnits = this.unitService.getSameUnitType(
        this.data!.factor.emissionUnitDenominator
      );
    } else {
      supportedUnits = this.unitService.getSameUnitType(
        this.data!.factor.energyConversionDTO!.conversionUnitDenominator
      );
    }
    this.unitOptions = supportedUnits.map(unit => ({
      label: this.translate.instant(`unit.${unit}`),
      value: unit
    }));
  }
}
