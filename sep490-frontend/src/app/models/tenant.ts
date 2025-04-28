import {InvitationStatus} from '@models/enterprise';
import {UUID} from '../../types/uuid';

export interface InvitationResponse {
  id: UUID;
  status: InvitationStatus;
}

export interface InvitationSearchCriteria {
  enterpriseId: UUID;
  buildingId?: UUID;
  buildingGroupId?: UUID;
  tenantEmail: string;
  status?: InvitationStatus;
}
