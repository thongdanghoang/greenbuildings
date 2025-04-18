import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, ParamMap, Router} from '@angular/router';
import {UUID} from '../../../../../types/uuid';
import {SubscriptionAwareComponent} from '../../../core/subscription-aware.component';
import {BuildingGroupService} from '../../services/building-group.service';
import {BuildingGroup} from '../../models/enterprise.dto';
import {takeUntil} from 'rxjs/operators';
import {AppRoutingConstants} from '../../../../app-routing.constant';

@Component({
  selector: 'app-building-group-detail',
  templateUrl: './building-group-detail.component.html'
})
export class BuildingGroupDetailComponent
  extends SubscriptionAwareComponent
  implements OnInit
{
  buildingGroup!: BuildingGroup;

  constructor(
    private readonly activatedRoute: ActivatedRoute,
    private readonly buildingGroupService: BuildingGroupService,
    private readonly router: Router
  ) {
    super();
  }

  ngOnInit(): void {
    this.fetchBuildingGroupDetails();
  }

  goBack(): void {
    void this.router.navigate([
      AppRoutingConstants.ENTERPRISE_PATH,
      AppRoutingConstants.BUILDING_MANAGEMENT_PATH,
      this.buildingGroup.building.id
    ]);
  }

  private fetchBuildingGroupDetails(): void {
    this.activatedRoute.paramMap
      .pipe(takeUntil(this.destroy$))
      .subscribe((params: ParamMap) => {
        const groupId = params.get('id');
        if (groupId) {
          this.buildingGroupService
            .getById(groupId as UUID)
            .subscribe((group: BuildingGroup) => {
              this.buildingGroup = group;
            });
        }
      });
  }
}
