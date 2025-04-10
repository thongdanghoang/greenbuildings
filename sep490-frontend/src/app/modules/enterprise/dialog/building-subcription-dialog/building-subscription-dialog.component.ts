import {HttpClient} from '@angular/common/http';
import {Component} from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  Validators
} from '@angular/forms';
import {TranslateService} from '@ngx-translate/core';
import moment from 'moment';
import {MessageService} from 'primeng/api';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {AbstractFormComponent} from '../../../shared/components/form/abstract-form-component';
import {
  Building,
  BuildingDetails,
  CreditConvertRatio,
  CreditConvertType,
  TransactionType
} from '../../models/enterprise.dto';
import {SubscriptionService} from '../../services/subscription.service';

export interface SubscriptionDialogOptions {
  selectedBuildingDetails: BuildingDetails;
  balance: number;
  type: TransactionType;
  building: Building;
}

@Component({
  selector: 'app-building-subscription-dialog',
  templateUrl: './building-subscription-dialog.component.html',
  styleUrl: './building-subscription-dialog.component.css'
})
export class BuildingSubscriptionDialogComponent extends AbstractFormComponent<void> {
  data: SubscriptionDialogOptions;
  checked: boolean = false;
  totalToPay: number = 0;
  sufficientBalance: boolean = true;
  protected readonly TransactionType = TransactionType;
  protected readonly AppRoutingConstants = AppRoutingConstants;
  protected readonly formStructure = {
    months: new FormControl(0, {
      nonNullable: true
    }),
    numberOfDevices: new FormControl(0, {
      nonNullable: true
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
    if (this.data.type === TransactionType.NEW_PURCHASE) {
      this.totalToPay =
        this.formStructure.months.value *
        this.formStructure.monthRatio.value *
        this.formStructure.numberOfDevices.value *
        this.formStructure.deviceRatio.value;
    } else {
      this.totalToPay = this.calculateUpdateTotalPayForUpdate();
    }

    this.totalToPay = Number(this.totalToPay.toFixed(0));

    this.sufficientBalance = this.data.balance >= this.totalToPay;
    this.checked = false;
  }

  // eslint-disable-next-line max-lines-per-function
  calculateUpdateTotalPayForUpdate(): number {
    const months = this.formStructure.months.value;
    const numberOfDevices = this.formStructure.numberOfDevices.value;
    const monthRatio = this.formStructure.monthRatio.value;
    const deviceRatio = this.formStructure.deviceRatio.value;
    if (months === 0 && numberOfDevices === 0) {
      return 0;
    } else if (months > 0 && numberOfDevices === 0) {
      return (
        months *
        monthRatio *
        this.data.building.subscriptionDTO!.maxNumberOfDevices! *
        deviceRatio
      );
    } else if (numberOfDevices > 0 && months === 0) {
      const numberOfLeftDays = this.data.selectedBuildingDetails
        ?.subscriptionDTO?.endDate
        ? moment(
            this.data.selectedBuildingDetails.subscriptionDTO.endDate
          ).diff(moment(), 'days')
        : 0;
      return (
        numberOfDevices * deviceRatio * numberOfLeftDays * (monthRatio / 30)
      );
    }
    const numberOfLeftDays = this.data.selectedBuildingDetails?.subscriptionDTO
      ?.endDate
      ? moment(this.data.selectedBuildingDetails.subscriptionDTO.endDate).diff(
          moment(),
          'days'
        )
      : 0;
    const oldTotal =
      numberOfDevices * deviceRatio * numberOfLeftDays * (monthRatio / 30);
    const newTotal =
      (numberOfDevices +
        this.data.building.subscriptionDTO!.maxNumberOfDevices!) *
      deviceRatio *
      months *
      monthRatio;
    return oldTotal + newTotal;
  }

  populateHiddenFields(): void {
    this.formStructure.buildingId.setValue(
      this.data.selectedBuildingDetails.id.toString()
    );
    this.formStructure.type.setValue(this.data.type.toString());
  }

  setValidators(): void {
    if (this.data.type === TransactionType.NEW_PURCHASE) {
      this.formStructure.numberOfDevices.setValidators([
        Validators.min(1),
        Validators.required
      ]);
      this.formStructure.months.setValidators([
        Validators.min(1),
        Validators.max(100),
        Validators.required
      ]);
    } else {
      this.formStructure.numberOfDevices.setValidators([Validators.min(1)]);
      this.formStructure.months.setValidators([
        Validators.min(0),
        Validators.max(100)
      ]);
    }
  }

  protected initializeData(): void {
    this.populateHiddenFields();
    this.fetchConversionRate();
    this.setValidators();
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
