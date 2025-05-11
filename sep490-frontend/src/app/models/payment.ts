import {BaseDTO} from '../modules/shared/models/base-models';
import {CreditPackageVersion} from './enterprise';
import {PaymentStatus} from './payment-status';

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

export interface PaymentDetailDTO extends BaseDTO {
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
  enterpriseDTO: EnterpriseDTO;
  creditPackageVersionDTO: CreditPackageVersion;
}

export interface EnterpriseDTO extends BaseDTO {
  name: string;
  email: string;
  hotline: string;
}
export interface PaymentEnterpriseAdminDTO extends BaseDTO {
  createdDate: Date;
  status: keyof typeof PaymentStatus;
  numberOfCredits: number;
  enterpriseName: string;
}
export interface PaymentAdminCriteria {
  criteria: string;
}
