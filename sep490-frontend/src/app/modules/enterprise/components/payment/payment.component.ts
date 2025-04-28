import {
  Component,
  EventEmitter,
  OnInit,
  TemplateRef,
  ViewChild
} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AppRoutingConstants} from '../../../../app-routing.constant';
import {SubscriptionAwareComponent} from '../../../core/subscription-aware.component';
import {TableTemplateColumn} from '../../../shared/components/table-template/table-template.component';
import {
  SearchCriteriaDto,
  SearchResultDto,
  SortDto
} from '../../../shared/models/base-models';
import {Observable} from 'rxjs';
import {ApplicationService} from '../../../core/services/application.service';
import {PaymentStatus} from '../../enums/payment-status';
import {PaymentDTO} from '../../models/payment';
import {PaymentService} from '../../services/payment.service';
import {WalletService} from '../../services/wallet.service';
import {
  DialogService,
  DynamicDialogConfig,
  DynamicDialogRef
} from 'primeng/dynamicdialog';
import {UUID} from '../../../../../types/uuid';
import {PaymentDetailDialogComponent} from '../../dialog/payment-detail-dialog/payment-detail-dialog.component';

export interface PaymentCriteria {
  criteria: string;
  // specific criteria such as name, category, etc.
}

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrl: './payment.component.css'
})
export class PaymentComponent
  extends SubscriptionAwareComponent
  implements OnInit
{
  @ViewChild('statusTemplate', {static: true})
  statusTemplate!: TemplateRef<any>;
  @ViewChild('amountTemplate', {static: true})
  amountTemplate!: TemplateRef<any>;
  @ViewChild('actionsTemplate', {static: true})
  actionsTemplate!: TemplateRef<any>;
  ref: DynamicDialogRef | undefined;
  cols: TableTemplateColumn[] = [];
  paymentCriteria!: PaymentCriteria;
  sort!: SortDto;
  balance: number = 0;
  triggerSearch: EventEmitter<void> = new EventEmitter();

  protected fetchData!: (
    criteria: SearchCriteriaDto<PaymentCriteria>
  ) => Observable<SearchResultDto<PaymentDTO>>;
  protected selected: PaymentDTO[] = [];
  private readonly payOsOrderCodeField = 'orderCode';

  constructor(
    protected readonly applicationService: ApplicationService,
    private readonly paymentService: PaymentService,
    private readonly walletService: WalletService,
    private readonly router: Router,
    private readonly route: ActivatedRoute,
    private readonly dialogService: DialogService
  ) {
    super();
  }

  ngOnInit(): void {
    this.sort = {
      field: 'createdDate',
      direction: 'DESC'
    };
    this.paymentCriteria = {criteria: ''};
    this.buildCols();
    this.fetchData = this.paymentService.getPayments.bind(this.paymentService);
    const params = this.route.snapshot.queryParamMap;
    if (params.get(this.payOsOrderCodeField)) {
      this.updatePayment(params.get(this.payOsOrderCodeField));
    } else {
      this.getBalance();
    }
  }

  updatePayment(orderCode: any): void {
    this.paymentService.updatePayment(orderCode).subscribe(() => {
      this.getBalance();
      this.triggerSearch.emit();
    });
  }

  onSelectionChange(selectedUsers: PaymentDTO[]): void {
    this.selected = selectedUsers;
  }

  buildCols(): void {
    this.cols.push({
      field: 'createdDate',
      header: 'payment.history.table.date',
      sortable: true
    });
    this.cols.push({
      field: 'status',
      header: 'payment.history.table.status',
      templateRef: this.statusTemplate
    });
    this.cols.push({
      field: 'numberOfCredits',
      header: 'payment.history.table.amount',
      sortable: true,
      templateRef: this.amountTemplate
    });
    this.cols.push({
      field: 'actions',
      header: '',
      templateRef: this.actionsTemplate
    });
  }

  getStatusClass(status: string): string {
    switch (status) {
      case PaymentStatus[PaymentStatus.PENDING]:
        return 'bg-yellow-100 text-yellow-800 font-medium px-2 py-1 rounded-md';
      case PaymentStatus[PaymentStatus.PAID]:
        return 'bg-green-100 text-green-800 font-medium px-2 py-1 rounded-md';
      case PaymentStatus[PaymentStatus.PROCESSING]:
        return 'bg-blue-100 text-blue-800 font-medium px-2 py-1 rounded-md';
      case PaymentStatus[PaymentStatus.CANCELLED]:
        return 'bg-red-100 text-red-800 font-medium px-2 py-1 rounded-md';
      default:
        return 'bg-gray-100 text-gray-800 font-medium px-2 py-1 rounded-md';
    }
  }

  openNewActivityDialog(paymentId?: UUID): void {
    // Made emissionId optional with ?
    this.selected = []; // Clear local selection
    this.triggerSearch.emit();
    const config: DynamicDialogConfig<UUID | undefined> = {
      // Allow undefined in config.data
      data: paymentId, // Will be undefined for add, UUID for edit
      closeOnEscape: true,
      dismissableMask: true,
      showHeader: false,
      modal: true
    };

    // Clean up previous dialog if it exists
    if (this.ref) {
      this.ref.close();
    }

    // Open the dialog with EmissionSourceDialogComponent
    this.ref = this.dialogService.open(PaymentDetailDialogComponent, config);
    this.ref.onClose.subscribe((result: boolean | undefined) => {
      if (result) {
        this.triggerSearch.emit(); // Refresh the list if dialog closed with a result
      }
    });
  }

  getBalance(): void {
    this.registerSubscription(
      this.walletService.getWalletBalance().subscribe(result => {
        this.balance = result;
      })
    );
  }

  onViewDetail(rowData: PaymentDTO): void {
    this.selected = [rowData];
    const emissionId = this.selected[0].id;
    this.openNewActivityDialog(emissionId);
  }

  navigateToSubscription(): void {
    void this.router.navigate([
      '/',
      AppRoutingConstants.ENTERPRISE_PATH,
      AppRoutingConstants.PLAN_PATH
    ]);
  }
}
