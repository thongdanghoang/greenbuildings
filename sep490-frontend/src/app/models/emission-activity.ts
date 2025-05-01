import {UUID} from '../../types/uuid';

export interface ActivityTypeCriteria {
  name: string;
  buildingId: UUID;
}

export interface ActivitySearchCriteria {
  buildingGroupId?: UUID;
  buildingId?: UUID;
  name?: string;
}
