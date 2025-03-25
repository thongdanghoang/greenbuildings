import {Component} from '@angular/core';
import {AbstractFormComponent} from '../../../shared/components/form/abstract-form-component';
import {UnitService} from '../../../shared/services/unit.service';
import {EmissionActivityRecord} from '../../models/enterprise.dto';
import {
  EmissionFactorDTO,
  EmissionUnit
} from '../../../shared/models/shared-models';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  Validators
} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {MessageService} from 'primeng/api';
import {TranslateService} from '@ngx-translate/core';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {EmissionActivityRecordService} from '../../services/emission-activity-record.service';

export interface NewActivityRecordDialogConfig {
  activityId: string;
  factor: EmissionFactorDTO;
}

@Component({
  selector: 'app-new-activity-record-dialog',
  templateUrl: './new-activity-record-dialog.component.html'
})
export class NewActivityRecordDialogComponent extends AbstractFormComponent<EmissionActivityRecord> {
  unitOptions: {label: string; value: EmissionUnit}[] = [];
  data?: NewActivityRecordDialogConfig;
  constructor(
    protected override readonly httpClient: HttpClient,
    protected override readonly formBuilder: FormBuilder,
    protected override readonly notificationService: MessageService,
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
      startDate: new FormControl(new Date(), [Validators.required]),
      endDate: new FormControl(new Date(), [Validators.required])
    };
  }

  override initializeData(): void {
    this.initUnits();
  }

  /* eslint-disable dot-notation */
  override prepareDataBeforeSubmit(): void {
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
  }
  /* eslint-enable dot-notation */

  override submitFormDataUrl(): string {
    return this.emissionActivityRecordService.newRecordURL;
  }

  override onSubmitFormDataSuccess(result: any): void {
    this.notificationService.add({
      severity: 'success',
      summary: this.translate.instant('common.success'),
      detail: this.translate.instant('common.saveSuccess')
    });
    this.dialogRef.close(result);
  }

  onCancel(): void {
    this.dialogRef.close();
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
    const supportedUnits = this.unitService.getSameUnitType(
      this.data!.factor.emissionUnitDenominator
    );
    this.unitOptions = supportedUnits.map(unit => ({
      label: this.translate.instant(`unit.${unit}`),
      value: unit
    }));
  }
}
