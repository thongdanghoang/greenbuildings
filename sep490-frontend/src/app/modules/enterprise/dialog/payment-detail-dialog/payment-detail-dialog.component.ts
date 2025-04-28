import {Component, OnDestroy, OnInit} from '@angular/core';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {Subscription} from 'rxjs';
import {formatCurrency} from '@angular/common';
import {PaymentDetailDTO} from '../../../../models/payment';
import {PaymentStatus} from '../../../../models/payment-status';
import {PaymentService} from '../../../../services/payment.service';

@Component({
  selector: 'app-payment-detail-dialog',
  templateUrl: './payment-detail-dialog.component.html',
  styleUrl: './payment-detail-dialog.component.css'
})
export class PaymentDetailDialogComponent implements OnInit, OnDestroy {
  paymentDetail: PaymentDetailDTO | undefined;
  loading: boolean = true;
  private readonly subscription: Subscription = new Subscription();

  constructor(
    public ref: DynamicDialogRef,
    public config: DynamicDialogConfig,
    private readonly paymentService: PaymentService
  ) {}

  ngOnInit(): void {
    const paymentId = this.config.data; // UUID from PaymentComponent
    if (paymentId) {
      this.loadPaymentDetail(paymentId);
    } else {
      this.loading = false;
      this.closeDialog();
    }
  }

  loadPaymentDetail(paymentId: string): void {
    this.subscription.add(
      this.paymentService.getPaymentDetail(paymentId).subscribe({
        next: (data: PaymentDetailDTO) => {
          this.paymentDetail = data;
          this.loading = false;
        },
        error: () => {
          this.loading = false;
          this.closeDialog();
        }
      })
    );
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

  formatAmount(amount: bigint): string {
    // Format bigint amount to readable currency (assuming VND for example)
    return formatCurrency(Number(amount), 'en-US', '', 'VND');
  }

  closeDialog(): void {
    this.ref.close();
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
