import {BaseDTO} from '../../shared/models/base-models';
import {UUID} from '../../../../types/uuid';
import {EmissionUnit} from '../../shared/models/shared-models';

export interface Building extends BaseDTO {
  name: string;
  address?: string;
  activated?: boolean;
  latitude: number;
  longitude: number;
  subscriptionDTO?: Subscription;
}

export interface BuildingDetails extends BaseDTO {
  name: string;
  numberOfDevices: number;
  latitude: number;
  longitude: number;
  address: string;
  subscriptionDTO?: Subscription;
  emissionActivities: EmissionActivity[];
}

export interface EmissionActivity extends BaseDTO {
  records: EmissionActivityRecord[];
  buildingID: UUID;
  emissionFactorID: UUID;
  emissionSourceID: UUID;
  name: string;
  type: string;
  category: string;
  description: string;
  quantity: number;
}

export interface EmissionActivityRecord extends BaseDTO {
  value: number;
  unit: keyof typeof EmissionUnit;
  startDate: Date;
  endDate: Date;
}

export interface CreditPackage extends BaseDTO {
  numberOfCredits: number;
  price: number;
}

export enum CreditConvertType {
  MONTH,
  DEVICE
}

export enum TransactionType {
  NEW_PURCHASE,
  UPGRADE
}

export interface CreditConvertRatio extends BaseDTO {
  ratio: number;
  convertType: keyof typeof CreditConvertType;
}

export interface SubscribeRequest extends BaseDTO {
  buildingId: UUID;
  months: number;
  numberOfDevices: number;
  monthRatio: number;
  deviceRatio: number;
  type: keyof typeof TransactionType;
}

export interface Subscription extends BaseDTO {
  startDate?: Date;
  endDate?: Date;
  maxNumberOfDevices?: number;
}
