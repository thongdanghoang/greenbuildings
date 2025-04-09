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

  // eslint-disable-next-line @typescript-eslint/no-unused-vars,@typescript-eslint/explicit-function-return-type
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
      .subscribe((result: HttpResponse<Blob>) => {
        const contentType = result.headers.get('content-type');
        // const contentDisposition = result.headers.get('content-disposition');
        if (result.body) {
          const blob = new Blob([result.body], {
            type: contentType || 'application/octet-stream'
          });
          const downloadLink = document.createElement('a');
          downloadLink.href = URL.createObjectURL(blob);

          const contentDisposition = result.headers.get('content-disposition');
          const fileName =
            contentDisposition?.split('filename=')[1]?.trim() || 'report.xlsx';
          downloadLink.download = fileName;
          downloadLink.click();
        } else {
          console.error('Empty response body for file download');
        }
      });
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  protected override onSubmitFormDataSuccess(result: any): void {}

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  protected override onSubmitFormRequestError(error: any): void {}

  protected submitFormDataUrl(): string {
    return this.buildingService.generateReportUrl;
  }
}
