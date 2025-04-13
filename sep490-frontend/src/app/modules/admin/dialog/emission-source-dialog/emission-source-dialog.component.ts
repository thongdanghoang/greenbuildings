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
import {EmissionSource} from '../../../enterprise/models/enterprise.dto';
import {AbstractFormComponent} from '../../../shared/components/form/abstract-form-component';
import {ToastProvider} from '../../../shared/services/toast-provider';
import {EmissionSourceService} from '../../services/emission-source.service';

@Component({
  selector: 'app-emission-source-dialog',
  templateUrl: './emission-source-dialog.component.html',
  styleUrl: './emission-source-dialog.component.css'
})
export class EmissionSourceDialogComponent extends AbstractFormComponent<EmissionSource> {
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
    notificationService: ToastProvider,
    translate: TranslateService,
    private readonly sourceService: EmissionSourceService,
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
      this.sourceService
        .getEmissionSourceById(this.config.data.toString())
        .subscribe(emission => {
          this.formGroup.patchValue(emission);
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
    return this.sourceService.createOrUpdateEmissionSourceURL;
  }
}
