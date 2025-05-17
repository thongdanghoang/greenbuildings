import {Component, OnInit} from '@angular/core';
import {AbstractControl, FormControl, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {CreateBuildingGroupDTO} from '@models/building-group';
import {BuildingDetails} from '@models/enterprise';
import {BuildingGroupService} from '@services/building-group.service';
import {BuildingService} from '@services/building.service';
import {AbstractFormComponent} from '@shared/components/form/abstract-form-component';
import {filter, map, switchMap, takeUntil} from 'rxjs';
import {validate} from 'uuid';
import {UUID} from '../../../../../types/uuid';
import {AppRoutingConstants} from '../../../../app-routing.constant';

@Component({
  selector: 'app-new-building-group',
  templateUrl: './new-building-group.component.html',
  styleUrl: './new-building-group.component.css'
})
export class NewBuildingGroupComponent extends AbstractFormComponent<CreateBuildingGroupDTO> implements OnInit {
  buildingDetails!: BuildingDetails;

  protected readonly formStructure = {
    name: new FormControl('', Validators.required),
    description: new FormControl(''),
    buildingId: new FormControl('', Validators.required),
    tenantEmail: new FormControl('', Validators.email)
  };

  constructor(
    private readonly activatedRoute: ActivatedRoute,
    private readonly buildingService: BuildingService,
    private readonly buildingGroupService: BuildingGroupService,
    private readonly router: Router
  ) {
    super();
  }

  initializeData(): void {
    this.fetchBuildingDetails();
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
      });
  }

  protected initializeFormControls(): {[p: string]: AbstractControl} {
    return this.formStructure;
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  protected onSubmitFormDataSuccess(result: any): void {
    void this.router.navigate([
      AppRoutingConstants.ENTERPRISE_PATH,
      AppRoutingConstants.BUILDING_MANAGEMENT_PATH,
      this.buildingDetails.id
    ]);
  }

  protected submitFormDataUrl(): string {
    return this.buildingGroupService.newBuildingGroupUrl;
  }
}
