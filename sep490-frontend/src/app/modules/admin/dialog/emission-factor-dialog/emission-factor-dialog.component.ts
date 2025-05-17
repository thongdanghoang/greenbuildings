import {Component} from '@angular/core';
import {AbstractControl, FormControl, Validators} from '@angular/forms';
import {EmissionFactorDTO} from '@models/shared-models';
import {EmissionFactorService} from '@services/emission_factor.service';
import {AbstractFormComponent} from '@shared/components/form/abstract-form-component';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {UUID} from '../../../../../types/uuid';

@Component({
  selector: 'app-emission-factor-dialog',
  templateUrl: './emission-factor-dialog.component.html',
  styleUrl: './emission-factor-dialog.component.css'
})
export class EmissionFactorDialogComponent extends AbstractFormComponent<EmissionFactorDTO> {
  protected readonly formStructure = {
    id: new FormControl(''),
    version: new FormControl(0),
    co2: new FormControl(null, Validators.required),
    ch4: new FormControl(null, Validators.required),
    n2o: new FormControl(null, Validators.required),
    nameVN: new FormControl('', Validators.required),
    nameEN: new FormControl('', Validators.required),
    nameZH: new FormControl('', Validators.required),
    emissionUnitNumerator: new FormControl(null, Validators.required),
    emissionUnitDenominator: new FormControl(null, Validators.required),
    emissionSourceDTO: new FormControl(null, Validators.required),
    description: new FormControl(null),
    validFrom: new FormControl(null, Validators.required),
    validTo: new FormControl(null, Validators.required),
    directEmission: new FormControl(false),
    energyConversionDTO: new FormControl(null),
    active: new FormControl(false)
  };

  constructor(
    private readonly emissionFactorService: EmissionFactorService,
    private readonly ref: DynamicDialogRef,
    public config: DynamicDialogConfig<UUID>
  ) {
    super();
  }

  protected initializeData(): void {
    if (this.config.data) {
      this.emissionFactorService.getEmissionFactorById(this.config.data.toString()).subscribe(emission => {
        // Convert validFrom and validTo to Date objects if they are strings
        const patchedEmission = {
          ...emission,
          validFrom: emission.validFrom ? new Date(emission.validFrom) : null,
          validTo: emission.validTo ? new Date(emission.validTo) : null
        };
        this.formGroup.patchValue(patchedEmission);
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
    return this.emissionFactorService.createOrUpdateURL;
  }
}
