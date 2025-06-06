import {UUID} from '../../types/uuid';

export interface BuildingGroupCriteria {
  buildingId: UUID;
  name?: string;
}

export interface CreateBuildingGroupDTO {
  name: string;
  description: string;
  buildingId: UUID;
  tenantEmail: string;
}

export interface InviteTenantToBuildingGroup {
  buildingId: UUID;
  email: string;
  selectedGroupId: UUID;
}
