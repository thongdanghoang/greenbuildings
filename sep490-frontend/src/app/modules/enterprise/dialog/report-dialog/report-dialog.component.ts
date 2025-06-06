import {HttpResponse} from '@angular/common/http';
import {Component} from '@angular/core';
import {AbstractControl, FormControl, Validators} from '@angular/forms';
import {BuildingGroup, DownloadReport} from '@models/enterprise';
import {BuildingGroupService} from '@services/building-group.service';
import {BuildingService} from '@services/building.service';
import {AbstractFormComponent} from '@shared/components/form/abstract-form-component';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {take} from 'rxjs';
import {UUID} from '../../../../../types/uuid';

@Component({
  selector: 'app-report-dialog',
  templateUrl: './report-dialog.component.html',
  styleUrl: './report-dialog.component.css'
})
export class ReportDialogComponent extends AbstractFormComponent<DownloadReport> {
  groups: BuildingGroup[] = [];

  protected readonly formStructure = {
    buildingID: new FormControl('', Validators.required),
    startDate: new FormControl(new Date()),
    rangeDates: new FormControl([new Date(), new Date()], [Validators.required]),
    endDate: new FormControl(new Date()),
    selectedGroups: new FormControl<UUID[]>([])
  };

  constructor(
    private readonly groupService: BuildingGroupService,
    private readonly buildingService: BuildingService,
    private readonly ref: DynamicDialogRef,
    public config: DynamicDialogConfig<UUID>
  ) {
    super();
  }

  protected initializeFormControls(): {[p: string]: AbstractControl} {
    return this.formStructure;
  }

  protected initializeData(): void {
    this.formStructure.buildingID.setValue(this.config.data!.toString());
    this.groupService.getByBuildingId(this.config.data!).subscribe(groups => {
      this.groups = groups;
    });
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars,@typescript-eslint/explicit-function-return-type,max-lines-per-function
  protected override submitForm(_data: DownloadReport | null = null) {
    return this.buildingService
      .submitReport(this.getSubmitFormData())
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

            const contentDisposition = result.headers.get('content-disposition');
            downloadLink.download = contentDisposition?.split('filename=')[1]?.trim() || 'report.xlsx'; // fileName
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
    if (this.formStructure.rangeDates.value!.length !== 2 && this.formStructure.rangeDates.value!.length !== 1) {
      this.formStructure.rangeDates.setErrors({
        invalid: true
      });
      return;
    }
    this.formStructure.startDate.setValue(this.formStructure.rangeDates.value![0]);
    if (this.formStructure.rangeDates.value![1]) {
      this.formStructure.endDate.setValue(this.formStructure.rangeDates.value![1]);
    } else {
      this.formStructure.endDate.setValue(this.formStructure.rangeDates.value![0]);
    }
    if (this.formStructure.startDate.value! > this.formStructure.endDate.value!) {
      const temp = this.formStructure.startDate.value;
      this.formStructure.startDate.setValue(this.formStructure.endDate.value);
      this.formStructure.endDate.setValue(temp);
    }
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  protected override onSubmitFormDataSuccess(_result: any): void {
    this.ref.close();
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  protected override onSubmitFormRequestError(_error: any): void {
    this.ref.close();
  }

  protected submitFormDataUrl(): string {
    return this.buildingService.generateReportUrl;
  }
}
