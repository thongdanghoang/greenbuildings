import {Component, EventEmitter, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {SubscriptionAwareComponent} from '@shared/directives/subscription-aware.component';
import {SearchCriteriaDto, SearchResultDto} from '@shared/models/base-models';
import {Observable} from 'rxjs';
import {TableTemplateColumn} from '@shared/components/table-template/table-template.component';
import {ApplicationService} from '@services/application.service';
import {ToastProvider} from '@shared/services/toast-provider';
import {ModalProvider} from '@shared/services/modal-provider';
import {TranslateService} from '@ngx-translate/core';
import {DialogService} from 'primeng/dynamicdialog';
import {PaymentAdminCriteria, PaymentEnterpriseAdminDTO} from '@models/payment';
import {PaymentService} from '@services/payment.service';
import {PaymentStatus} from '@models/payment-status';

@Component({
  selector: 'app-payment-enterprise-management',
  templateUrl: './payment-enterprise-management.component.html',
  styleUrl: './payment-enterprise-management.component.css'
})
export class PaymentEnterpriseManagementComponent extends SubscriptionAwareComponent implements OnInit {
  @ViewChild('statusTemplate', {static: true})
  statusTemplate!: TemplateRef<any>;
  @ViewChild('amountTemplate', {static: true})
  amountTemplate!: TemplateRef<any>;
  protected fetchPayments!: (
    criteria: SearchCriteriaDto<PaymentAdminCriteria>
  ) => Observable<SearchResultDto<PaymentEnterpriseAdminDTO>>;
  protected cols: TableTemplateColumn[] = [];
  protected readonly searchEvent: EventEmitter<void> = new EventEmitter();
  protected readonly clearSelectedEvent: EventEmitter<void> = new EventEmitter();
  protected selected: PaymentEnterpriseAdminDTO[] = [];
  protected searchCriteria: PaymentAdminCriteria = {criteria: ''};
  constructor(
    protected readonly applicationService: ApplicationService,
    private readonly paymentService: PaymentService,
    private readonly messageService: ToastProvider,
    private readonly modalProvider: ModalProvider,
    private readonly translate: TranslateService,
    private readonly dialogService: DialogService
  ) {
    super();
  }
  ngOnInit(): void {
    this.buildCols();
    this.fetchPayments = this.paymentService.getPaymentsAdmin.bind(this.paymentService);
  }

  buildCols(): void {
    this.cols.push({
      field: 'createdDate',
      header: 'payment.history.table.date',
      sortable: true
    });
    this.cols.push({
      field: 'enterpriseName',
      header: 'enterprise.create.name',
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
  }

  getStatusClass(status: string): string {
    switch (status) {
      case PaymentStatus[PaymentStatus.PENDING]:
        return 'bg-yellow-100 text-yellow-800';
      case PaymentStatus[PaymentStatus.PAID]:
        return 'bg-green-100 text-green-800';
      case PaymentStatus[PaymentStatus.PROCESSING]:
        return 'bg-blue-100 text-blue-800';
      case PaymentStatus[PaymentStatus.CANCELLED]:
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  }

  onSelectionChange(selectedTransaction: PaymentEnterpriseAdminDTO[]): void {
    this.selected = selectedTransaction;
  }
  search(): void {
    this.searchEvent.emit();
  }
}
