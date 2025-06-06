/**
 * OpenAPI definition
 *
 *
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

export interface BuildingPermissionDTO {
  buildingId?: string;
  role: BuildingPermissionDTO.RoleEnum;
}
export namespace BuildingPermissionDTO {
  export const RoleEnum = {
    MANAGER: 'MANAGER',
    AUDITOR: 'AUDITOR',
    STAFF: 'STAFF'
  } as const;
  export type RoleEnum = (typeof RoleEnum)[keyof typeof RoleEnum];
}
