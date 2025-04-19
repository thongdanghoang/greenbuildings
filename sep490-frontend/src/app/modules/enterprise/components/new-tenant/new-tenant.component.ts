import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {filter, map, takeUntil} from 'rxjs';
import {validate} from 'uuid';
import {UUID} from '../../../../../types/uuid';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {BuildingService} from '../../../../services/building.service';
import {SubscriptionAwareComponent} from '../../../core/subscription-aware.component';
import {ToastProvider} from '../../../shared/services/toast-provider';
import {
  BuildingDetails,
  BuildingGroup,
  Tenant
} from '../../models/enterprise.dto';
import {TenantService} from '../../services/tenant.service';
import {BuildingGroupService} from '../../services/building-group.service';

@Component({
  selector: 'app-new-tenant',
  templateUrl: './new-tenant.component.html',
  styleUrl: './new-tenant.component.css'
})
export class NewTenantComponent
  extends SubscriptionAwareComponent
  implements OnInit
{
  buildingDetails!: BuildingDetails;
  tenant: Partial<Tenant> = {
    name: ''
  };
  availableGroups: BuildingGroup[] = [];

  constructor(
    private readonly router: Router,
    private readonly activatedRoute: ActivatedRoute,
    private readonly buildingService: BuildingService,
    private readonly tenantService: TenantService,
    private readonly buildingGroupService: BuildingGroupService,
    private readonly msgService: ToastProvider,
    private readonly translate: TranslateService
  ) {
    super();
  }

  ngOnInit(): void {
    this.fetchBuildingDetails();
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
        map(id => this.buildingService.getBuildingDetails(id as UUID))
      )
      .subscribe(building => {
        building.subscribe(details => {
          this.buildingDetails = details;
          this.fetchAvailableGroups();
        });
      });
  }

  onSubmit(): void {
    if (this.buildingDetails?.id) {
      this.tenantService.create(this.tenant as Tenant).subscribe({
        next: () => {
          this.msgService.success({
            summary: this.translate.instant('common.success')
          });
          void this.router.navigate([
            AppRoutingConstants.ENTERPRISE_PATH,
            AppRoutingConstants.BUILDING_PATH,
            this.buildingDetails.id
          ]);
        },
        error: error => {
          console.error('Error creating tenant:', error);
          this.msgService.warn({
            summary: this.translate.instant('common.error')
          });
        }
      });
    }
  }

  onCancel(): void {
    void this.router.navigate([
      AppRoutingConstants.ENTERPRISE_PATH,
      AppRoutingConstants.BUILDING_MANAGEMENT_PATH,
      this.buildingDetails.id
    ]);
  }
}
