import {Component, output} from '@angular/core';
import {AbstractControl, FormControl, Validators} from '@angular/forms';
import {AssetView} from '@generated/models/asset-view';
import {AssetService} from '@services/asset.service';
import {EmissionActivityService} from '@services/emission-activity.service';
import {AbstractFormComponent} from '@shared/components/form/abstract-form-component';
import {SelectableItem} from '@shared/models/base-models';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {takeUntil} from 'rxjs';
import {UUID} from '../../../../../types/uuid';

@Component({
  selector: 'app-asset-details-dialog',
  templateUrl: './asset-details-dialog.component.html',
  styleUrl: './asset-details-dialog.component.css'
})
export class AssetDetailsDialogComponent extends AbstractFormComponent<AssetView> {
  readonly hide = output({});
  data: AssetView;

  formStructure = {
    id: new FormControl<null | UUID>(null),
    version: new FormControl(0),
    name: new FormControl<null | string>(null, [Validators.required]),
    description: new FormControl<null | string>(null),
    buildingId: new FormControl<null | UUID>(null)
  };

  protected selectableBuildings: SelectableItem<UUID>[] = [];

  constructor(
    readonly configs: DynamicDialogConfig,
    private readonly ref: DynamicDialogRef,
    private readonly assetService: AssetService,
    private readonly emissionActivityService: EmissionActivityService
  ) {
    super();
    this.data = configs.data;
  }

  onclose(): void {
    this.ref.close();
  }

  protected override initializeFormControls(): {[key: string]: AbstractControl} {
    return this.formStructure;
  }

  protected override initializeData(): void {
    this.emissionActivityService
      .getSelectableBuildings()
      .pipe(takeUntil(this.destroy$))
      .subscribe(selectableBuildings => (this.selectableBuildings = selectableBuildings));
    if (this.data.id) {
      this.assetService
        .getAssetById(this.data.id as UUID)
        .pipe(takeUntil(this.destroy$))
        .subscribe(asset =>
          this.formGroup.patchValue({
            version: asset.version ?? 0,
            id: asset.id,
            buildingId: asset.building?.id,
            name: asset.name ?? null,
            description: asset.description ?? null
          })
        );
    }
  }

  protected override submitFormDataUrl(): string {
    return AssetService.URL;
  }

  protected override onSubmitFormDataSuccess(result: any): void {
    this.ref.close(result);
  }
}
