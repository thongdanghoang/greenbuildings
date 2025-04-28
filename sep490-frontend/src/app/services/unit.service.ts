import {Injectable} from '@angular/core';
import {EmissionUnit, UnitCategory} from '../models/shared-models';

@Injectable({
  providedIn: 'root'
})
export class UnitService {
  constructor() {}

  getAllUnit(): EmissionUnit[] {
    return Object.values(EmissionUnit);
  }

  getAllUnitCategory(): UnitCategory[] {
    return Object.values(UnitCategory);
  }

  // eslint-disable-next-line max-lines-per-function
  getSameUnitType(unit: EmissionUnit): EmissionUnit[] {
    switch (unit) {
      // Mass units
      case EmissionUnit.GRAM:
      case EmissionUnit.KILOGRAM:
      case EmissionUnit.MEGAGRAM:
      case EmissionUnit.GIGAGRAM:
      case EmissionUnit.TERAGRAM:
        return [
          EmissionUnit.GRAM,
          EmissionUnit.KILOGRAM,
          EmissionUnit.MEGAGRAM,
          EmissionUnit.GIGAGRAM,
          EmissionUnit.TERAGRAM
        ];

      // Volume units
      case EmissionUnit.MILLILITER:
      case EmissionUnit.CENTILITER:
      case EmissionUnit.DECILITER:
      case EmissionUnit.LITER:
      case EmissionUnit.CUBIC_METER:
      case EmissionUnit.UK_GALLON:
      case EmissionUnit.US_GALLON:
      case EmissionUnit.BARREL:
      case EmissionUnit.PECK:
      case EmissionUnit.THOUSAND_CUBIC_METER:
      case EmissionUnit.MILLION_CUBIC_METER:
        return [
          EmissionUnit.MILLILITER,
          EmissionUnit.CENTILITER,
          EmissionUnit.DECILITER,
          EmissionUnit.LITER,
          EmissionUnit.CUBIC_METER,
          EmissionUnit.UK_GALLON,
          EmissionUnit.US_GALLON,
          EmissionUnit.BARREL,
          EmissionUnit.PECK,
          EmissionUnit.THOUSAND_CUBIC_METER,
          EmissionUnit.MILLION_CUBIC_METER
        ];

      // Electric units
      case EmissionUnit.WH:
      case EmissionUnit.KWH:
        return [EmissionUnit.WH, EmissionUnit.KWH];

      // Energy units
      case EmissionUnit.TERAJOULE:
      case EmissionUnit.GIGAJOULE:
      case EmissionUnit.MEGAJOULE:
        return [
          EmissionUnit.TERAJOULE,
          EmissionUnit.GIGAJOULE,
          EmissionUnit.MEGAJOULE
        ];

      // Other units
      case EmissionUnit.PERCENT:
        return [EmissionUnit.PERCENT];

      case EmissionUnit.PIECES:
        return [EmissionUnit.PIECES];

      default:
        return [];
    }
  }

  // eslint-disable-next-line max-lines-per-function
  getUnitType(unit: EmissionUnit): string {
    switch (unit) {
      // Mass units
      case EmissionUnit.GRAM:
      case EmissionUnit.KILOGRAM:
      case EmissionUnit.MEGAGRAM:
      case EmissionUnit.GIGAGRAM:
      case EmissionUnit.TERAGRAM:
        return UnitCategory.MASS;

      // Volume units
      case EmissionUnit.MILLILITER:
      case EmissionUnit.CENTILITER:
      case EmissionUnit.DECILITER:
      case EmissionUnit.LITER:
      case EmissionUnit.CUBIC_METER:
      case EmissionUnit.UK_GALLON:
      case EmissionUnit.US_GALLON:
      case EmissionUnit.BARREL:
      case EmissionUnit.PECK:
      case EmissionUnit.THOUSAND_CUBIC_METER:
      case EmissionUnit.MILLION_CUBIC_METER:
        return UnitCategory.VOLUME;

      // Electric units
      case EmissionUnit.WH:
      case EmissionUnit.KWH:
        return UnitCategory.ELECTRIC;

      // Energy units
      case EmissionUnit.TERAJOULE:
      case EmissionUnit.GIGAJOULE:
      case EmissionUnit.MEGAJOULE:
        return UnitCategory.ENERGY;

      // Other units
      case EmissionUnit.PERCENT:
      case EmissionUnit.PIECES:
        return UnitCategory.PIECES;

      default:
        return UnitCategory.UNKNOWN;
    }
  }
}
