import {HttpClient} from '@angular/common/http';
import {Component} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {MessageService} from 'primeng/api';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {UUID} from '../../../../../types/uuid';
import {AbstractFormComponent} from '../../../shared/components/form/abstract-form-component';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  Validators
} from '@angular/forms';
import {
  EmissionFactorDTO,
  EmissionSourceDTO
} from '../../../shared/models/shared-models';
import {EmissionFactorService} from '../../../shared/services/emission-factor.service';
import {EmissionSourceService} from '../../../shared/services/emission-source.service';
import {EmissionActivity} from '../../models/enterprise.dto';
import {EmissionActivityService} from '../../services/emission-activity.service';

@Component({
  selector: 'app-new-activity-dialog',
  templateUrl: './new-activity-dialog.component.html',
  styleUrl: './new-activity-dialog.component.css'
})
export class NewActivityDialogComponent extends AbstractFormComponent<EmissionActivity> {
  emissionFactors: EmissionFactorDTO[] = [];
  emissionSources: EmissionSourceDTO[] = [];
  availableEmissionFactors: EmissionFactorDTO[] = [];

  protected readonly formStructure = {
    id: new FormControl(null),
    version: new FormControl(0),
    buildingID: new FormControl('', Validators.required),
    emissionFactorID: new FormControl('', Validators.required),
    emissionSourceID: new FormControl('', Validators.required), // for UI handle only
    name: new FormControl('', Validators.required),
    type: new FormControl(''),
    category: new FormControl(''),
    description: new FormControl(''),
    records: new FormControl([])
  };

  constructor(
    httpClient: HttpClient,
    formBuilder: FormBuilder,
    notificationService: MessageService,
    translate: TranslateService,
    private readonly sourceService: EmissionSourceService,
    private readonly factorService: EmissionFactorService,
    private readonly activityService: EmissionActivityService,
    private readonly ref: DynamicDialogRef,
    public config: DynamicDialogConfig<UUID>
  ) {
    super(httpClient, formBuilder, notificationService, translate);
  }

  protected override initializeFormControls(): {
    [key: string]: AbstractControl;
  } {
    return this.formStructure;
  }

  protected override initializeData(): void {
    this.formStructure.emissionFactorID.disable();
    this.initializeFormData();
    this.factorService.findAll().subscribe((rs: EmissionFactorDTO[]) => {
      this.emissionFactors = rs;
      this.emissionSources = Array.from(
        new Map(
          this.emissionFactors.map(f => [
            f.emissionSourceDTO.id,
            f.emissionSourceDTO
          ])
        ).values()
      );
    });
  }

  protected override submitFormDataUrl(): string {
    return this.activityService.getCreateNewActivityURL();
  }

  protected closeDialog(): void {
    this.ref.close();
  }

  protected onSourceChange(): void {
    this.availableEmissionFactors = this.emissionFactors.filter(
      f =>
        f.emissionSourceDTO.id.toString() ===
        this.formStructure.emissionSourceID.value
    );
    this.formStructure.emissionFactorID.enable();
    this.formStructure.emissionFactorID.reset();
    this.formStructure.emissionFactorID.markAsPristine();
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  protected override onSubmitFormDataSuccess(result: any): void {
    this.ref.close(true);
  }

  protected getLocalizedName(source: any): string {
    const lang = this.translate.currentLang.toLowerCase();
    switch (lang) {
      case 'zh':
        return source.nameZH;
      case 'vi':
        return source.nameVN;
      default:
        return source.nameEN;
    }
  }

  private initializeFormData(): void {
    this.formStructure.buildingID.setValue(this.config.data!.toString());
  }
}
