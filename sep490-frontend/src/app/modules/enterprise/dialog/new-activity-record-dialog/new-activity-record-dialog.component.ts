import {Component} from '@angular/core';
import {AbstractControl, FormControl, Validators} from '@angular/forms';
import {DateRangeView} from '@generated/models/date-range-view';
import {EmissionActivityRecord} from '@models/enterprise';
import {EmissionFactorDTO, EmissionUnit} from '@models/shared-models';
import {AssetService} from '@services/asset.service';
import {EmissionActivityRecordService} from '@services/emission-activity-record.service';
import {EmissionActivityService} from '@services/emission-activity.service';
import {FuelConversionService} from '@services/fuel-conversion.service';
import {UnitService} from '@services/unit.service';
import {AbstractFormComponent} from '@shared/components/form/abstract-form-component';
import {SelectableItem} from '@shared/models/base-models';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {takeUntil} from 'rxjs';
import {UUID} from '../../../../../types/uuid';

export interface NewActivityRecordDialogConfig {
  activityId: UUID;
  buildingId?: UUID;
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
  maxDate: Date = new Date();
  disabledDates: Date[] = [];
  formStructure = {
    activityId: new FormControl<null | UUID>(null, [Validators.required]),
    assetId: new FormControl<null | UUID>(null),
    id: new FormControl<null | UUID>(null),
    version: new FormControl(0),
    value: new FormControl(0, [Validators.required, Validators.min(0)]),
    unit: new FormControl<null | string>(null, [Validators.required]),
    quantity: new FormControl(1, {nonNullable: true, validators: [Validators.required, Validators.min(1)]}),
    startDate: new FormControl(new Date()),
    rangeDates: new FormControl([new Date(), new Date()], [Validators.required, Validators.min(2), Validators.max(2)]),
    endDate: new FormControl(new Date())
  };

  data?: NewActivityRecordDialogConfig;

  selectableAssets: SelectableItem<UUID>[] = [];

  constructor(
    private readonly unitService: UnitService,
    private readonly dialogRef: DynamicDialogRef,
    private readonly energyService: FuelConversionService,
    private readonly dialogConfig: DynamicDialogConfig<NewActivityRecordDialogConfig>,
    private readonly emissionActivityRecordService: EmissionActivityRecordService,
    private readonly assetService: AssetService,
    private readonly emissionActivityService: EmissionActivityService
  ) {
    super();
    this.data = this.dialogConfig.data;
  }

  override initializeFormControls(): {[key: string]: AbstractControl} {
    return this.formStructure;
  }

  override initializeData(): void {
    this.initUnits();
    this.handleUpdate();
    this.initFormData();
    if (this.data?.activityId) {
      this.getRecordedDateRanges(this.data.activityId, this.data?.editRecord?.id, this.data?.editRecord?.assetId);
    }
    this.formStructure.assetId.valueChanges.pipe(takeUntil(this.destroy$)).subscribe(assetId => {
      if (assetId) {
        this.formStructure.quantity.setValue(1);
        this.formStructure.quantity.disable();
      } else {
        this.formStructure.quantity.enable();
      }
      if (this.data?.activityId) {
        this.getRecordedDateRanges(this.data.activityId, this.data?.editRecord?.id, assetId as UUID);
      }
    });
    this.assetService
      .selectable(this.data?.buildingId, this.data?.editRecord?.assetId)
      .pipe(takeUntil(this.destroy$))
      .subscribe(assets => {
        this.selectableAssets = assets;

        if (this.formGroupDisable) {
          this.formGroup.disable();
        }
      });
  }

  get formGroupDisable(): boolean {
    const assetId = this.data?.editRecord?.assetId;
    return !!(assetId && this.selectableAssets.filter(asset => asset.value === assetId)[0].disabled);
  }

  getRecordedDateRanges(activityId: UUID, excludeRecordId?: UUID, assetId?: UUID): void {
    this.emissionActivityService
      .getRecordedDateRanges(activityId, excludeRecordId, assetId)
      .subscribe(
        recordedDateRanges => (this.disabledDates = this.convertDateRangesToDisabledDates(recordedDateRanges))
      );
  }

