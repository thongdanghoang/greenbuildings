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
import {EmissionActivityRecordDTO} from './emission-activity-record-dto';

export interface EmissionActivityTableView {
  id?: string;
  version?: number;
  records?: Set<EmissionActivityRecordDTO>;
  emissionFactor?: EmissionFactorDTO;
  name?: string;
  type?: ActivityTypeDTO;
  category?: string;
  description?: string;
}
