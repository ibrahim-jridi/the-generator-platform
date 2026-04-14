import { AbstractControl, AsyncValidatorFn, ValidationErrors, ValidatorFn } from '@angular/forms';
import { Observable, of } from 'rxjs';

export default class Validation {
  static match(controlName: string, checkControlName: string): ValidatorFn {
    return (controls: AbstractControl) => {
      const control = controls.get(controlName);
      const checkControl = controls.get(checkControlName);

      if (checkControl.errors && !checkControl.errors['matching']) {
        return null;
      }

      if (control.value !== checkControl.value) {
        controls.get(checkControlName).setErrors({ matching: true });
        return { matching: true };
      } else {
        return null;
      }
    };
  }

  static nonZero(control: AbstractControl): { [key: string]: any } {
    if (Number(control.value) < 0) {
      return { nonZero: true };
    } else {
      return null;
    }
  }

  static isPercentage(control: AbstractControl): { [key: string]: any } {
    if (control.value == null || control.value === undefined || control.value == '') {
      return null;
    }
    const value = control.value.toString().replace('%', '');
    // const value = parseFloat(control.value.toString().replace('%', ''));
    // const value = parseFloat(control.value.replace('%', ''));
    if (isNaN(value) && control.value != '') {
      return { invalidValue: true };
    }
    if (value < 1 || value > 100) {
      return { isPercentage: true };
    } else {
      return null;
    }
  }

  static phoneNumberValidator(): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors | null> => {
      const phoneNumber = control.value;
      if (!phoneNumber) {
        return of(null);
      }
      const regionalCodes = ['2', '4', '5', '7', '9'];
      const startsWithRegionalCode = regionalCodes.some((code) => phoneNumber.startsWith(code));

      if (!phoneNumber.startsWith('+') && !startsWithRegionalCode) {
        return of({ invalidRegionalCode: true });
      }
      const isInternationalFormat = phoneNumber.startsWith('+');
      const expectedLength = isInternationalFormat ? [10, 11, 12, 13] : [8];

      if (!expectedLength.includes(phoneNumber.length)) {
        return of({ invalidLength: true });
      }
      return of(null);
    };
  }

  static cinStartWithValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;

      if (value && !/^[01]/.test(value)) {
        return { cinStartWithError: true };
      }
      return null;
    };
  }


  static noSpecialCharacters(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value: string = control.value;
      if (!value) return null;
      const specialCharRegex = /[^a-zA-Z0-9]/;

      return specialCharRegex.test(value) ? { specialCharactersNotAllowed: true } : null;
    };
  }


  static alphanumericButNotOnlyLetters(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value: string = control.value;
      if (!value) return null;
      if (!/^[A-Z0-9]+$/.test(value)) {
        return { notAlphanumeric: true };
      }
      if (/^[A-Z]+$/.test(value)) {
        return { onlyLetters: true };
      }

      return null;
    };
  }

  static blockNegativeInput(event: KeyboardEvent | ClipboardEvent) {
    if (event instanceof KeyboardEvent) {
      if (event.key === '-' || event.key === '.' || event.key === '+' || event.key === 'e') {
        event.preventDefault();
      }
    } else if (event instanceof ClipboardEvent) {
      event.preventDefault();
      const clipboardData = event.clipboardData;
      if (clipboardData) {
        const pastedText = clipboardData.getData('text');
        const pastedNumber = parseFloat(pastedText);
        if (!isNaN(pastedNumber) && pastedNumber >= 0) {
          document.execCommand('insertText', false, pastedNumber.toString());
        }
      }
    }
  }
}
