import {UUID} from '../../types/uuid';
import {BaseDTO} from '../modules/shared/models/base-models';

export interface PowerBiAuthority extends BaseDTO {
  note: string;
  expirationTime: Date;
  scope: keyof typeof PowerBiScope;
  buildings: UUID[];
  lastUsed?: Date; // TODO: replace with lastUsed
}

export interface PowerBiAuthorityGenerateResponse {
  apiKey: UUID;
}

export enum PowerBiScope {
  BUILDING,
  ENTERPRISE
}

export interface PowerBiBuilding extends BaseDTO {
  name: string;
}
