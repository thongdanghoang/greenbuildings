import {Component, EventEmitter, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import moment from 'moment';
import {MessageService} from 'primeng/api';
import {Observable, Observer, filter, map, switchMap, takeUntil} from 'rxjs';
import {validate} from 'uuid';
import {UUID} from '../../../../../types/uuid';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {BuildingService} from '../../../../services/building.service';
import {SubscriptionAwareComponent} from '../../../core/subscription-aware.component';
import {BuildingDetails, EmissionActivity} from '../../models/enterprise.dto';
import {ApplicationService} from '../../../core/services/application.service';
import {
  ActivitySearchCriteria,
  EmissionActivityService
} from '../../services/emission-activity.service';
import {
  SearchCriteriaDto,
  SearchResultDto
} from '../../../shared/models/base-models';
import {TableTemplateColumn} from '../../../shared/components/table-template/table-template.component';

@Component({
  selector: 'app-emission-activity',
  templateUrl: './emission-activity.component.html',
  styleUrl: './emission-activity.component.css'
})
export class EmissionActivityComponent
  extends SubscriptionAwareComponent
  implements OnInit
{
  protected fetchActivity!: (
    criteria: SearchCriteriaDto<ActivitySearchCriteria>
  ) => Observable<SearchResultDto<EmissionActivity>>;
  protected searchCriteria!: ActivitySearchCriteria;
  protected cols: TableTemplateColumn[] = [];
  protected readonly searchEvent: EventEmitter<void> = new EventEmitter();

  protected buildingDetail!: BuildingDetails;
  private readonly fetchBuildingObserver: Observer<BuildingDetails> = {
    next: building => {
      if (!building.subscriptionDTO) {
        this.msgService.add({
          severity: 'error',
          summary: this.translate.instant('http.error.status.403.title'),
          life: 3000,
          sticky: true
        });
        void this.router.navigate([
          AppRoutingConstants.ENTERPRISE_PATH,
          AppRoutingConstants.BUILDING_PATH
        ]);
      } else {
        this.handleAfterSuccessValidation(building);
      }
    },
    error: () => {
      void this.router.navigate([
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
    private readonly buildingService: BuildingService,
    private readonly activityService: EmissionActivityService,
    protected readonly applicationService: ApplicationService
  ) {
    super();
  }

  ngOnInit(): void {
    this.fetchAndValidateBuilding();
    this.fetchActivity = this.activityService.fetchActivityOfBuilding.bind(
      this.activityService
    );
    this.searchCriteria = {buildingId: '' as UUID};
  }

  handleAfterSuccessValidation(building: BuildingDetails): void {
    this.buildingDetail = building;
    this.searchCriteria.buildingId = this.buildingDetail.id;
    this.buildCols();
    this.searchEvent.emit();
  }

  buildCols(): void {
    this.cols.push({
      header: 'enterprise.emission.activity.table.name',
      field: 'name',
      sortable: true
    });
    this.cols.push({
      header: 'enterprise.emission.activity.table.type',
      field: 'type',
      sortable: true
    });
    this.cols.push({
      header: 'enterprise.emission.activity.table.category',
      field: 'category',
      sortable: true
    });
    this.cols.push({
      header: 'enterprise.emission.activity.table.quantity',
      field: 'quantity',
      sortable: true
    });
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

  getRemainingDays(): number | string {
    if (!this.buildingDetail?.subscriptionDTO?.endDate) {
      return 'N/A'; // Handle missing date
    }
    const endDate = moment(this.buildingDetail.subscriptionDTO.endDate);
    const today = moment();
    const diff = endDate.diff(today, 'days'); // Difference in days
    return diff >= 0 ? diff : 'Expired';
  }
}
