import {
  Component,
  EventEmitter,
  OnInit,
  TemplateRef,
  ViewChild,
  inject
} from '@angular/core';
import {Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {Observable, takeUntil} from 'rxjs';
import {UUID} from '../../../../../types/uuid';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {ApplicationService} from '@services/application.service';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';
import {TableTemplateColumn} from '@shared/components/table-template/table-template.component';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';
import {ModalProvider} from '@shared/services/modal-provider';
import {ToastProvider} from '@shared/services/toast-provider';
import {EnterpriseUser} from '@models/enterprise-user';
import {EnterpriseUserService} from '@services/enterprise-user.service';

export interface UserCriteria {
  criteria: string;
}

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrl: './users.component.css'
})
export class UsersComponent
  extends SubscriptionAwareComponent
  implements OnInit
{
  @ViewChild('scopeTemplate', {static: true})
  scopeTemplate!: TemplateRef<any>;
  @ViewChild('actionsTemplate', {static: true})
  actionsTemplate!: TemplateRef<any>;
  protected readonly AppRoutingConstants = AppRoutingConstants;
  protected fetchUsers!: (
    criteria: SearchCriteriaDto<UserCriteria>
  ) => Observable<SearchResultDto<EnterpriseUser>>;
  protected cols: TableTemplateColumn[] = [];
  protected readonly searchEvent: EventEmitter<void> = new EventEmitter();
  protected readonly clearSelectedEvent: EventEmitter<void> =
    new EventEmitter();
  protected selected: EnterpriseUser[] = [];
  protected searchCriteria: UserCriteria = {criteria: ''};
  private readonly router = inject(Router);

  constructor(
    protected readonly applicationService: ApplicationService,
    private readonly userService: EnterpriseUserService,
    private readonly messageService: ToastProvider,
    private readonly modalProvider: ModalProvider,
    private readonly translate: TranslateService
  ) {
    super();
  }

  ngOnInit(): void {
    this.buildCols();
    this.fetchUsers = this.userService.getUsers.bind(this.userService);
  }

  buildCols(): void {
    this.cols.push({
      field: 'name',
      sortField: 'firstName',
      header: 'enterprise.Users.table.name',
      sortable: true
    });
    this.cols.push({
      field: 'email',
      header: 'enterprise.Users.table.email',
      sortable: true
    });
    this.cols.push({
      field: 'scope',
      header: 'enterprise.Users.table.scope',
      templateRef: this.scopeTemplate
    });
    this.cols.push({
      field: 'actions',
      header: '',
      templateRef: this.actionsTemplate
    });
  }

  onDelete(rowData: EnterpriseUser): void {
    this.selected = [rowData];
    this.confirmDelete();
  }

  onEdit(rowData: EnterpriseUser): void {
    this.selected = [rowData];
    const userId = this.selected[0].id; // Retrieve the selected user's ID.
    void this.router.navigate([
      `/${AppRoutingConstants.AUTH_PATH}/${AppRoutingConstants.USER_DETAILS}`,
      userId
    ]);
  }

  onSelectionChange(selectedUsers: EnterpriseUser[]): void {
    this.selected = selectedUsers;
  }

  search(): void {
    this.searchEvent.emit();
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
          this.deleteUsers();
        }
      });
  }

  private deleteUsers(): void {
    const userIds: UUID[] = this.selected.map(user => user.id);

    this.userService.deleteUsers(userIds).subscribe({
      next: () => {
        this.messageService.success({
          summary: this.translate.instant(
            'enterprise.Users.message.success.summary'
          ),
          detail: this.translate.instant(
            'enterprise.Users.message.success.detail'
          )
        });
        this.selected = []; // Clear local selection
        this.searchEvent.emit(); // Refresh table
        this.clearSelectedEvent.emit();
      },
      error: () => {
        this.messageService.businessError({
          summary: this.translate.instant(
            'enterprise.Users.message.error.summary'
          ),
          detail: this.translate.instant(
            'enterprise.Users.message.error.detail'
          )
        });
      }
    });
  }
}
