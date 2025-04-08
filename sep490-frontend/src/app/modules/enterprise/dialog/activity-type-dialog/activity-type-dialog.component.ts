import {Component} from '@angular/core';
import {AbstractFormComponent} from '../../../shared/components/form/abstract-form-component';
import {ActivityType} from '../../models/enterprise.dto';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  Validators
} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {MessageService} from 'primeng/api';
import {TranslateService} from '@ngx-translate/core';
import {ActivityTypeService} from '../../services/activity-type.service';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {UUID} from '../../../../../types/uuid';
import {takeUntil} from 'rxjs';
import {ApplicationService} from '../../../core/services/application.service';

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
    enterpriseId: new FormControl('')
  };

  constructor(
    httpClient: HttpClient,
    formBuilder: FormBuilder,
    notificationService: MessageService,
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
          this.formGroup.get('enterpriseId')?.setValue(u.enterpriseId);
          // or alternatively: this.formStructure.enterpriseId.setValue(u.enterpriseId);
        }
      }
    );
  }
}
