import {
  Component,
  EventEmitter,
  OnInit,
  TemplateRef,
  ViewChild
} from '@angular/core';
import {InvitationResponse, InvitationSearchCriteria} from '@models/tenant';
import {TranslateService} from '@ngx-translate/core';
import {Observable, takeUntil} from 'rxjs';
import {UUID} from '../../../../../types/uuid';
import {
  Building,
  BuildingGroup,
  InvitationDTO,
  InvitationStatus
} from '@models/enterprise';
import {BuildingGroupService} from '@services/building-group.service';
import {BuildingService} from '@services/building.service';
import {InvitationService} from '@services/invitation.service';
import {ApplicationService} from '../../../core/services/application.service';
import {SubscriptionAwareComponent} from '../../../core/subscription-aware.component';
import {TableTemplateColumn} from '@shared/components/table-template/table-template.component';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';
import {ToastProvider} from '@shared/services/toast-provider';

@Component({
  selector: 'app-sent-invitation',
  templateUrl: './sent-invitation.component.html',
  styleUrl: './sent-invitation.component.css'
})
export class SentInvitationComponent
  extends SubscriptionAwareComponent
  implements OnInit
{
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
  protected searchCriteria: InvitationSearchCriteria = {
    enterpriseId: '' as UUID,
    tenantEmail: ''
  };
  protected readonly InvitationStatus = InvitationStatus;
  protected readonly searchEvent: EventEmitter<void> = new EventEmitter();

  constructor(
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
    this.fetchInvitations = this.invitationService.fetchInvitations.bind(
      this.invitationService
    );
    this.fetchBuildings();
    this.fetchBuildingGroups();
    this.applicationService.UserData.pipe(takeUntil(this.destroy$)).subscribe(
      user => {
        this.searchCriteria.enterpriseId = user.enterpriseId;
      }
    );
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

  clearCriteria(): void {
    this.searchCriteria.tenantEmail = '';
    this.matchingGroups = [];
    this.searchCriteria.buildingId = '' as UUID;
    this.searchCriteria.buildingGroupId = '' as UUID;
    this.searchEvent.emit();
  }

  onSelectBuilding(event: any): void {
    if (event.value) {
      this.matchingGroups = this.allAvailableGroups.filter(
        group => group.building.id === event.value
      );
    } else {
      this.matchingGroups = [];
      this.searchCriteria.buildingGroupId = '' as UUID;
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
