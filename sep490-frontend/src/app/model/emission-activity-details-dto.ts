/**
 * OpenAPI definition
 *
 *
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import {EmissionFactorDTO} from './emission-factor-dto';
import {ActivityTypeDTO} from './activity-type-dto';
import {BuildingGroupDTO} from './building-group-dto';
import {EmissionActivityRecordDTO} from './emission-activity-record-dto';

export interface EmissionActivityDetailsDTO {
  id?: string;
  version?: number;
  buildingId: string;
  buildingGroup?: BuildingGroupDTO;
  emissionFactor: EmissionFactorDTO;
  records?: Set<EmissionActivityRecordDTO>;
  name?: string;
  type?: ActivityTypeDTO;
  category?: string;
  quantity?: number;
  description?: string;
}
