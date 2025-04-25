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
import {takeUntil} from 'rxjs';
import {UUID} from '../../../../../types/uuid';
import {ApplicationService} from '../../../core/services/application.service';
import {AbstractFormComponent} from '../../../shared/components/form/abstract-form-component';
import {ToastProvider} from '../../../shared/services/toast-provider';
import {ActivityType} from '../../models/enterprise.dto';
import {ActivityTypeService} from '../../services/activity-type.service';

@Component({
  selector: 'app-activity-type-dialog',
  templateUrl: './activity-type-dialog.component.html',
  styleUrl: './activity-type-dialog.component.css'
})
export class ActivityTypeDialogComponent extends AbstractFormComponent<ActivityType> {
  protected readonly formStructure = {
    id: new FormControl(''),
    version: new FormControl(0),
    name: new FormControl('', Validators.required),
    description: new FormControl('', Validators.required),
    tenantID: new FormControl('')
  };

  constructor(
    httpClient: HttpClient,
    formBuilder: FormBuilder,
    notificationService: ToastProvider,
    translate: TranslateService,
    private readonly activityTypeService: ActivityTypeService,
    private readonly ref: DynamicDialogRef,
    public config: DynamicDialogConfig<UUID>,
    protected readonly applicationService: ApplicationService
  ) {
    super(httpClient, formBuilder, notificationService, translate);
  }

  get isEdit(): boolean {
    return !!this.formStructure.id.value;
  }

  protected initializeData(): void {
    if (this.config.data) {
      this.activityTypeService
        .getActivityTypeById(this.config.data.toString())
        .subscribe(activityType => {
          this.formGroup.patchValue(activityType);
        });
    }
    this.loadActivityTypes(); // Call the method during construction
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
    return this.activityTypeService.createNewURL;
  }

  private loadActivityTypes(): void {
    this.applicationService.UserData.pipe(takeUntil(this.destroy$)).subscribe(
      u => {
        if (u?.enterpriseId) {
          this.formGroup.get('tenantID')?.setValue(u.enterpriseId);
          // or alternatively: this.formStructure.enterpriseId.setValue(u.enterpriseId);
        }
      }
    );
  }
}
