import {Component, EventEmitter, OnInit} from '@angular/core';
import {EnterpriseUser} from '@models/enterprise-user';
import {ApplicationService} from '@services/application.service';
import {EnterpriseUserService} from '@services/enterprise-user.service';
import {TableTemplateColumn} from '@shared/components/table-template/table-template.component';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';
import {Observable} from 'rxjs';
import {UserCriteria} from '../../../authorization/components/users-management/users.component';

@Component({
  selector: 'app-account-management',
  templateUrl: './account-management.component.html',
  styleUrl: './account-management.component.css'
})
export class AccountManagementComponent extends SubscriptionAwareComponent implements OnInit {
  protected fetchUsers!: (criteria: SearchCriteriaDto<UserCriteria>) => Observable<SearchResultDto<EnterpriseUser>>;
  protected cols: TableTemplateColumn[] = [];
  protected readonly searchEvent: EventEmitter<void> = new EventEmitter();
  protected selected: EnterpriseUser[] = [];
  protected searchCriteria: UserCriteria = {criteria: ''};

  constructor(
    protected readonly applicationService: ApplicationService,
    private readonly enterpriseUserService: EnterpriseUserService
  ) {
    super();
  }

  ngOnInit(): void {
    this.buildCols();
    this.fetchUsers = this.enterpriseUserService.getUsers.bind(this.enterpriseUserService);
  }

  buildCols(): void {
    this.cols.push({
      field: 'email',
      header: 'enterprise.Users.email'
    });
  }

  onSelectionChange(selectedUsers: EnterpriseUser[]): void {
    this.selected = selectedUsers;
  }

  search(): void {
    this.searchEvent.emit();
  }
}
