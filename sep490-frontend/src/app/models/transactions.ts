import {BaseDTO} from '@shared/models/base-models';
import {TransactionType} from '@models/enterprise';

export interface TransactionCriteria {
  criteria: string;
}
export interface TransactionAdminCriteria {
  criteria: string;
}

export interface TransactionEnterpriseAdminDTO extends BaseDTO {
  createdDate: Date;
  enterpriseName: string;
  buildingName: string;
  amount: number;
  months: number;
  numberOfDevices: number;
  transactionType: keyof typeof TransactionType;
}
