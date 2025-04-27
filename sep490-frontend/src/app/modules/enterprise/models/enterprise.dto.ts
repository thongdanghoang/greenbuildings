import {UUID} from '../../../../types/uuid';
import {BuildingPermissionRole} from '../../authorization/enums/building-permission-role';
import {BaseDTO} from '../../shared/models/base-models';
import {
  EmissionFactorDTO,
  EmissionUnit
} from '../../shared/models/shared-models';

export interface Building extends BaseDTO {
  name: string;
  address?: string;
  activated?: boolean;
  latitude: number;
  longitude: number;
  subscriptionDTO?: Subscription;
}

export interface OverviewBuildingDTO {
  numberOfGroups: number;
  numberOfCorporationTenant: number;
  numberOfActivities: number;
}

export interface BuildingDetails extends BaseDTO {
  name: string;
  latitude: number;
  longitude: number;
  address: string;
  subscriptionDTO?: Subscription;
  buildingGroups: BuildingGroup[];
}

export interface Tenant extends BaseDTO {
  name: string;
  email: string;
  hotline: string;
}

export interface TenantDetailDTO extends BaseDTO {
  name: string;
  email: string;
  hotline: string;
}

export enum InvitationStatus {
  PENDING = 'PENDING',
  ACCEPTED = 'ACCEPTED',
  REJECTED = 'REJECTED'
}

export interface InvitationDTO extends BaseDTO {
  buildingGroup: BuildingGroup;
  email: string;
  status: InvitationStatus;
}

export interface BuildingGroup extends BaseDTO {
  name: string;
  description: string;
  building: Building;
  tenant: Tenant;
  emissionActivities: EmissionActivity[];
}

export interface EmissionActivity extends BaseDTO {
  records: EmissionActivityRecord[];
  buildingID: UUID;
  emissionFactorID: UUID;
  name: string;
  type: ActivityType;
  category: string;
  description: string;
}

export interface CreateNewActivityDTO extends BaseDTO {
  buildingGroupID: UUID;
  emissionFactorID: UUID;
  name: string;
  type: string;
  category: string;
  description: string;
}

export interface ActivityType extends BaseDTO {
  name: string;
  description: string;
  tenantID: UUID;
}

export interface DownloadReport {
  buildingID: UUID;
  startDate: Date;
  endDate: Date;
  selectedActivities: UUID[];
}

export interface EmissionActivityDetails extends BaseDTO {
  buildingId: UUID;
  records: EmissionActivityRecord[];
  buildingGroup: BuildingGroup;
  emissionFactor: EmissionFactorDTO;
  name: string;
  type: ActivityType;
  category: string;
  description: string;
  quantity: number;
}

export interface EmissionActivityRecord extends BaseDTO {
  value: number;
  unit: keyof typeof EmissionUnit;
  startDate: Date;
  endDate: Date;
  file: EmissionActivityRecordFile;
  ghg: number;
}

export interface EmissionActivityRecordFile extends BaseDTO {
  fileName: string;
  contentType: string;
}

export interface EmissionSource extends BaseDTO {
  nameVN: string;
  nameEN: string;
  nameZH: string;
}

export interface FuelConversion extends BaseDTO {
  nameVN: string;
  nameEN: string;
  nameZH: string;
}

export interface CreditPackage extends BaseDTO {
  numberOfCredits: number;
  price: number;
  convertedPriceCurrency: number;
  discount: number;
}

export interface CreditPackageAdmin extends BaseDTO {
  numberOfCredits: number;
  price: number;
  active: boolean;
  packageVersionDTOList: CreditPackageVersion[];
}

export interface CreditPackageVersion extends BaseDTO {
  numberOfCredits: number;
  price: number;
  active: boolean;
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
export interface UserByBuilding {
  id: UUID;
  name: string;
  role: keyof typeof BuildingPermissionRole;
}
export interface TransactionDTO extends BaseDTO {
  createdDate: Date;
  amount: number;
  months: number;
  numberOfDevices: number;
  transactionType: keyof typeof TransactionType;
}
