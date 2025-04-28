import {UUID} from '../../types/uuid';

export interface ActivityTypeCriteria {
  criteria: string;
}

export interface ActivitySearchCriteria {
  buildingGroupId: UUID;
  name?: string;
}
