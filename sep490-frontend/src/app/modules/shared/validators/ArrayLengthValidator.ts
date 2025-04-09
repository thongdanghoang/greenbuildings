import {AbstractControl, ValidationErrors, ValidatorFn} from '@angular/forms';

export function arrayLength(requiredLength: number): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;
    if (Array.isArray(value) && value.length === requiredLength) {
      return null;
    }
    return {arrayLength: {requiredLength, actualLength: value?.length ?? 0}};
  };
}
