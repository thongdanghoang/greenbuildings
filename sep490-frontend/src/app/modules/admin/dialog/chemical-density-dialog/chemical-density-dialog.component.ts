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
import {UUID} from '../../../../../types/uuid';
import {AbstractFormComponent} from '../../../shared/components/form/abstract-form-component';
import {
  ChemicalDensityDTO,
  EmissionUnit
} from '../../../shared/models/shared-models';
import {ToastProvider} from '../../../shared/services/toast-provider';
import {ChemicalDensityService} from '../../services/chemical-density.service';

@Component({
  selector: 'app-chemical-density-dialog',
  templateUrl: './chemical-density-dialog.component.html',
  styleUrl: './chemical-density-dialog.component.css'
})
export class ChemicalDensityDialogComponent extends AbstractFormComponent<ChemicalDensityDTO> {
  emissionUnits: {label: string; value: EmissionUnit}[] = Object.keys(
    EmissionUnit
  ).map(key => ({
    label: this.translate.instant(`unit.${key}`),
    value: EmissionUnit[key as keyof typeof EmissionUnit]
  }));
  protected readonly formStructure = {
    id: new FormControl<UUID | null>(null),
    version: new FormControl(0),
    chemicalFormula: new FormControl('', [Validators.required]),
    value: new FormControl<number | null>(null, [
      Validators.required,
      Validators.min(0)
    ]),
    unitNumerator: new FormControl<EmissionUnit | null>(null, [
      Validators.required
    ]),
    unitDenominator: new FormControl<EmissionUnit | null>(null, [
      Validators.required
    ])
  };

  constructor(
    httpClient: HttpClient,
    formBuilder: FormBuilder,
    notificationService: ToastProvider,
    translate: TranslateService,
    private readonly chemicalDensityService: ChemicalDensityService,
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
      this.chemicalDensityService
        .getById(this.config.data.toString())
        .subscribe(chemicalDensity => {
          this.formGroup.patchValue(chemicalDensity);
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
    return this.chemicalDensityService.createOrUpdateURL;
  }
}
