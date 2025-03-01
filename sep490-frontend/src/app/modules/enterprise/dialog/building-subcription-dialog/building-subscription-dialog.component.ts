import {Component} from '@angular/core';
import {AbstractFormComponent} from '../../../shared/components/form/abstract-form-component';
import {multipleOfValidator} from '../../../shared/validators/MultipleOfValidator';
import {
  BuildingDetails,
  CreditConvertRatio,
  CreditConvertType,
  SubscribeRequest,
  TransactionType
} from '../../models/enterprise.dto';
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
import {SubscriptionService} from '../../services/subscription.service';

export interface SubscriptionDialogOptions {
  selectedBuildingDetails: BuildingDetails;
  balance: number;
  type: TransactionType;
}

@Component({
  selector: 'app-building-subscription-dialog',
  templateUrl: './building-subscription-dialog.component.html',
  styleUrl: './building-subscription-dialog.component.css'
})
export class BuildingSubscriptionDialogComponent extends AbstractFormComponent<SubscribeRequest> {
  data: SubscriptionDialogOptions;
  checked: boolean = false;
  totalToPay: number = 0;
  sufficientBalance: boolean = true;
  protected readonly formStructure = {
    months: new FormControl(0, {
      nonNullable: true,
      validators: [Validators.min(1), Validators.max(100), Validators.required]
    }),
    numberOfDevices: new FormControl(0, {
      nonNullable: true,
      validators: [
        Validators.min(50),
        Validators.required,
        multipleOfValidator(50)
      ]
    }),
    buildingId: new FormControl('', {nonNullable: true}),
    monthRatio: new FormControl(0, {nonNullable: true}),
    deviceRatio: new FormControl(0, {nonNullable: true}),
    type: new FormControl('', {nonNullable: true})
  };

  constructor(
    httpClient: HttpClient,
    formBuilder: FormBuilder,
    notificationService: MessageService,
    translate: TranslateService,
    protected subscribeService: SubscriptionService,
    protected ref: DynamicDialogRef,
    public config: DynamicDialogConfig
  ) {
    super(httpClient, formBuilder, notificationService, translate);
    this.data = config.data;
  }

  closeDialog(): void {
    this.ref.close();
  }

  calculateTotalToPay(): void {
    this.totalToPay =
      this.formStructure.months.value *
      this.formStructure.monthRatio.value *
      this.formStructure.numberOfDevices.value *
      this.formStructure.deviceRatio.value;

    this.sufficientBalance = this.data.balance >= this.totalToPay;
    this.checked = false;
  }

  populateHiddenFields(): void {
    this.formStructure.buildingId.setValue(
      this.data.selectedBuildingDetails.id.toString()
    );
    this.formStructure.type.setValue(this.data.type.toString());
  }

  protected initializeData(): void {
    this.populateHiddenFields();
    this.fetchConversionRate();
  }

  protected initializeFormControls(): {[p: string]: AbstractControl} {
    return this.formStructure;
  }

  protected onSubmitFormDataSuccess(): void {
    this.ref.close('buy');
  }

  protected submitFormDataUrl(): string {
    return this.subscribeService.subscribeRequestURL;
  }

  private fetchConversionRate(): void {
    this.subscribeService
      .getCreditConvertRatio()
      .subscribe((rs: CreditConvertRatio[]) => {
        rs.forEach(x => {
          if (x.convertType === CreditConvertType[CreditConvertType.MONTH]) {
            this.formStructure.monthRatio.setValue(x.ratio);
          } else if (
            x.convertType === CreditConvertType[CreditConvertType.DEVICE]
          ) {
            this.formStructure.deviceRatio.setValue(x.ratio);
          }
        });
      });
  }
}
