import {Component, EventEmitter, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';
import {Observable} from 'rxjs';
import {TransactionAdminCriteria, TransactionEnterpriseAdminDTO} from '@models/transactions';
import {TableTemplateColumn} from '@shared/components/table-template/table-template.component';
import {ApplicationService} from '@services/application.service';
import {ToastProvider} from '@shared/services/toast-provider';
import {ModalProvider} from '@shared/services/modal-provider';
import {TranslateService} from '@ngx-translate/core';
import {DialogService} from 'primeng/dynamicdialog';
import {TransactionService} from '@services/transaction.service';

@Component({
  selector: 'app-transaction-enterprise-management',
  templateUrl: './transaction-enterprise-management.component.html',
  styleUrl: './transaction-enterprise-management.component.css'
})
export class TransactionEnterpriseManagementComponent extends SubscriptionAwareComponent implements OnInit {
  @ViewChild('typeTemplate', {static: true})
  typeTemplate!: TemplateRef<any>;
  protected fetchTransaction!: (
    criteria: SearchCriteriaDto<TransactionAdminCriteria>
  ) => Observable<SearchResultDto<TransactionEnterpriseAdminDTO>>;
  protected cols: TableTemplateColumn[] = [];
  protected readonly searchEvent: EventEmitter<void> = new EventEmitter();
  protected readonly clearSelectedEvent: EventEmitter<void> = new EventEmitter();
  protected selected: TransactionEnterpriseAdminDTO[] = [];
  protected searchCriteria: TransactionAdminCriteria = {criteria: ''};
  constructor(
    protected readonly applicationService: ApplicationService,
    private readonly transactionService: TransactionService,
    private readonly messageService: ToastProvider,
    private readonly modalProvider: ModalProvider,
    private readonly translate: TranslateService,
    private readonly dialogService: DialogService
  ) {
    super();
  }
  ngOnInit(): void {
    this.buildCols();
    this.fetchTransaction = this.transactionService.searchTransactionAdmin.bind(this.transactionService);
  }

  buildCols(): void {
    this.cols = [
      {
        field: 'createdDate',
        header: 'useCreditHistory.table.createdDate'
      },
      {
        field: 'enterpriseName',
        header: 'enterprise.create.name'
      },
      {
        field: 'buildingName',
        header: 'buildingGroup.building.name'
      },
      {
        field: 'amount',
        header: 'useCreditHistory.table.amount'
      },
      {
        field: 'months',
        header: 'useCreditHistory.table.months'
      },
      {
        field: 'numberOfDevices',
        header: 'useCreditHistory.table.numberOfDevices'
      },
      {
        field: 'transactionType',
        header: 'useCreditHistory.table.transactionType',
        templateRef: this.typeTemplate
      }
    ];
  }
  onSelectionChange(selectedTransaction: TransactionEnterpriseAdminDTO[]): void {
    this.selected = selectedTransaction;
  }
  search(): void {
    this.searchEvent.emit();
  }
}
