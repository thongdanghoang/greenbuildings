import {
  Component,
  EventEmitter,
  OnInit,
  TemplateRef,
  ViewChild
} from '@angular/core';
import {TransactionCriteria} from '@models/transactions';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {Observable} from 'rxjs';
import {UUID} from '../../../../../types/uuid';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {TransactionDTO} from '@models/enterprise';
import {TransactionService} from '@services/transaction.service';
import {ApplicationService} from '../../../core/services/application.service';
import {SubscriptionAwareComponent} from '../../../core/subscription-aware.component';
import {TableTemplateColumn} from '@shared/components/table-template/table-template.component';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';

@Component({
  selector: 'app-credit-deduction-history-dialog',
  templateUrl: './credit-deduction-history-dialog.component.html',
  styleUrl: './credit-deduction-history-dialog.component.css'
})
export class CreditDeductionHistoryDialogComponent
  extends SubscriptionAwareComponent
  implements OnInit
{
  @ViewChild('typeTemplate', {static: true})
  typeTemplate!: TemplateRef<any>;
  protected readonly AppRoutingConstants = AppRoutingConstants;
  protected fetchTransaction!: (
    criteria: SearchCriteriaDto<TransactionCriteria>
  ) => Observable<SearchResultDto<TransactionDTO>>;
  protected cols: TableTemplateColumn[] = [];
  protected readonly searchEvent: EventEmitter<void> = new EventEmitter();
  protected readonly clearSelectedEvent: EventEmitter<void> =
    new EventEmitter();
  protected selected: TransactionDTO[] = [];
  protected searchCriteria: TransactionCriteria = {criteria: ''};
  private readonly buildingId: UUID | undefined;

  constructor(
    protected readonly applicationService: ApplicationService,
    private readonly transactionService: TransactionService,
    protected readonly ref: DynamicDialogRef,
    public config: DynamicDialogConfig<UUID>
  ) {
    super();
    this.buildingId = config.data;
  }

  ngOnInit(): void {
    this.buildCols();
    this.fetchTransaction = (
      criteria: SearchCriteriaDto<TransactionCriteria>
    ): Observable<SearchResultDto<TransactionDTO>> =>
      this.transactionService.search(criteria, this.buildingId);
  }

  buildCols(): void {
    this.cols = [
      {
        field: 'createdDate',
        header: 'useCreditHistory.table.createdDate'
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

  onSelectionChange(selectedTransaction: TransactionDTO[]): void {
    this.selected = selectedTransaction;
  }
}
