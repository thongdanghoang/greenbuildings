import {UUID} from '../../types/uuid';
import {BaseDTO} from '../modules/shared/models/base-models';
import {BuildingPermissionRole} from './building-permission-role';
import {UserRole} from './role-names';
import {UserScope} from './user-scope';

export interface EnterpriseUserDetails extends BaseDTO {
  createdDate: Date;
  email: string;
  emailVerified: boolean;
  firstName: string;
  lastName: string;
  role: keyof typeof UserRole;
  scope: keyof typeof UserScope;
  phone: string;
  buildingPermissions: BuildingPermission[];
}

export interface EnterpriseUser extends BaseDTO {
  email: string;
  name: string;
  role: UserRole;
  scope: UserScope;
}

export interface BuildingPermission {
  buildingId?: UUID;
  role: keyof typeof BuildingPermissionRole;
}

export interface NewEnterpriseDTO {
  name: string;
  enterpriseEmail: string;
  hotline: string;
  role: keyof typeof UserRole;
}

export interface ValidateOTPRequest {
  otpCode: string;
}
