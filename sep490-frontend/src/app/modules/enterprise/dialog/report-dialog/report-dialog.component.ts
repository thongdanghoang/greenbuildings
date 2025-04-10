import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {Component} from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  Validators
} from '@angular/forms';
import {MessageService} from 'primeng/api';
import {take} from 'rxjs';
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
    startDate: new FormControl(new Date()),
    rangeDates: new FormControl([], [Validators.required]),
    endDate: new FormControl(new Date()),
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
    protected readonly http: HttpClient,
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

  // eslint-disable-next-line @typescript-eslint/no-unused-vars,@typescript-eslint/explicit-function-return-type,max-lines-per-function
  protected override submitForm(data: DownloadReport | null = null) {
    return this.httpClient
      .post<any>(this.submitFormDataUrl(), this.getSubmitFormData(), {
        headers: new HttpHeaders({
          // responseType: 'blob',
          contentType: 'application/json',
          accept: 'application/octet-stream'
        }),
        responseType: 'blob' as 'json',
        observe: 'response'
      })
      .pipe(take(1))
      .subscribe(
        (result: HttpResponse<Blob>) => {
          const contentType = result.headers.get('content-type');
          // const contentDisposition = result.headers.get('content-disposition');
          if (result.body) {
            const blob = new Blob([result.body], {
              type: contentType || 'application/octet-stream'
            });
            const downloadLink = document.createElement('a');
            downloadLink.href = URL.createObjectURL(blob);

            const contentDisposition = result.headers.get(
              'content-disposition'
            );
            const fileName =
              contentDisposition?.split('filename=')[1]?.trim() ||
              'report.xlsx';
            downloadLink.download = fileName;
            downloadLink.click();
            this.showSaveSuccessNotification(result);
            this.onSubmitFormDataSuccess(result);
          }
        },
        error => {
          this.onSubmitFormRequestError(error);
          this.displayFormResultErrors(error.error);
          this.enableSubmitBtn();
        }
      );
  }

  protected override prepareDataBeforeSubmit(): void {
    if (
      this.formStructure.rangeDates.value!.length !== 2 &&
      this.formStructure.rangeDates.value!.length !== 1
    ) {
      this.formStructure.rangeDates.setErrors({
        invalid: true
      });
      return;
    }
    this.formStructure.startDate.setValue(
      this.formStructure.rangeDates.value![0]
    );
    if (this.formStructure.rangeDates.value![1]) {
      this.formStructure.endDate.setValue(
        this.formStructure.rangeDates.value![1]
      );
    } else {
      this.formStructure.endDate.setValue(
        this.formStructure.rangeDates.value![0]
      );
    }
    if (
      this.formStructure.startDate.value! > this.formStructure.endDate.value!
    ) {
      const temp = this.formStructure.startDate.value;
      this.formStructure.startDate.setValue(this.formStructure.endDate.value);
      this.formStructure.endDate.setValue(temp);
    }
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  protected override onSubmitFormDataSuccess(result: any): void {
    this.ref.close();
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  protected override onSubmitFormRequestError(error: any): void {
    this.ref.close();
  }

  protected submitFormDataUrl(): string {
    return this.buildingService.generateReportUrl;
  }
}
