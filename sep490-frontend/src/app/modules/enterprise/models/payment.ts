import {BaseDTO} from '../../shared/models/base-models';
import {PaymentStatus} from '../enums/payment-status';

export interface PaymentDTO extends BaseDTO {
  createdDate: Date;
  status: keyof typeof PaymentStatus;
  amount: bigint;
  bin: string;
  numberOfCredits: number;
  accountNumber: string;
  accountName: string;
  description: string;
  orderCode: number;
  currency: string;
  paymentLinkId: string;
  payOSStatus: string;
  expiredAt: number;
  checkoutUrl: string;
  qrCode: string;
}
