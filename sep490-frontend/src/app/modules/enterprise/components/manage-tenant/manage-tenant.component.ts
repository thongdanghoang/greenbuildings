import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {DialogService} from 'primeng/dynamicdialog';
import {UUID} from '../../../../../types/uuid';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {BuildingGroup} from '@models/enterprise';
import {SubscriptionAwareComponent} from '../../../core/subscription-aware.component';
import {BuildingGroupService} from '@services/building-group.service';

@Component({
  selector: 'app-manage-tenant',
  templateUrl: './manage-tenant.component.html',
  styleUrl: './manage-tenant.component.css'
})
export class ManageTenantComponent
  extends SubscriptionAwareComponent
  implements OnInit
{
  buildingGroups: BuildingGroup[] = [];

  constructor(
    private readonly router: Router,
    public dialogService: DialogService,
    private readonly buildingGroupService: BuildingGroupService
  ) {
    super();
  }

  ngOnInit(): void {
    this.fetchBuildingGroups();
  }

  fetchBuildingGroups(): void {
    this.buildingGroupService.getByTenant().subscribe(rs => {
      this.buildingGroups = rs;
    });
  }

  goToDetails(groupId: UUID): void {
    void this.router.navigate([
      AppRoutingConstants.ENTERPRISE_PATH,
      AppRoutingConstants.BUILDING_GROUP_PATH,
      groupId
    ]);
  }
}
