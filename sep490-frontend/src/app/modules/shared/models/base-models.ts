import {UUID} from '../../../../types/uuid';

export interface BaseDTO {
  id: UUID;
  version: number;
}

export interface SearchPageDto {
  pageNumber: number;
  pageSize: number;
}

export interface SortDto {
  field: string;
  direction: 'ASC' | 'DESC';
}

export interface SearchCriteriaDto<C> {
  page: SearchPageDto;
  criteria?: C;
  sort?: SortDto;
}

export interface SearchResultDto<R> {
  results: R[];
  totalElements: number;
}

export interface KeyValue<T = any> {
  [key: string]: T;
}

export interface DateRange<D = Date | string> {
  from: D;
  to: D;
}

export interface BusinessErrorParam {
  key: string;
  value: string;
}

export interface BusinessErrorResponse {
  correlationId: string;
  field: string;
  i18nKey: string;
  args: BusinessErrorParam[];
}

export interface AutoCompleteSearchDto<T> {
  value: T;
  searchText: string;
}

export interface SelectableItem<V> {
  label: string;
  value: V;
  disabled?: boolean;
}

export interface EnumOptions {
  name: string;
  code: string;
  i18nCode: string;
}

export type AutoCompleteDto<V> = SelectableItem<V>;
export type DropdownItem<V> = SelectableItem<V>;
