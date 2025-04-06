import {UUID} from '../../../../types/uuid';
import {BaseDTO} from '../../shared/models/base-models';
import {BuildingPermissionRole} from '../enums/building-permission-role';
import {UserRole} from '../enums/role-names';
import {UserScope} from '../enums/user-scope';

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
