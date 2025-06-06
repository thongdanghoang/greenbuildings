/**
 * OpenAPI definition
 *
 *
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

export interface PaymentDTO {
  id?: string;
  createdDate?: string;
  status?: PaymentDTO.StatusEnum;
  amount?: number;
  numberOfCredits?: number;
  bin?: string;
  accountNumber?: string;
  accountName?: string;
  description?: string;
  orderCode?: number;
  currency?: string;
  paymentLinkId?: string;
  payOSStatus?: string;
  expiredAt?: number;
  checkoutUrl?: string;
  qrCode?: string;
}
export namespace PaymentDTO {
  export const StatusEnum = {
    PENDING: 'PENDING',
    PAID: 'PAID',
    PROCESSING: 'PROCESSING',
    CANCELLED: 'CANCELLED'
  } as const;
  export type StatusEnum = (typeof StatusEnum)[keyof typeof StatusEnum];
}
