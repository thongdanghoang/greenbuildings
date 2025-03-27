import {Component} from '@angular/core';
import {AbstractFormComponent} from '../../../shared/components/form/abstract-form-component';
import {FuelConversion} from '../../../enterprise/models/enterprise.dto';
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
import {UUID} from '../../../../../types/uuid';
import {FuelConversionService} from '../../services/fuel-conversion.service';

@Component({
  selector: 'app-fuel-dialog',
  templateUrl: './fuel-dialog.component.html',
  styleUrl: './fuel-dialog.component.css'
})
export class FuelDialogComponent extends AbstractFormComponent<FuelConversion> {
  protected readonly formStructure = {
    id: new FormControl(''),
    version: new FormControl(0),
    nameVN: new FormControl('', Validators.required),
    nameEN: new FormControl('', Validators.required),
    nameZH: new FormControl('', Validators.required)
  };

  constructor(
    httpClient: HttpClient,
    formBuilder: FormBuilder,
    notificationService: MessageService,
    translate: TranslateService,
    private readonly fuelService: FuelConversionService,
    private readonly ref: DynamicDialogRef,
    public config: DynamicDialogConfig<UUID>
  ) {
    super(httpClient, formBuilder, notificationService, translate);
  }

  get isEdit(): boolean {
    return !!this.formStructure.id.value;
  }

  protected initializeData(): void {
    if (this.config.data) {
      this.fuelService
        .getFuelConversionById(this.config.data.toString())
        .subscribe(fuel => {
          this.formGroup.patchValue(fuel);
        });
    }
  }

  protected closeDialog(): void {
    this.ref.close();
  }

  protected initializeFormControls(): {[p: string]: AbstractControl} {
    return this.formStructure;
  }

  protected onSubmitFormDataSuccess(): void {
    this.ref.close(true);
  }

  protected submitFormDataUrl(): string {
    return this.fuelService.createOrUpdateFuelConversionURL;
  }
}
