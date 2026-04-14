import { AfterViewInit, ChangeDetectorRef, Component, Input, OnInit, ViewChild } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginService } from '../../shared/services/login.service';
import { AppToastNotificationService } from '../../shared/services/appToastNotification.service';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import Validation from '../../shared/services/validation';
import { nationalitiesEnum } from '../../shared/enums/nationalities-enum';
import { User } from '../../shared/models/user.model';
import { NationalityList } from '../../shared/utils/Nationalities';
import { CountryISO } from 'ngx-intl-tel-input';
import { RegexConstants } from '../../shared/utils/regex-constants';
import { firstValueFrom, Subscription } from 'rxjs';
import { Constants } from '../../shared/utils/constants';
import { GenderEnum } from '../../shared/enums/gender.enum';
import { ModalService } from '../../shared/services/modal.service';
import { MatStepper } from '@angular/material/stepper';
import { LoaderService } from "../../shared/services/loader.service";
import { formatDate } from "@angular/common";
import { EhouweyaService } from "../../shared/services/ehouweya.service";
import { EhoweyaConfig } from "../../shared/models/ehoweyaConfig";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Country } from "ngx-intl-tel-input/lib/model/country.model";
import { Nationalities } from "../../shared/enums/nationality.enum";
import { UiModalComponent } from "../../theme/shared/components/modal/ui-modal/ui-modal.component";
import { UserService } from "../../shared/services/user.service";

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss'
})
export class SignupComponent implements OnInit {
  protected user = new User();
  public nationalityList = NationalityList;
  public selectedCountry: CountryISO = CountryISO.Tunisia;
  public phoneNumberPrefix: string = '+216';
  public countryFlagUrl: string = '';
  public countryISOValues = Object.values(CountryISO);
  informationFormGroup: FormGroup;
  private subscriptions: Subscription = new Subscription();
  fileName: string = '';
  genders = Object.values(GenderEnum);
  maxBirthDate: Date;
  minBirthDate: Date;
  protected readonly Nationalities = Nationalities;
  emailRegex: RegExp;
  public language: string;
  public currentLanguage: string;
  userCountry: Country;
  public countries: any[];

  constructor(
    private readonly _formBuilder: FormBuilder,
    private readonly router: Router,
    private readonly loginService: LoginService,
    private readonly toastrService: AppToastNotificationService,
    private readonly translatePipe: TranslatePipe,
    private readonly translateService: TranslateService,
    private readonly modalService: ModalService,
    private readonly loaderService: LoaderService,
    private readonly route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.currentLanguage = localStorage.getItem('language') || 'fr';
    this.language = this.currentLanguage.charAt(0).toUpperCase() + this.currentLanguage.slice(1).toLowerCase();

    this.emailRegex = /^([a-zA-Z]*([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z][a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,6}))$/;

    // Initialize form with all validators for Tunisian CIN registration
    this.informationFormGroup = this._formBuilder.group({
      lastName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      firstName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      gender: ['', Validators.required],
      birthDate: ['', Validators.required],
      age: [{ value: '', disabled: true }, Validators.required],
      phoneNumber: ['', [Validators.required, Validators.maxLength(8), Validators.minLength(8)]],
      address: ['', [Validators.required, Validators.pattern(RegexConstants.ADDRESS_LENGTH)]],
      email: ['', [Validators.required, Validators.email, Validators.maxLength(100), Validators.pattern(this.emailRegex)]],
      cin: ['', [Validators.required, Validators.maxLength(8), Validators.minLength(8), Validation.cinStartWithValidator()]],
      checkbox: [false, Validators.required]
    });

    // Set date constraints (must be at least 18 years old)
    const currentDate = new Date();
    this.maxBirthDate = new Date(currentDate.setFullYear(currentDate.getFullYear() - 18));
    this.minBirthDate = new Date(currentDate.setFullYear(currentDate.getFullYear() - 100));

    // Calculate age when birth date changes
    this.informationFormGroup.get('birthDate')?.valueChanges.subscribe((value) => {
      this.calculateAge(value);
    });

    // Add whitespace validators
    const fieldsToValidate = ['firstName', 'lastName', 'address'];
    fieldsToValidate.forEach(field => {
      const control = this.informationFormGroup.get(field);
      if (control) {
        control.addValidators(this.noOnlyWhitespaceValidator);
        control.updateValueAndValidity();
      }
    });

    // Load countries for potential future use
    this.mapToCountryISO(this.nationalityList);
  }

