import {HttpClient} from '@angular/common/http';
import {Component} from '@angular/core';
import {AbstractControl, FormBuilder, FormControl, Validators} from '@angular/forms';
import {ActivityType, CreateNewActivityDTO} from '@models/enterprise';
import {EmissionFactorDTO, EmissionSourceDTO} from '@models/shared-models';
import {TranslateService} from '@ngx-translate/core';
import {ActivityTypeService} from '@services/activity-type.service';
import {BuildingGroupService} from '@services/building-group.service';
import {EmissionActivityService} from '@services/emission-activity.service';
import {EmissionFactorService} from '@services/emission-factor.service';
import {AbstractFormComponent} from '@shared/components/form/abstract-form-component';
import {SelectableItem} from '@shared/models/base-models';
import {ToastProvider} from '@shared/services/toast-provider';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {UUID} from '../../../../../types/uuid';

@Component({
  selector: 'app-new-activity-dialog',
  templateUrl: './new-activity-dialog.component.html',
  styleUrl: './new-activity-dialog.component.css'
})
export class NewActivityDialogComponent extends AbstractFormComponent<CreateNewActivityDTO> {
  emissionFactors: EmissionFactorDTO[] = [];
  emissionSources: EmissionSourceDTO[] = [];
  availableEmissionFactors: EmissionFactorDTO[] = [];
  activityTypes: ActivityType[] = [];
  selectableBuildings: SelectableItem<UUID>[] = [];
  selectableBuildingGroups: SelectableItem<UUID>[] = [];

  protected readonly formStructure = {
    id: new FormControl(null),
    version: new FormControl(0),
    buildingId: new FormControl<UUID | null>(null, Validators.required),
    buildingGroupID: new FormControl<UUID | null>(null),
    emissionFactorID: new FormControl<UUID | null>(null, Validators.required),
    emissionSourceID: new FormControl<UUID | null>(null, Validators.required), // for UI handle only
    name: new FormControl<string | null>(null, Validators.required),
    type: new FormControl<string | null>(null),
    category: new FormControl<string | null>(null, Validators.required), // why database mandatory in this field?
    description: new FormControl<string | null>(null),
    records: new FormControl([])
  };

  constructor(
    httpClient: HttpClient,
    formBuilder: FormBuilder,
    notificationService: ToastProvider,
    translate: TranslateService,
    private readonly factorService: EmissionFactorService,
    private readonly activityService: EmissionActivityService,
    private readonly ref: DynamicDialogRef,
    public config: DynamicDialogConfig,
    private readonly activityTypeService: ActivityTypeService,
    private readonly buildingGroupService: BuildingGroupService
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
    this.factorService.findAll().subscribe((rs: EmissionFactorDTO[]) => {
      this.emissionFactors = rs;
      this.emissionSources = Array.from(
        new Map(this.emissionFactors.map(f => [f.emissionSourceDTO.id, f.emissionSourceDTO])).values()
      );
      this.initializeFormData();
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
      f => f.emissionSourceDTO.id === this.formStructure.emissionSourceID.value
    );
    this.formStructure.emissionFactorID.enable();
    this.formStructure.emissionFactorID.reset();
    this.formStructure.emissionFactorID.markAsPristine();
  }

  protected onSelectedBuildingChanged(): void {
    if (this.formStructure.buildingId.value) {
      this.loadActivityTypes(this.formStructure.buildingId.value);
      this.buildingGroupService.getByBuildingId(this.formStructure.buildingId.value).subscribe(
        buildingGroups =>
          (this.selectableBuildingGroups = buildingGroups.map(group => {
            return {
              value: group.id,
              label: group.name,
              disabled: false
            };
          }))
      );
    }
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  protected override onSubmitFormDataSuccess(_result: any): void {
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
    if (this.config.data?.id) {
      // edit mode
      this.formStructure.id.setValue(this.config.data?.id);
      this.formStructure.version.setValue(this.config.data?.version);
      this.formStructure.buildingId.setValue(this.config.data?.buildingId);
      this.onSelectedBuildingChanged();
      this.formStructure.buildingGroupID.setValue(this.config.data?.buildingGroupId);
      this.formStructure.emissionSourceID.setValue(this.config.data?.emissionSourceID);
      this.onSourceChange();
      this.formStructure.emissionFactorID.setValue(this.config.data?.emissionFactorID);
      this.formStructure.name.setValue(this.config.data?.name);
      this.formStructure.category.setValue(this.config.data?.category);
      this.formStructure.description.setValue(this.config.data?.description);
    } else {
      // create mode
      this.formStructure.buildingGroupID.setValue(this.config.data?.buildingGroupId);
      if (this.config.data?.buildingId) {
        this.formStructure.buildingId.setValue(this.config.data?.buildingId);
        this.loadActivityTypes(this.config.data?.buildingId);
      }
    }
    if (this.config.data?.selectableBuildings) {
      this.selectableBuildings = this.config.data.selectableBuildings;
    }
  }

  private loadActivityTypes(buildingId: UUID): void {
    this.activityTypeService.getByBuildingId(buildingId).subscribe(types => {
      this.activityTypes = types;
      if (this.config.data?.type) {
        this.formStructure.type.setValue(
          this.activityTypes
            .filter(activity => activity.name === this.config.data.type)
            .map(activity => activity.id)[0]
            .toString()
        );
      }
    });
  }
}
