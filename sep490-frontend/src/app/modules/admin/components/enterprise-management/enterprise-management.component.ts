import {Component, EventEmitter, OnInit} from '@angular/core';
import {EnterpriseDTO} from '@generated/models/enterprise-dto';
import {EnterpriseSearchCriteria} from '@generated/models/enterprise-search-criteria';
import {ApplicationService} from '@services/application.service';
import {EnterpriseService} from '@services/enterprise.service';
import {TableTemplateColumn} from '@shared/components/table-template/table-template.component';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';
import {Observable} from 'rxjs';

@Component({
  selector: 'app-enterprise-management',
  templateUrl: './enterprise-management.component.html',
  styleUrl: './enterprise-management.component.css'
})
export class EnterpriseManagementComponent extends SubscriptionAwareComponent implements OnInit {
  protected fetchEnterprises!: (
    criteria: SearchCriteriaDto<EnterpriseSearchCriteria>
  ) => Observable<SearchResultDto<EnterpriseDTO>>;
  protected cols: TableTemplateColumn[] = [];
  protected readonly searchEvent: EventEmitter<void> = new EventEmitter();
  protected searchCriteria: EnterpriseSearchCriteria = {criteria: ''};

  constructor(
    protected readonly applicationService: ApplicationService,
    private readonly enterpriseService: EnterpriseService
  ) {
    super();
  }

  ngOnInit(): void {
    this.buildCols();
    this.fetchEnterprises = this.enterpriseService.search.bind(this.enterpriseService);
  }

  buildCols(): void {
    this.cols.push(
      {
        field: 'name',
        header: 'enterprise.create.name'
      },
      {
        field: 'email',
        header: 'enterprise.create.email'
      },
      {
        field: 'hotline',
        header: 'enterprise.create.hotline'
      },
      {
        field: 'taxCode',
        header: 'enterprise.create.taxCode'
      },
      {
        field: 'representativeName',
        header: 'enterprise.create.representativeName'
      }
    );
  }

  search(): void {
    this.searchEvent.emit();
  }
}
