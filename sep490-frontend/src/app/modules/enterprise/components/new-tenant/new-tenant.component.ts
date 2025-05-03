import {Location} from '@angular/common';
import {HttpClient} from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormControl, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {InviteTenantToBuildingGroup} from '@models/building-group';
import {TranslateService} from '@ngx-translate/core';
import {filter, map, switchMap, takeUntil} from 'rxjs';
import {validate} from 'uuid';
import {UUID} from '../../../../../types/uuid';
import {BuildingDetails, BuildingGroup} from '@models/enterprise';
import {BuildingGroupService} from '@services/building-group.service';
import {BuildingService} from '@services/building.service';
import {AbstractFormComponent} from '@shared/components/form/abstract-form-component';
import {ToastProvider} from '@shared/services/toast-provider';

@Component({
  selector: 'app-new-tenant',
  templateUrl: './new-tenant.component.html',
  styleUrl: './new-tenant.component.css'
})
export class NewTenantComponent extends AbstractFormComponent<InviteTenantToBuildingGroup> implements OnInit {
  buildingDetails!: BuildingDetails;
  availableGroups: BuildingGroup[] = [];

  protected readonly formStructure = {
    buildingId: new FormControl('', [Validators.required]),
    tenantEmail: new FormControl('', [Validators.required, Validators.email]),
    selectedGroupId: new FormControl<UUID | null>(null, Validators.required)
  };

  constructor(
    httpClient: HttpClient,
    formBuilder: FormBuilder,
    notificationService: ToastProvider,
    translate: TranslateService,
    private readonly router: Router,
    private readonly activatedRoute: ActivatedRoute,
    private readonly buildingService: BuildingService,
    private readonly buildingGroupService: BuildingGroupService,
    private readonly location: Location
  ) {
    super(httpClient, formBuilder, notificationService, translate);
  }

  fetchAvailableGroups(): void {
    this.buildingGroupService
      .getAvailableBuildingGroups(this.buildingDetails.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: groups => {
          this.availableGroups = groups;
        }
      });
  }

  fetchBuildingDetails(): void {
    this.activatedRoute.paramMap
      .pipe(
        takeUntil(this.destroy$),
        map(params => params.get('id')),
        filter((idParam): idParam is string => !!idParam),
        filter(id => validate(id)),
        switchMap(id => this.buildingService.getBuildingDetails(id as UUID))
      )
      .subscribe(details => {
        this.formStructure.buildingId.setValue(details.id.toString());
        this.buildingDetails = details;
        this.fetchAvailableGroups();
      });
  }

  selectGroup(id: UUID): void {
    this.formStructure.selectedGroupId.setValue(id);
  }

  protected override initializeFormControls(): {
    [key: string]: AbstractControl;
  } {
    return this.formStructure;
  }

  protected override initializeData(): void {
    this.fetchBuildingDetails();
  }

  protected override submitFormDataUrl(): string {
    return this.buildingGroupService.inviteTenantUrl;
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  protected override onSubmitFormDataSuccess(_result: any): void {
    this.location.back();
  }
}
