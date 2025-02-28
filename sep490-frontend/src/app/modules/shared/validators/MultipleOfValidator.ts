import {AbstractControl, ValidationErrors, ValidatorFn} from '@angular/forms';

export function multipleOfValidator(multiple: number): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;
    if (value % multiple !== 0) {
      return {multipleOf: {requiredMultiple: multiple, actual: value}};
    }
    return null; // Valid case
  };
}