  public submit(): void {
    if (!this.informationFormGroup.invalid) {
      this.loaderService.show();
      this.fillUserData();

      this.loginService.verifyUserBeforeSignUp(this.user).subscribe({
        next: () => {
          this.loginService.signup(this.user).subscribe(
            (result) => {
              this.modalService.showModal({
                header: '',
                icon: 'Sticker.svg',
                body: this.translatePipe.transform('signup.SUCCESS-SIGNUP-TEXT'),
                footer: '',
                dialogClass: 'modal-dialog-centered',
                hideHeader: false,
                hideIcon: false,
                hideFooter: true,
                containerClick: false
              });
              this.loaderService.hide();
              this.toastrService.onSuccess(
                this.translatePipe.transform('users.INSCRIPTION_SUCCESS'),
                this.translatePipe.transform('menu.SUCCESS')
              );
              this.router.navigate(['/auth/login']);
            },
            (error) => {
              this.loaderService.hide();
              let errorDetails: string[] = error.error.detail.split(',');
              if (errorDetails.length > 0) {
                errorDetails.forEach((error) => {
                  this.toastrService.onError(
                    this.translatePipe.transform('users.errors.' + error.trim()),
                    this.translatePipe.transform('menu.ERROR')
                  );
                });
              }
              this.toastrService.onError(
                this.translatePipe.transform('users.errors.FAILED_TO_ADD_USER'),
                this.translatePipe.transform('menu.ERROR')
              );
            },
          );
        },
        error: (error) => {
          this.loaderService.hide();
          let errorDetails: string[] = error.error.detail.split(',');
          if (errorDetails.length > 0) {
            errorDetails.forEach((error) => {
              this.toastrService.onError(
                this.translatePipe.transform('users.errors.' + error.trim()),
                this.translatePipe.transform('menu.ERROR')
              );
            });
          } else {
            this.toastrService.onError(
              this.translatePipe.transform('users.errors.FAILED_TO_ADD_USER'),
              this.translatePipe.transform('menu.ERROR')
            );
          }
        }
      });
    }
  }

  public keypress_charNum(event: KeyboardEvent): boolean {
    let character: string;
    const regex = new RegExp('^[0-9]*$');
    character = event.key;
    if (!regex.test(character)) {
      event.preventDefault();
      return false;
    }
    return true;
  }

  public keypress_char(event: KeyboardEvent) {
    let character: string;
    const regex = new RegExp('^[a-zA-Z\u00C0-\u017F\\s-]*$');
    character = event.key;
    if (!regex.test(character)) {
      event.preventDefault();
      return false;
    }
    return true;
  }

  public onCancel(): void {
    this.router.navigate(['/auth/login']);
  }

  public calculateAge(birthDate: string) {
    if (birthDate) {
      const today = new Date();
      const birth = new Date(birthDate);
      let age = today.getFullYear() - birth.getFullYear();
      const monthDiff = today.getMonth() - birth.getMonth();

      if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
        age--;
      }

      this.informationFormGroup.get('age')?.setValue(age);
    }
  }

  public fillUserData(): void {
    const formValues = this.informationFormGroup.value;

    this.user.firstName = formValues.firstName;
    this.user.lastName = formValues.lastName;
    this.user.email = formValues.email;
    this.user.nationalId = formValues.cin;
    this.user.nationality = 'Tunisian';
    this.user.country = 'Tunisia';
    this.user.address = formValues.address;
    this.user.gender = formValues.gender;

    // Format phone number with Tunisian prefix
    let cleanedPhoneNumber = formValues.phoneNumber ? formValues.phoneNumber.replace(/^(ext-|\+|\s|-)+/i, '') : '';
    this.user.phoneNumber = '+216' + cleanedPhoneNumber;

    if (formValues.birthDate) {
      this.user.birthDate = formatDate(formValues.birthDate, 'yyyy-MM-dd', 'en-US');
    }
  }

  public mapToCountryISO(nationalities: any[]): void {
    const keys = nationalities.map((country) => {
      const normalizedLabel = country.label.replace(/\s+/g, '_').toLowerCase();
      return `users.nationalities.${normalizedLabel}`;
    });

    this.translateService.get(keys).subscribe((translations) => {
      this.countries = nationalities
      .map((country) => {
        const normalizedLabel = country.label.replace(/\s+/g, '_').toLowerCase();
        const translationKey = `users.nationalities.${normalizedLabel}`;
        const translatedLabel = translations[translationKey];

        if (translatedLabel && !translatedLabel.includes('_')) {
          return {
            code: country.code,
            label: translatedLabel,
            phonePrefix: country.phonePrefix,
            countryISO: this.countryISOValues.find((iso) => iso.toLowerCase() === country.code.toLowerCase()) || null
          };
        } else {
          return null;
        }
      })
      .filter((country) => country !== null);
    });
  }

  protected onLanguageChanged(newLanguage: string): void {
    this.language = newLanguage.charAt(0).toUpperCase() + newLanguage.slice(1).toLowerCase();
  }

  public noOnlyWhitespaceValidator(control: AbstractControl): ValidationErrors | null {
    if (typeof control.value !== 'string') return null;
    return control.value.trim().length === 0 && control.value.length > 0 ? { whitespace: true } : null;
  }
}