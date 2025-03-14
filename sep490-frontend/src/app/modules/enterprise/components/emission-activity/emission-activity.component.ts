import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {MessageService} from 'primeng/api';
import {Observer, filter, map, switchMap, takeUntil} from 'rxjs';
import {validate} from 'uuid';
import {UUID} from '../../../../../types/uuid';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {BuildingService} from '../../../../services/building.service';
import {SubscriptionAwareComponent} from '../../../core/subscription-aware.component';
import {BuildingDetails} from '../../models/enterprise.dto';

@Component({
  selector: 'app-emission-activity',
  templateUrl: './emission-activity.component.html',
  styleUrl: './emission-activity.component.css'
})
export class EmissionActivityComponent
  extends SubscriptionAwareComponent
  implements OnInit
{
  buildingDetail!: BuildingDetails;

  readonly fetchBuildingObserver: Observer<BuildingDetails> = {
    next: async building => {
      if (!building.subscriptionDTO) {
        this.msgService.add({
          severity: 'error',
          summary: this.translate.instant('http.error.status.403.title'),
          life: 3000,
          sticky: true
        });
        await this.router.navigate([
          AppRoutingConstants.ENTERPRISE_PATH,
          AppRoutingConstants.BUILDING_PATH
        ]);
      } else {
        this.buildingDetail = building;
      }
    },
    error: async () => {
      await this.router.navigate([
        AppRoutingConstants.ENTERPRISE_PATH,
        AppRoutingConstants.BUILDING_PATH
      ]);
    },
    complete: () => {}
  };

  constructor(
    private readonly activatedRoute: ActivatedRoute,
    private readonly msgService: MessageService,
    private readonly router: Router,
    private readonly translate: TranslateService,
    private readonly buildingService: BuildingService
  ) {
    super();
  }

  ngOnInit(): void {
    this.fetchAndValidateBuilding();
  }

  fetchAndValidateBuilding(): void {
    this.activatedRoute.paramMap
      .pipe(
        takeUntil(this.destroy$),
        map(params => params.get('buildingId')),
        filter((idParam): idParam is string => !!idParam),
        filter(id => validate(id)),
        switchMap(id => this.buildingService.getBuildingDetails(id as UUID))
      )
      .subscribe(this.fetchBuildingObserver);
  }
}
