import {BaseDTO} from './base-models';

export interface FuelDTO extends BaseDTO {
  nameVN: string;
  nameEN: string;
  nameZH: string;
}

export enum EmissionUnit {
  // Mass
  GRAM = 'GRAM',
  KILOGRAM = 'KILOGRAM',
  MEGAGRAM = 'MEGAGRAM',
  GIGAGRAM = 'GIGAGRAM',
  TERAGRAM = 'TERAGRAM',

  // Volume
  MILLILITER = 'MILLILITER',
  CENTILITER = 'CENTILITER',
  DECILITER = 'DECILITER',
  LITER = 'LITER',
  CUBIC_METER = 'CUBIC_METER',
  UK_GALLON = 'UK_GALLON',
  US_GALLON = 'US_GALLON',
  BARREL = 'BARREL',
  PECK = 'PECK',
  THOUSAND_CUBIC_METER = 'THOUSAND_CUBIC_METER',
  MILLION_CUBIC_METER = 'MILLION_CUBIC_METER',

  // Electric
  WH = 'WH',
  KWH = 'KWH',

  // Energy
  TERAJOULE = 'TERAJOULE',
  GIGAJOULE = 'GIGAJOULE',
  MEGAJOULE = 'MEGAJOULE',

  // Other
  PERCENT = 'PERCENT',
  PIECES = 'PIECES'
}

export enum UnitCategory {
  MASS = 'MASS',
  VOLUME = 'VOLUME',
  ELECTRIC = 'ELECTRIC',
  ENERGY = 'ENERGY',
  PERCENT = 'PERCENT',
  PIECES = 'PIECES',
  UNKNOWN = 'UNKNOWN'
}

export interface EmissionSourceDTO extends BaseDTO {
  nameVN: string;
  nameEN: string;
  nameZH: string;
}

export interface EnergyConversionDTO extends BaseDTO {
  fuel: FuelDTO;
  conversionValue: number;
  conversionUnitNumerator: EmissionUnit;
  conversionUnitDenominator: EmissionUnit;
}

export interface EmissionFactorDTO extends BaseDTO {
  co2: number;
  ch4: number;
  n2o: number;
  nameVN: string;
  nameEN: string;
  nameZH: string;
  emissionUnitNumerator: EmissionUnit;
  emissionUnitDenominator: EmissionUnit;
  emissionSourceDTO: EmissionSourceDTO;
  description?: string;
  validFrom: Date;
  validTo?: Date;
  isDirectEmission: boolean;
  energyConversionDTO?: EnergyConversionDTO;
  active: boolean;
}
