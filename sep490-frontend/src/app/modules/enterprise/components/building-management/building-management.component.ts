import {Component, EventEmitter, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {BuildingGroupCriteria} from '@models/building-group';
import {TranslateService} from '@ngx-translate/core';
import moment from 'moment/moment';
import {Observable, filter, map, switchMap, takeUntil} from 'rxjs';
import {validate} from 'uuid';
import {UUID} from '../../../../../types/uuid';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {BuildingDetails, BuildingGroup, OverviewBuildingDTO} from '@models/enterprise';
import {BuildingGroupService} from '@services/building-group.service';
import {BuildingService} from '@services/building.service';
import {ApplicationService} from '@services/application.service';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';
import {TableTemplateColumn} from '@shared/components/table-template/table-template.component';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';
import {ModalProvider} from '@shared/services/modal-provider';
import {ToastProvider} from '@shared/services/toast-provider';

@Component({
  selector: 'app-building-management',
  templateUrl: './building-management.component.html',
  styleUrl: './building-management.component.css'
})
export class BuildingManagementComponent extends SubscriptionAwareComponent implements OnInit {
  @ViewChild('actionsTemplate', {static: true})
  actionsTemplate!: TemplateRef<any>;
  buildingDetails!: BuildingDetails;
  protected fetchGroups!: (
    criteria: SearchCriteriaDto<BuildingGroupCriteria>
  ) => Observable<SearchResultDto<BuildingGroup>>;
  protected searchCriteria!: BuildingGroupCriteria;
  protected cols: TableTemplateColumn[] = [];
  protected readonly searchEvent: EventEmitter<void> = new EventEmitter();
  protected readonly clearSelectedEvent: EventEmitter<void> = new EventEmitter();
  protected overviewBuilding?: OverviewBuildingDTO;
  protected selected: BuildingGroup[] = [];
  protected readonly AppRoutingConstants = AppRoutingConstants;

  constructor(
    protected readonly applicationService: ApplicationService,
    private readonly translate: TranslateService,
    private readonly router: Router,
    private readonly modalProvider: ModalProvider,
    private readonly activatedRoute: ActivatedRoute,
    private readonly buildingService: BuildingService,
    private readonly msgService: ToastProvider,
    private readonly buildingGroupService: BuildingGroupService
  ) {
    super();
  }

  ngOnInit(): void {
    this.fetchBuildingDetails();
    this.fetchGroups = this.buildingGroupService.search.bind(this.buildingGroupService);
    this.searchCriteria = {buildingId: '' as UUID};
  }

  buildCols(): void {
    this.cols.push({
      header: 'enterprise.building-group.create.name',
      field: 'name',
      sortable: true
    });
    this.cols.push({
      header: 'enterprise.building-group.create.description',
      field: 'description',
      sortable: true
    });
    this.cols.push({
      field: 'actions',
      header: '',
      templateRef: this.actionsTemplate
    });
  }

  goBack(): void {
    void this.router.navigate([AppRoutingConstants.ENTERPRISE_PATH, AppRoutingConstants.BUILDING_PATH]);
  }

  navigateToBuildingGroupDetail(group: BuildingGroup): void {
    void this.router.navigate([AppRoutingConstants.ENTERPRISE_PATH, AppRoutingConstants.BUILDING_GROUP_PATH, group.id]);
  }

  generateReport(): void {}

  openNewActivityDialog(): void {}

  getRemainingDays(): number | string {
    if (!this.buildingDetails?.subscriptionDTO?.endDate) {
      return 'N/A'; // Handle missing date
    }
    const endDate = moment(this.buildingDetails.subscriptionDTO.endDate);
    const today = moment();
    const diff = endDate.diff(today, 'days'); // Difference in days
    return diff >= 0 ? diff : 'Expired';
  }

  onSelectionChange(selected: BuildingGroup[]): void {
    this.selected = selected;
  }

  confirmDelete(): void {
    this.modalProvider
      .showConfirm({
        message: this.translate.instant('common.defaultConfirmMessage'),
        header: this.translate.instant('common.confirmHeader'),
        icon: 'pi pi-info-circle',
        acceptButtonStyleClass: 'p-button-danger p-button-text min-w-20',
        rejectButtonStyleClass: 'p-button-contrast p-button-text min-w-20',
        acceptIcon: 'none',
        acceptLabel: this.translate.instant('common.accept'),
        rejectIcon: 'none',
        rejectLabel: this.translate.instant('common.reject')
      })
      .pipe(takeUntil(this.destroy$))
      .subscribe((result: boolean): void => {
        if (result) {
          this.deleteGroups();
        }
      });
  }

  onNewTenant(): void {
    void this.router.navigate([
      AppRoutingConstants.ENTERPRISE_PATH,
      AppRoutingConstants.NEW_TENANT_PATH,
      this.buildingDetails.id
    ]);
  }

  onNewGroup(): void {
    void this.router.navigate([
      AppRoutingConstants.ENTERPRISE_PATH,
      AppRoutingConstants.NEW_GROUP_PATH,
      this.buildingDetails.id
    ]);
  }

  private deleteGroups(): void {
    const ids: UUID[] = this.selected.map(group => group.id);

    this.buildingGroupService.deleteGroups(ids).subscribe({
      next: () => {
        this.msgService.success({
          summary: this.translate.instant('common.success')
        });
        this.selected = []; // Clear local selection
        this.searchEvent.emit(); // Refresh table
        this.clearSelectedEvent.emit();
      }
    });
  }

  private fetchBuildingDetails(): void {
    this.activatedRoute.paramMap
      .pipe(
        takeUntil(this.destroy$),
        map(params => params.get('id')),
        filter((idParam): idParam is string => !!idParam),
        filter(id => validate(id)),
        switchMap(id => this.buildingService.getBuildingDetails(id as UUID))
      )
      .subscribe(building => {
        this.buildingDetails = building;
        this.searchCriteria.buildingId = this.buildingDetails.id;
        this.buildCols();
        this.searchEvent.emit();
        // Fetch overview after building details
        this.buildingService
          .getBuildingOverview(this.buildingDetails.id)
          .subscribe(overview => (this.overviewBuilding = overview));
      });
  }
}
