import {HttpClient} from '@angular/common/http';
import {Component} from '@angular/core';
import {AbstractControl, FormBuilder, FormControl, Validators} from '@angular/forms';
import {ActivityType} from '@models/enterprise';
import {TranslateService} from '@ngx-translate/core';
import {ActivityTypeService} from '@services/activity-type.service';
import {ApplicationService} from '@services/application.service';
import {AbstractFormComponent} from '@shared/components/form/abstract-form-component';
import {ToastProvider} from '@shared/services/toast-provider';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {UUID} from '../../../../../types/uuid';

export interface ActivityTypeDialogData {
  buildingId: UUID;
  activityTypeId?: UUID;
}

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
    buildingId: new FormControl<UUID | null>(null)
  };

  constructor(
    httpClient: HttpClient,
    formBuilder: FormBuilder,
    notificationService: ToastProvider,
    translate: TranslateService,
    private readonly activityTypeService: ActivityTypeService,
    private readonly ref: DynamicDialogRef,
    public config: DynamicDialogConfig<ActivityTypeDialogData>,
    protected readonly applicationService: ApplicationService
  ) {
    super(httpClient, formBuilder, notificationService, translate);
  }

  protected initializeData(): void {
    if (this.config.data?.activityTypeId) {
      this.activityTypeService
        .getActivityTypeById(this.config.data.activityTypeId.toString())
        .subscribe(activityType => {
          this.formGroup.patchValue(activityType);
        });
    }
    this.formStructure.buildingId.setValue(this.config.data!.buildingId);
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
}
