import {HttpClient} from '@angular/common/http';
import {Component} from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  Validators
} from '@angular/forms';
import {MessageService} from 'primeng/api';
import {AbstractFormComponent} from '../../../shared/components/form/abstract-form-component';
import {TranslateService} from '@ngx-translate/core';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {DownloadReport, EmissionActivity} from '../../models/enterprise.dto';
import {UUID} from '../../../../../types/uuid';
import {EmissionActivityService} from '../../services/emission-activity.service';
import {BuildingService} from '../../../../services/building.service';
@Component({
  selector: 'app-report-dialog',
  templateUrl: './report-dialog.component.html',
  styleUrl: './report-dialog.component.css'
})
export class ReportDialogComponent extends AbstractFormComponent<DownloadReport> {
  activities: EmissionActivity[] = [];

  protected readonly formStructure = {
    buildingID: new FormControl('', Validators.required),
    startDate: new FormControl('', Validators.required),
    endDate: new FormControl('', Validators.required),
    selectedActivities: new FormControl<UUID[]>([])
  };

  constructor(
    httpClient: HttpClient,
    formBuilder: FormBuilder,
    notificationService: MessageService,
    translate: TranslateService,
    private readonly emissionActivityService: EmissionActivityService,
    private readonly buildingService: BuildingService,
    private readonly ref: DynamicDialogRef,
    public config: DynamicDialogConfig<UUID>
  ) {
    super(httpClient, formBuilder, notificationService, translate);
  }

  protected initializeFormControls(): {[p: string]: AbstractControl} {
    return this.formStructure;
  }

  protected initializeData(): void {
    this.formStructure.buildingID.setValue(this.config.data!.toString());
    this.emissionActivityService
      .getAllActivitiesByBuildingId(this.config.data!)
      .subscribe(activities => {
        this.activities = activities;
      });
  }

  protected onSubmitFormDataSuccess(result: any): void {
    this.ref.close(result);
  }

  protected submitFormDataUrl(): string {
    return this.buildingService.generateReportUrl;
  }
}
