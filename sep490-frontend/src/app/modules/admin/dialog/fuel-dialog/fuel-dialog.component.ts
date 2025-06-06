import {Component} from '@angular/core';
import {AbstractControl, FormControl, FormGroup, Validators} from '@angular/forms';
import {EmissionUnit, EnergyConversionDTO} from '@models/shared-models';
import {FuelConversionService} from '@services/fuel-conversion.service';
import {AbstractFormComponent} from '@shared/components/form/abstract-form-component';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {UUID} from '../../../../../types/uuid';

@Component({
  selector: 'app-fuel-dialog',
  templateUrl: './fuel-dialog.component.html',
  styleUrl: './fuel-dialog.component.css'
})
export class FuelDialogComponent extends AbstractFormComponent<EnergyConversionDTO> {
  emissionUnits: {label: string; value: EmissionUnit}[] = Object.keys(EmissionUnit).map(key => ({
    label: this.translate.instant(`unit.${key}`),
    value: EmissionUnit[key as keyof typeof EmissionUnit]
  }));

  constructor(
    private readonly fuelService: FuelConversionService,
    private readonly ref: DynamicDialogRef,
    public config: DynamicDialogConfig<UUID>
  ) {
    super();
  }

  get fuelFormGroup(): FormGroup {
    return this.formGroup.get('fuel') as FormGroup;
  }

  protected initializeData(): void {
    if (this.config.data) {
      this.fuelService.getFuelConversionById(this.config.data.toString()).subscribe(fuel => {
        this.formGroup.patchValue(fuel);
      });
    }
  }

  protected closeDialog(): void {
    this.ref.close();
  }

  protected initializeFormControls(): {[key: string]: AbstractControl} {
    return {
      id: new FormControl(''),
      version: new FormControl(0),
      fuel: this.formBuilder.group({
        id: [''],
        version: [0],
        nameVN: ['', Validators.required],
        nameEN: ['', Validators.required],
        nameZH: ['', Validators.required]
      }),
      conversionValue: new FormControl(0, Validators.required),
      conversionUnitNumerator: new FormControl('', Validators.required),
      conversionUnitDenominator: new FormControl('', Validators.required)
    };
  }

  protected onSubmitFormDataSuccess(): void {
    this.ref.close(true);
  }

  protected submitFormDataUrl(): string {
    return this.fuelService.createOrUpdateFuelConversionURL;
  }
}
