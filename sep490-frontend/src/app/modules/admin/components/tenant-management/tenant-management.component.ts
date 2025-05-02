import {Component, EventEmitter, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {ApplicationService} from '@services/application.service';
import {SearchTenantCriteria, TenantService, TenantTableView} from '@services/tenant.service';
import {TableTemplateColumn} from '@shared/components/table-template/table-template.component';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';
import {Observable} from 'rxjs';
import {AppRoutingConstants} from '../../../../app-routing.constant';

@Component({
  selector: 'app-tenant-management',
  templateUrl: './tenant-management.component.html',
  styleUrl: './tenant-management.component.css'
})
export class TenantManagementComponent extends SubscriptionAwareComponent implements OnInit {
  @ViewChild('buildingTemplate', {static: true})
  buildingTemplate!: TemplateRef<any>;
  fetchTenant!: (criteria: SearchCriteriaDto<SearchTenantCriteria>) => Observable<SearchResultDto<TenantTableView>>;
  cols: TableTemplateColumn[] = [];
  searchCriteria: SearchTenantCriteria = {
    email: ''
  };
  searchEvent: EventEmitter<void> = new EventEmitter();

  protected readonly AppRoutingConstants = AppRoutingConstants;

  constructor(
    private readonly tenantService: TenantService,
    protected readonly applicationService: ApplicationService
  ) {
    super();
  }

  ngOnInit(): void {
    this.buildCols();
    this.fetchTenant = this.tenantService.searchByAdmin.bind(this.tenantService);
  }

  buildCols(): void {
    this.cols = [
      {
        field: 'name',
        header: 'linkedTenant.table.name'
      },
      {
        field: 'email',
        header: 'linkedTenant.table.email'
      },
      {
        field: '',
        header: 'linkedTenant.table.building',
        templateRef: this.buildingTemplate
      }
    ];
  }
}