  override prepareDataBeforeSubmit(): void {
    const {rangeDates, startDate, endDate} = this.formGroup.controls;
    if (rangeDates.value.length !== 2 && rangeDates.value.length !== 1) {
      rangeDates.setErrors({
        invalid: true
      });
      return;
    }
    startDate.setValue(rangeDates.value[0]);
    if (rangeDates.value[1]) {
      endDate.setValue(rangeDates.value[1]);
    } else {
      endDate.setValue(rangeDates.value[0]);
    }
    if (startDate.value > endDate.value) {
      const temp = startDate.value;
      startDate.setValue(endDate.value);
      endDate.setValue(temp);
    }
    if (startDate.value > Date.now() || endDate.value > Date.now()) {
      this.notificationService.businessError({
        detail: this.translate.instant('enterprise.emission.activity.record.dateWarning'),
        summary: this.translate.instant('common.error.title')
      });
      rangeDates.setValue([]);
    }
  }

  override submitForm(): void {
    if (this.formGroup.valid) {
      const formData = new FormData();
      const recordData = {
        ...this.getSubmitFormData(),
        startDate: this.convertDateToUTC(this.formGroup.value.startDate),
        endDate: this.convertDateToUTC(this.formGroup.value.endDate)
      };
      formData.append('record', new Blob([JSON.stringify(recordData)], {type: 'application/json'}));
      if (this.selectedFile) {
        formData.append('file', this.selectedFile);
      }
      this.emissionActivityRecordService
        .submitRecord(formData)
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
  override onSubmitFormDataSuccess(_result: any): void {
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

  initFormData(): void {
    if (this.data && this.formStructure) {
      this.formStructure.activityId.setValue(this.data.activityId);
    }
  }

  handleUpdate(): void {
    if (this.data?.editRecord) {
      this.formStructure.startDate.setValue(new Date(this.data.editRecord.startDate));
      this.formStructure.endDate.setValue(new Date(this.data.editRecord.endDate));
      this.formStructure.rangeDates.setValue([
        new Date(this.data.editRecord.startDate),
        new Date(this.data.editRecord.endDate)
      ]);
      this.formStructure.value.setValue(this.data.editRecord.value);
      this.formStructure.unit.setValue(this.data.editRecord.unit);
      this.formStructure.quantity.setValue(this.data.editRecord.quantity);
      if (this.data.editRecord.assetId) {
        this.formStructure.assetId.setValue(this.data.editRecord.assetId);
        this.formStructure.quantity.disable();
      }
      this.formStructure.id.setValue(this.data.editRecord.id);
      this.formStructure.version.setValue(this.data.editRecord.version);
    }
  }

  onDownloadFile(): void {
    this.emissionActivityRecordService
      .getFile(this.data?.editRecord!.id as string, this.data?.editRecord!.file.id as string)
      .subscribe((fileData: Blob) => {
        const link = document.createElement('a');
        link.href = URL.createObjectURL(fileData);
        link.download = this.data?.editRecord!.file.fileName as string;
        link.click();
        URL.revokeObjectURL(link.href);
      });
  }

  /* eslint-enable dot-notation */

  onCancel(): void {
    this.dialogRef.close();
  }

  onFileSelect(event: any): void {
    const file: File = event.files[0];
    if (file.size > 5 * 1024 * 1024 || (file.type !== 'application/pdf' && !file.type.startsWith('image/'))) {
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

  private initUnits(): void {
    if (this.data!.factor.directEmission) {
      this.populateUnitOptions(this.data!.factor.emissionUnitDenominator);
    } else {
      if (this.data?.factor.energyConversionDTO) {
        this.populateUnitOptions(this.data.factor.energyConversionDTO.conversionUnitDenominator);
      } else {
        this.energyService.getFuelConversionByFactorId(this.data!.factor.id).subscribe(rs => {
          this.data!.factor.energyConversionDTO = rs;
          this.populateUnitOptions(this.data!.factor.energyConversionDTO.conversionUnitDenominator);
        });
      }
    }
  }

  private populateUnitOptions(unit: EmissionUnit): void {
    const supportedUnits = this.unitService.getSameUnitType(unit);
    this.unitOptions = supportedUnits.map(unit => ({
      label: this.translate.instant(`unit.${unit}`),
      value: unit
    }));
  }

  private convertDateToUTC(date: Date): Date {
    const year = date.getFullYear();
    const month = date.getMonth();
    const day = date.getDate();
    return new Date(Date.UTC(year, month, day));
  }

  private convertDateRangesToDisabledDates(recordedDateRanges: DateRangeView[]): Date[] {
    const disabledDates: Date[] = [];
    for (const range of recordedDateRanges) {
      if (!range.fromInclusive || !range.toInclusive) {
        continue;
      }

      const start = new Date(range.fromInclusive);
      const end = new Date(range.toInclusive);

      const currentDate = new Date(start);
      while (currentDate <= end) {
        disabledDates.push(new Date(currentDate.getTime()));
        currentDate.setDate(currentDate.getDate() + 1);
      }
    }
    return disabledDates;
  }
}
