import {UUID} from '../../../../types/uuid';
import {BaseDTO} from '../../shared/models/base-models';

export interface PowerBiAuthority extends BaseDTO {
  note: string;
  expirationTime: Date;
  lastUsed?: Date; // TODO: replace with lastUsed
  scopes?: string; // TODO: replace with scopes
}

export interface PowerBiAuthorityGenerateRequest {
  note: string;
  expirationTime: string;
}

export interface PowerBiAuthorityGenerateResponse {
  apiKey: UUID;
}
