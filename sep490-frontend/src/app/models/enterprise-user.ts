import {BaseDTO} from '../modules/shared/models/base-models';
import {UserRole} from './role-names';
import {UserScope} from './user-scope';

export interface EnterpriseUserDetails extends BaseDTO {
  createdDate: Date;
  email: string;
  emailVerified: boolean;
  firstName: string;
  lastName: string;
  role: keyof typeof UserRole;
  phone: string;
}

export interface EnterpriseUser extends BaseDTO {
  email: string;
  name: string;
  emailVerified: boolean;
  role: UserRole;
  scope: UserScope;
}

export interface ValidateOTPRequest {
  otpCode: string;
}
