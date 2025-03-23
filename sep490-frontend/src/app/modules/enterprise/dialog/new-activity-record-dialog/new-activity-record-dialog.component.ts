import {Component} from '@angular/core';
import {AbstractFormComponent} from '../../../shared/components/form/abstract-form-component';
import {EmissionActivityRecord} from '../../models/enterprise.dto';
import {EmissionUnit} from '../../../shared/models/shared-models';
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
import {AppRoutingConstants} from '../../../../app-routing.constant';

@Component({
  selector: 'app-new-activity-record-dialog',
  templateUrl: './new-activity-record-dialog.component.html'
})
export class NewActivityRecordDialogComponent extends AbstractFormComponent<EmissionActivityRecord> {
  unitOptions: {label: string; value: EmissionUnit}[] = [];
  constructor(
    protected override readonly httpClient: HttpClient,
    protected override readonly formBuilder: FormBuilder,
    protected override readonly notificationService: MessageService,
    protected override readonly translate: TranslateService,
    private readonly dialogRef: DynamicDialogRef,
    private readonly dialogConfig: DynamicDialogConfig
  ) {
    super(httpClient, formBuilder, notificationService, translate);
  }

  override initializeFormControls(): {[key: string]: AbstractControl} {
    return {
      value: new FormControl(0, [Validators.required, Validators.min(0)]),
      unit: new FormControl('', [Validators.required]),
      startDate: new FormControl(new Date(), [Validators.required]),
      endDate: new FormControl(new Date(), [Validators.required])
    };
  }

  override initializeData(): void {
    this.initUnits();
  }

  initUnits(): void {
    this.unitOptions = Object.values(EmissionUnit).map(unit => ({
      label: this.translate.instant(
        `enterprise.emission.activity.record.table.unit.${unit}`
      ),
      value: unit
    }));
  }

  override submitFormDataUrl(): string {
    return `${AppRoutingConstants.ENTERPRISE_API_URL}/emission-activity/${this.dialogConfig.data}/records`;
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
}
