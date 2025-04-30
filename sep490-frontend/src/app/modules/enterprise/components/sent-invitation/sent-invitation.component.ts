import {Component, EventEmitter, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {Building, BuildingGroup, InvitationDTO, InvitationStatus} from '@models/enterprise';
import {InvitationResponse, InvitationSearchCriteria} from '@models/tenant';
import {TranslateService} from '@ngx-translate/core';
import {ApplicationService} from '@services/application.service';
import {BuildingGroupService} from '@services/building-group.service';
import {BuildingService} from '@services/building.service';
import {InvitationService} from '@services/invitation.service';
import {TableTemplateColumn} from '@shared/components/table-template/table-template.component';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';
import {ModalProvider} from '@shared/services/modal-provider';
import {ToastProvider} from '@shared/services/toast-provider';
import {Observable, takeUntil} from 'rxjs';

@Component({
  selector: 'app-sent-invitation',
  templateUrl: './sent-invitation.component.html',
  styleUrl: './sent-invitation.component.css'
})
export class SentInvitationComponent extends SubscriptionAwareComponent implements OnInit {
  @ViewChild('buildingNameTemplate', {static: true})
  buildingNameTemplate!: TemplateRef<any>;
  @ViewChild('buildingGroupNameTemplate', {static: true})
  buildingGroupNameTemplate!: TemplateRef<any>;
  @ViewChild('invitationStatusTemplate', {static: true})
  invitationStatusTemplate!: TemplateRef<any>;
  @ViewChild('actionsTemplate', {static: true})
  actionTemplate!: TemplateRef<any>;

  availableBuildings: Building[] = [];
  allAvailableGroups: BuildingGroup[] = [];
  matchingGroups: BuildingGroup[] = [];

  protected fetchInvitations!: (
    criteria: SearchCriteriaDto<InvitationSearchCriteria>
  ) => Observable<SearchResultDto<InvitationDTO>>;

  protected cols: TableTemplateColumn[] = [];
  protected searchCriteria: InvitationSearchCriteria = {} as InvitationSearchCriteria;
  protected readonly InvitationStatus = InvitationStatus;
  protected readonly searchEvent: EventEmitter<void> = new EventEmitter();

  constructor(
    private readonly modalProvider: ModalProvider,
    private readonly msgService: ToastProvider,
    private readonly translate: TranslateService,
    private readonly buildingService: BuildingService,
    private readonly buildingGroupService: BuildingGroupService,
    private readonly invitationService: InvitationService,
    protected readonly applicationService: ApplicationService
  ) {
    super();
  }

  ngOnInit(): void {
    this.buildCols();
    this.fetchInvitations = this.invitationService.fetchInvitations.bind(this.invitationService);
    this.fetchBuildings();
    this.fetchBuildingGroups();
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'PENDING':
        return 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-100';
      case 'REJECTED':
        return 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-100';
      case 'ACCEPTED':
        return 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-100';
      default:
        return 'bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-100';
    }
  }

  onRejectInvitation(invitation: InvitationDTO): void {
    this.modalProvider
      .showConfirm({
        message: this.translate.instant('sentInvitation.rejectConfirm'),
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
          const data: InvitationResponse = {
            id: invitation.id,
            status: InvitationStatus.REJECTED
          };
          this.invitationService.updateStatus(data).subscribe(() => {
            this.searchEvent.emit();
            this.msgService.success({
              summary: this.translate.instant('common.success'),
              detail: this.translate.instant('sentInvitation.reject')
            });
          });
        }
      });
  }

  clearCriteria(): void {
    this.searchCriteria.tenantEmail = undefined;
    this.matchingGroups = [];
    this.searchCriteria.buildingId = undefined;
    this.searchCriteria.buildingGroupId = undefined;
    this.searchEvent.emit();
  }

  onSelectBuilding(event: any): void {
    if (event.value) {
      this.matchingGroups = this.allAvailableGroups.filter(group => group.building.id === event.value);
    } else {
      this.matchingGroups = [];
      this.searchCriteria.buildingGroupId = undefined;
    }
  }

  fetchBuildingGroups(): void {
    this.buildingGroupService
      .getAllWithBuilding()
      .pipe(takeUntil(this.destroy$))
      .subscribe(groups => {
        this.allAvailableGroups = groups;
      });
  }

  fetchBuildings(): void {
    this.buildingService
      .searchBuildings({
        page: {
          pageNumber: 0,
          pageSize: 100
        }
      })
      .pipe(takeUntil(this.destroy$))
      .subscribe(buildings => {
        this.availableBuildings = buildings.results;
      });
  }

  buildCols(): void {
    this.cols.push({
      header: 'sentInvitation.table.building',
      field: '',
      sortField: 'buildingGroup.building.name',
      sortable: true,
      templateRef: this.buildingNameTemplate
    });
    this.cols.push({
      header: 'sentInvitation.table.group',
      field: '',
      sortField: 'buildingGroup.name',
      sortable: true,
      templateRef: this.buildingGroupNameTemplate
    });
    this.cols.push({
      header: 'sentInvitation.table.email',
      field: 'email',
      sortable: true
    });
    this.cols.push({
      header: 'sentInvitation.table.status',
      field: 'status',
      sortable: true,
      templateRef: this.invitationStatusTemplate
    });
    this.cols.push({
      header: '',
      field: '',
      templateRef: this.actionTemplate
    });
  }
}
