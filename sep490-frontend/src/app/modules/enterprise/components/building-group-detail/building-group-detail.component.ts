import {Component, EventEmitter, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {ActivatedRoute, ParamMap, Router} from '@angular/router';
import {ActivitySearchCriteria} from '@models/emission-activity';
import {BuildingGroup, EmissionActivity, EmissionActivityTableView, InvitationDTO} from '@models/enterprise';
import {UserRole} from '@models/role-names';
import {EmissionFactorDTO} from '@models/shared-models';
import {TranslateService} from '@ngx-translate/core';
import {ApplicationService} from '@services/application.service';
import {BuildingGroupService} from '@services/building-group.service';
import {EmissionActivityService} from '@services/emission-activity.service';
import {InvitationService} from '@services/invitation.service';
import {TableTemplateColumn} from '@shared/components/table-template/table-template.component';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';
import {ModalProvider} from '@shared/services/modal-provider';
import {ToastProvider} from '@shared/services/toast-provider';
import {DialogService, DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {Observable, filter, switchMap, tap} from 'rxjs';
import {takeUntil} from 'rxjs/operators';
import {UUID} from '../../../../../types/uuid';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {ApplicationConstant} from '../../../../application.constant';
import {EnterpriseDetailDialogComponent} from '../../dialog/enteprise-detail-dialog/enterprise-detail-dialog.component';
import {NewActivityDialogComponent} from '../../dialog/new-activity-dialog/new-activity-dialog.component';
import {TenantDetailDialogComponent} from '../../dialog/tenant-detail-dialog/tenant-detail-dialog.component';

@Component({
  selector: 'app-building-group-detail',
  templateUrl: './building-group-detail.component.html'
})
export class BuildingGroupDetailComponent extends SubscriptionAwareComponent implements OnInit {
  userRole!: UserRole;
  buildingGroup!: BuildingGroup;
  pendingInvitation: InvitationDTO | undefined;
  ref: DynamicDialogRef | undefined;
  @ViewChild('typeTemplate', {static: true})
  typeTemplate!: TemplateRef<any>;
  @ViewChild('actionsTemplate', {static: true})
  actionsTemplate!: TemplateRef<any>;
  @ViewChild('factorNameTemplate', {static: true})
  factorNameTemplate!: TemplateRef<any>;
  protected readonly UserRole = UserRole;
  protected fetchActivity!: (
    criteria: SearchCriteriaDto<ActivitySearchCriteria>
  ) => Observable<SearchResultDto<EmissionActivityTableView>>;
  protected searchCriteria!: ActivitySearchCriteria;
  protected cols: TableTemplateColumn[] = [];
  protected readonly searchEvent: EventEmitter<void> = new EventEmitter();
  protected readonly clearSelectedEvent: EventEmitter<void> = new EventEmitter();
  protected selected: EmissionActivity[] = [];

  constructor(
    private readonly activatedRoute: ActivatedRoute,
    private readonly buildingGroupService: BuildingGroupService,
    private readonly router: Router,
    private readonly activityService: EmissionActivityService,
    private readonly dialogService: DialogService,
    private readonly translate: TranslateService,
    private readonly modalProvider: ModalProvider,
    private readonly msgService: ToastProvider,
    private readonly invitationService: InvitationService,
    protected readonly applicationService: ApplicationService
  ) {
    super();
  }

  ngOnInit(): void {
    this.getUserRole();
    this.buildCols();
    this.searchCriteria = {buildingGroupId: '' as UUID};
    this.fetchActivity = this.activityService.fetchActivityOfBuilding.bind(this.activityService);

    this.fetchBuildingGroupDetails();
  }

  getUserRole(): void {
    this.applicationService
      .getUserRoles()
      .pipe(takeUntil(this.destroy$))
      .subscribe(rs => {
        this.userRole = rs[0];
      });
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
      sortable: true,
      templateRef: this.typeTemplate
    });
    this.cols.push({
      header: 'enterprise.emission.activity.table.category',
      field: '',
      templateRef: this.factorNameTemplate
    });
    this.cols.push({
      field: 'actions',
      header: '',
      templateRef: this.actionsTemplate
    });
  }

  onSelectionChange(selected: EmissionActivity[]): void {
    this.selected = selected;
  }

  navigateToTenantDetail(): void {
    if (this.buildingGroup.tenant?.id) {
      const config: DynamicDialogConfig = {
        data: this.buildingGroup.tenant.id,
        closeOnEscape: true,
        dismissableMask: true,
        showHeader: true,
        header: this.translate.instant('enterprise.tenant.dialogTitle'),
        closable: true,
        width: '50%'
      };
      this.ref = this.dialogService.open(TenantDetailDialogComponent, config);
    }
  }

  navigateToEnterpriseDetail(): void {
    if (this.buildingGroup.building?.id) {
      const config: DynamicDialogConfig = {
        data: this.buildingGroup.building.id,
        closeOnEscape: true,
        dismissableMask: true,
        showHeader: true,
        header: this.translate.instant('enterprise.tenant.dialogTitle'),
        closable: true,
        width: '50%'
      };
      this.ref = this.dialogService.open(EnterpriseDetailDialogComponent, config);
    }
  }

  navigateToAssignTenant(): void {
    sessionStorage.setItem(ApplicationConstant.SELECT_GROUP_TO_ASSIGN, this.buildingGroup.id as string);
    void this.router.navigate([
      AppRoutingConstants.ENTERPRISE_PATH,
      AppRoutingConstants.NEW_TENANT_PATH,
      this.buildingGroup.building.id
    ]);
  }

  unlinkTenant(): void {
    this.modalProvider
      .showDefaultConfirm('tenant.unlinkTenantConfirmMessage')
      .pipe(
        takeUntil(this.destroy$),
        filter((result: boolean) => result), // Only proceed if the user said yes
        switchMap(() =>
          this.buildingGroupService.unlinkTenant(this.buildingGroup.id).pipe(
            tap((): void => {
              this.msgService.success({
                summary: this.translate.instant('common.success')
              });
              this.fetchBuildingGroupDetails();
            })
          )
        )
      )
      .subscribe();
  }

  openNewActivityDialog(): void {
    const config: DynamicDialogConfig = {
      data: {
        buildingId: this.buildingGroup.building.id,
        buildingGroupId: this.buildingGroup.id
      },
      closeOnEscape: true,
      dismissableMask: true,
      showHeader: false
    };
    this.ref = this.dialogService.open(NewActivityDialogComponent, config);
    this.ref.onClose.subscribe(rs => {
      if (rs) {
        this.searchEvent.emit();
      }
    });
  }

  fetchBuildingGroupDetails(): void {
    this.activatedRoute.paramMap.pipe(takeUntil(this.destroy$)).subscribe((params: ParamMap) => {
      const groupId = params.get('id');
      if (groupId) {
        this.buildingGroupService.getById(groupId as UUID).subscribe((group: BuildingGroup) => {
          this.buildingGroup = group;
          if (!this.buildingGroup.tenant) {
            this.fetchPendingInvitation();
          }
          this.searchCriteria.buildingGroupId = group.id;
          this.searchEvent.emit();
        });
      }
    });
  }

  confirmDelete(): void {
    this.modalProvider
      .showDefaultConfirm()
      .pipe(takeUntil(this.destroy$))
      .subscribe((result: boolean): void => {
        if (result) {
          this.deleteActivites();
        }
      });
  }

  deleteActivites(): void {
    const ids: UUID[] = this.selected.map(activity => activity.id);

    this.activityService.deleteActivities(ids).subscribe({
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

  getFactorName(activity: EmissionActivityTableView): string {
    if (!activity.emissionFactor) {
      return '';
    }

    let lang = this.translate.currentLang.toUpperCase();
    if (lang === 'VI') {
      lang = 'VN';
    }

    return (
      (activity.emissionFactor[`name${lang}` as keyof EmissionFactorDTO] as string) || activity.emissionFactor.nameEN
    );
  }

  openActivityDetailsDialog(activity: EmissionActivity): void {
    void this.router.navigate([
      AppRoutingConstants.ENTERPRISE_PATH,
      AppRoutingConstants.EMISSION_ACTIVITY_DETAIL_PATH,
      activity.id
    ]);
  }

  fetchPendingInvitation(): void {
    this.invitationService.fetchPendingInvitation(this.buildingGroup.id).subscribe(rs => {
      this.pendingInvitation = rs;
    });
  }
}
