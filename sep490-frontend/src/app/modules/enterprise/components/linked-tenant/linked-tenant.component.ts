import {Component, EventEmitter, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {ApplicationService} from '@services/application.service';
import {SearchTenantCriteria, TenantService, TenantTableView} from '@services/tenant.service';
import {TableTemplateColumn} from '@shared/components/table-template/table-template.component';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';
import {DialogService, DynamicDialogConfig} from 'primeng/dynamicdialog';
import {Observable} from 'rxjs';
import {UUID} from '../../../../../types/uuid';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {TenantDetailDialogComponent} from '../../dialog/tenant-detail-dialog/tenant-detail-dialog.component';

@Component({
  selector: 'app-linked-tenant',
  templateUrl: './linked-tenant.component.html',
  styleUrl: './linked-tenant.component.css'
})
export class LinkedTenantComponent extends SubscriptionAwareComponent implements OnInit {
  @ViewChild('buildingTemplate', {static: true})
  buildingTemplate!: TemplateRef<any>;
  @ViewChild('viewTemplate', {static: true})
  viewTemplate!: TemplateRef<any>;
  fetchTenant!: (criteria: SearchCriteriaDto<SearchTenantCriteria>) => Observable<SearchResultDto<TenantTableView>>;
  cols: TableTemplateColumn[] = [];
  searchCriteria: SearchTenantCriteria = {
    email: ''
  };
  searchEvent: EventEmitter<void> = new EventEmitter();

  protected readonly AppRoutingConstants = AppRoutingConstants;

  constructor(
    private readonly translate: TranslateService,
    private readonly tenantService: TenantService,
    private readonly dialogService: DialogService,
    protected readonly applicationService: ApplicationService
  ) {
    super();
  }

  ngOnInit(): void {
    this.buildCols();
    this.fetchTenant = this.tenantService.search.bind(this.tenantService);
  }

  onView(id: UUID): void {
    const config: DynamicDialogConfig = {
      data: id,
      closeOnEscape: true,
      dismissableMask: true,
      showHeader: true,
      header: this.translate.instant('enterprise.tenant.dialogTitle'),
      closable: true,
      width: '50%'
    };
    this.dialogService.open(TenantDetailDialogComponent, config);
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
      },
      {
        field: '',
        header: '',
        templateRef: this.viewTemplate
      }
    ];
  }
}
