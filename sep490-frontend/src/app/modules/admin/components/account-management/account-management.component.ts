import {Component, EventEmitter, OnInit, TemplateRef, ViewChild} from '@angular/core';
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
  @ViewChild('roleTemplate', {static: true})
  roleTemplate!: TemplateRef<any>;
  @ViewChild('emailVerifiedTemplate', {static: true})
  emailVerifiedTemplate!: TemplateRef<any>;

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
    this.cols.push({
      field: 'name',
      header: 'enterprise.Users.fullName'
    });
    this.cols.push({
      field: '',
      header: 'enterprise.Users.role',
      templateRef: this.roleTemplate
    });
    this.cols.push({
      field: '',
      header: 'enterprise.Users.emailVerified',
      templateRef: this.emailVerifiedTemplate
    });
  }

  onSelectionChange(selectedUsers: EnterpriseUser[]): void {
    this.selected = selectedUsers;
  }

  search(): void {
    this.searchEvent.emit();
  }
}
