import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, ValidatorFn, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {LoginService} from '../../shared/services/login.service';
import {AppToastNotificationService} from '../../shared/services/appToastNotification.service';
import {TranslatePipe, TranslateService} from '@ngx-translate/core';
import Validation from '../../shared/services/validation';
import {nationalitiesEnum} from '../../shared/enums/nationalities-enum';
import {User} from '../../shared/models/user.model';
import {NationalityList} from '../../shared/utils/Nationalities';
import {CountryISO} from 'ngx-intl-tel-input';
import {RegexConstants} from '../../shared/utils/regex-constants';
import {Subscription} from 'rxjs';
import {Constants} from '../../shared/utils/constants';
import {GenderEnum} from '../../shared/enums/gender.enum';
import decode from 'jwt-decode';
import {ModalService} from '../../shared/services/modal.service';
import {MatStepper} from '@angular/material/stepper';
import {LoaderService} from "../../shared/services/loader.service";
import {formatDate} from "@angular/common";
import {EhoweyaConfig} from "../../shared/models/ehoweyaConfig";
import {EhouweyaService} from "../../shared/services/ehouweya.service";
import {Nationalities} from "../../shared/enums/nationality.enum";
import {Country} from "ngx-intl-tel-input/lib/model/country.model";
import {UiModalComponent} from "../../theme/shared/components/modal/ui-modal/ui-modal.component";

@Component({
  selector: 'app-pre-signup',
  templateUrl: './pre-signup.component.html',
  styleUrl: './pre-signup.component.scss'
})
export class PreSignupComponent implements OnInit,AfterViewInit {
  protected user = new User();
  public countries: any[];
  public nationalityList = NationalityList;
  public selectedCountry: CountryISO = CountryISO.France;
  public phoneNumberPrefix: string = RegexConstants.PHONE_PREFIX_FRANCE;
  public countryFlagUrl: string = '';
  public countryISOValues = Object.values(CountryISO);
  nationalityFormGroup: FormGroup;
  subscriptionMethodFormGroup: FormGroup;
  informationFormGroup: FormGroup;
  private subscriptions: Subscription = new Subscription();
  fileName: string = '';
  genders = Object.values(GenderEnum);
  maxBirthDate: Date;
  minBirthDate: Date;
  emailRegex = RegexConstants.EMAIL;
  private token: string = '';
  private id: string = '';
  private email: string = '';
  public isTokenExpired: boolean = false;
  selectedNationality: string | null = null;
  public selectedRegistrationMethod: string | null = null;
  private timeoutId: any;
  public language: string;
  public currentLanguage: string;
  public ehoweyaConfig: EhoweyaConfig;
  private ehoweyaRecievedCode: string | null = null;
  protected readonly Nationalities = Nationalities;
  userCountry: Country;
  selectedNationalityy: Country | null = null;
  @ViewChild('stepper') stepper!: MatStepper;
  @ViewChild('cinVerificationModal') protected cinVerificationModal: UiModalComponent;
  subscriptionMethodFormGroups: FormGroup;
  public userData:string
  constructor(
    private _formBuilder: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
    private loginService: LoginService,
    private toastrService: AppToastNotificationService,
    private translatePipe: TranslatePipe,
    private translateService: TranslateService,
    private modalService: ModalService,
    private loaderService: LoaderService,
    private ehouweyaService: EhouweyaService
  ) {}

  ngOnInit(): void {
    this.currentLanguage = localStorage.getItem('language') || 'fr';
    this.language = this.currentLanguage.charAt(0).toUpperCase() + this.currentLanguage.slice(1).toLowerCase();
    this.mapToCountryISO(this.nationalityList);
    this.route.queryParams.subscribe((params) => {
      this.token = params['token'];
      this.id = params['id'];
      this.email = params['email'];

      let decodedToken: any = decode(this.token);
      if (!decodedToken.exp) {
        decodedToken = null;
      }
      if (decodedToken && decodedToken.exp && decodedToken.exp > Math.floor(Date.now() / 1000)) {
        this.isTokenExpired = false;
      } else {
        this.isTokenExpired = true;
      }
    });
    this.initNationalityForm();
    this.initSubscriptionMethodForm();
    this.initInformationForm();
    const currentDate = new Date();
    this.maxBirthDate = new Date(currentDate.setFullYear(currentDate.getFullYear() - 18));
    this.minBirthDate = new Date(currentDate.setFullYear(currentDate.getFullYear() - 100));
    this.setUpBirthDate();
    this.route.queryParams.subscribe(params => {
      const code = params['code'];
      if (code) {
        this.ehoweyaRecievedCode = code;
      }
      this.ehouweyaService.getEhoweyaConfig().subscribe(config => {
        this.ehoweyaConfig = config;
        sessionStorage.setItem('code_verifier', this.ehoweyaConfig.code_verifier.toString());
        if (this.ehoweyaRecievedCode && this.ehoweyaConfig) {
          this.selectedNationality = 'tunisienne';
          this.selectedNationality === 'tunisienne';
          this.onNationalityChange('tunisienne');
          this.onRegistrationMethodChange('ehouwiya');
          this.selectedRegistrationMethod === 'ehouwiya'
          this.nationalityFormGroup.get('tunisienne')?.setValue('tunisienne');
          this.subscriptionMethodFormGroup.get('ehouwiya')?.setValue('ehouwiya');
          this.subscriptionMethodFormGroup.get('checkbox')?.setValue(false);
          this.checkNationId(this.stepper);
          this.nationalityFormGroup.markAllAsTouched();
          this.nationalityFormGroup.updateValueAndValidity();
          this.subscriptionMethodFormGroup.markAllAsTouched();
          this.subscriptionMethodFormGroup.updateValueAndValidity();
          console.log("Stepper state", {
            step1Valid: this.nationalityFormGroup.valid,
            step2Valid: this.subscriptionMethodFormGroup.valid,
          });
          this.getUserInfo(this.ehoweyaRecievedCode);
          this.stepper.steps.toArray()[0].completed = true;
          this.stepper.steps.toArray()[1].completed = true;
          this.stepper.selectedIndex = 2;
        }
      }, error => {
        console.error('Erreur en récupérant la config ehoweya:', error);
      });
    });
  }
  ngAfterViewInit(): void {
    setTimeout( () => {
      if (this.ehoweyaRecievedCode && this.ehoweyaConfig) {
        this.selectedNationality = 'tunisienne';
        this.onNationalityChange('tunisienne');
        this.onRegistrationMethodChange('ehouwiya');
        this.selectedRegistrationMethod === 'ehouwiya'
        this.nationalityFormGroup.get('tunisienne')?.setValue('tunisienne');
        this.subscriptionMethodFormGroup.get('ehouwiya')?.setValue('ehouwiya');
        this.subscriptionMethodFormGroup.get('checkbox')?.setValue(false);
        this.checkNationId(this.stepper);
        this.nationalityFormGroup.markAllAsTouched();
        this.nationalityFormGroup.updateValueAndValidity();
        this.subscriptionMethodFormGroup.markAllAsTouched();
        this.subscriptionMethodFormGroup.updateValueAndValidity();
        console.log("Stepper state", {
          step1Valid: this.nationalityFormGroup.valid,
          step2Valid: this.subscriptionMethodFormGroup.valid,
        });
        this.getUserInfo(this.ehoweyaRecievedCode);
        this.stepper.steps.toArray()[0].completed = true;
        this.stepper.steps.toArray()[1].completed = true;
        this.stepper.selectedIndex = 2;
      }
    }, 200);
  }
  private setUpBirthDate() {
    this.informationFormGroup.get('birthDate').valueChanges.subscribe((value) => {
      this.calculateAge(value);
    });
  }

  private initInformationForm() {
    this.informationFormGroup = this._formBuilder.group({
      lastName: ['', Validators.required],
      firstName: ['', Validators.required],
      country: ['', Validators.required],
      phoneNumber: ['', Validators.required],
      email: [{ value: this.email, disabled: true }],
      passportNumber: [''],
      passportAttachment: [''],
      gender: ['', Validators.required],
      birthDate: ['', Validators.required],
      age: ['', Validators.required],
      address: ['', [Validators.required, Validators.pattern(RegexConstants.ADDRESS_LENGTH)]],
      cin: [''],
      eBarid: ['']
    });
  }

  private initSubscriptionMethodForm() {
    this.subscriptionMethodFormGroup = this._formBuilder.group({
      passportNumber: [''],
      nationalId: [''],
      ehouwiya: [''],
      evax: [''],
      cin: [''],
      checkbox: [false, Validators.required]
    });
    this.subscriptionMethodFormGroups = this._formBuilder.group({
      username: ['', [Validators.required, Validators.pattern(/^int-\s?\d{7}$/)]]
    });
  }

  private initNationalityForm() {
    this.nationalityFormGroup = this._formBuilder.group({
      tunisienne: [''],
      etrangere: ['']
    });
  }

  public resetForms(form: FormGroup): void {
    form.reset();
    form.markAsPristine();
    form.markAsUntouched();
    form.updateValueAndValidity();
  }

  public onNationalityChange(type: string): void {
    this.selectedNationality = type;
    this.resetForms(this.nationalityFormGroup);
    this.resetForms(this.subscriptionMethodFormGroup);

    this.resetFieldState('passportNumber', this.subscriptionMethodFormGroup);
    this.resetFieldState('passportNumber', this.informationFormGroup);

    if (type === 'etrangere') {
      this.setupEtrangereValidators();
      this.mapToCountryISO(this.nationalityList);
      this.informationFormGroup.get('country')?.valueChanges.subscribe((iso: CountryISO) => {
        const nationalityDetails = this.countries.find((c) => c.countryISO === iso);
        if (nationalityDetails) {
          this.selectedNationalityy = nationalityDetails;
          this.handleNationalityChange(nationalityDetails);
        }
      });
    } else if (type === 'tunisienne') {
      this.setupTunisienneValidators();
    }
    this.subscriptionMethodFormGroup.updateValueAndValidity();
    this.informationFormGroup.updateValueAndValidity();
  }

  private resetFieldState(fieldName: string, formGroup: FormGroup): void {
    const control = formGroup.get(fieldName);
    if (control) {
      control.reset('');
      control.markAsPristine();
      control.markAsUntouched();
      control.setErrors(null);
      control.updateValueAndValidity();
    }
  }

  private setupEtrangereValidators(): void {
    this.clearFormValidator(this.subscriptionMethodFormGroup);
    this.clearFormValidator(this.informationFormGroup);
    this.nationalityFormGroup.patchValue({ tunisienne: '', etrangere: 'etrangere' });
    this.resetFieldState('passportNumber', this.subscriptionMethodFormGroup);
    this.resetFieldState('passportNumber', this.informationFormGroup);

    this.setValidatorsForControl(this.subscriptionMethodFormGroup.get('passportNumber'), [
         Validators.required,
         Validators.maxLength(9), Validators.minLength(6),
         Validation.noSpecialCharacters(),
         Validation.alphanumericButNotOnlyLetters(),

    ]);
    this.setValidatorsForControl(this.informationFormGroup.get('passportNumber'), []);
    this.setValidatorsForControl(this.subscriptionMethodFormGroup.get('checkbox'), [Validators.requiredTrue]);
    this.subscriptionMethodFormGroup.get('checkbox')?.setValue(false);
    this.setInformationFormValidators();
    this.setValidatorsForControl(this.informationFormGroup.get('phoneNumber'), [Validators.required,Validators.maxLength(15), Validators.minLength(10)]);

    this.clearValidatorsForControls(['cin', 'ehouwiya', 'digitalId', 'pin'], this.subscriptionMethodFormGroup);
    this.subscriptionMethodFormGroup.updateValueAndValidity();
    this.informationFormGroup.updateValueAndValidity();
  }

  private setupTunisienneValidators(): void {
    this.nationalityFormGroup.patchValue({ tunisienne: 'tunisienne', etrangere: '' });

    this.resetFieldState('passportNumber', this.subscriptionMethodFormGroup);
    this.resetFieldState('passportNumber', this.informationFormGroup);

    this.clearValidatorsForControls(['passportNumber'], this.subscriptionMethodFormGroup);
    this.clearValidatorsForControls(['passportNumber'], this.informationFormGroup);

    this.setValidatorsForControl(this.informationFormGroup.get('phoneNumber'), [
      Validators.maxLength(8),
      Validators.minLength(8),
      Validation.phoneNumberValidator()
    ]);
    this.setValidatorsForControl(this.subscriptionMethodFormGroup.get('cin'), [Validators.required, Validators.pattern(RegexConstants.CIN)]);

    this.setInformationFormValidators();
    this.subscriptionMethodFormGroup.updateValueAndValidity();
    this.informationFormGroup.updateValueAndValidity();
  }

  private setInformationFormValidators(): void {
    this.setValidatorsForControl(this.informationFormGroup.get('lastName'), [Validators.required, Validators.minLength(2), Validators.maxLength(50)]);
    this.setValidatorsForControl(this.informationFormGroup.get('firstName'), [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(50)
    ]);
    this.setValidatorsForControl(this.informationFormGroup.get('address'), [Validators.pattern(RegexConstants.ADDRESS_LENGTH)]);
  }

  private setValidatorsForControl(control: AbstractControl | null, validators: ValidatorFn[]): void {
    if (control) {
      control.setValidators(validators);
      control.updateValueAndValidity();
    }
  }

  private clearValidatorsForControl(control: AbstractControl | null): void {
    if (control) {
      control.clearValidators();
      control.markAsPristine();
      control.markAsUntouched();
      control.reset();
      control.updateValueAndValidity();
    }
  }

  private clearValidatorsForControls(controlNames: string[], formGroup: FormGroup): void {
    controlNames.forEach((name) => {
      const control = formGroup.get(name);
      if (control) {
        this.clearValidatorsForControl(control);
      }
    });
  }
  private showToastError(errorCode: string): void {
    this.toastrService.onError(
        this.translatePipe.transform(`users.errors.${errorCode}`),
        this.translatePipe.transform('menu.ERROR')
    );
  }

  private setFieldErrorFromBackend(errorCode: string): void {
    const controlErrorMap: Record<string, { controlName: string; errorObj: any }> = {
      'INVALID_LENGTH_ADDRESS': { controlName: 'address', errorObj: { minlength: true } },
      'ADRESS_COULD_NOT_BE_NULL': { controlName: 'address', errorObj: { required: true } },
    };

    const mapping = controlErrorMap[errorCode];
    if (!mapping) return;

    const control = this.informationFormGroup.get(mapping.controlName);
    if (!control) return;

    control.setErrors(mapping.errorObj);
    control.markAsTouched();
  }
  public submit(): void {
    if (!this.informationFormGroup.invalid) {
      this.fillUserData(this.userData);
      this.loaderService.show();
      this.loginService.verifyUserBeforeSignUp(this.user).subscribe({
        next: () => {
          this.loginService.preSignup(this.user).subscribe(
            (result) => {
              this.loaderService.hide();
              this.modalService.showModal({
                header: '',
                icon: 'Sticker.svg',
                body: this.translatePipe.transform('signup.SUCCESS-SIGNUP-TEXT'),
                footer: '',
                dialogClass: 'modal-dialog-centered',
                hideHeader: false,
                hideIcon: false,
                hideFooter: false,
                containerClick: false
              });

              this.router.navigate(['/auth/login']);
            },
            (error) => {
              this.loaderService.hide();
              if (error.status === 409) {
                this.toastrService.onError(
                    this.translatePipe.transform('users.errors.ACTIVATION_LINK_USED'),
                    this.translatePipe.transform('menu.ERROR')
                );
                return;
              }
              let errorDetails: string[] = error.error.detail.split(',');
              if (errorDetails.length > 0) {
                errorDetails.forEach((error) => {
                  this.toastrService.onError(
                    this.translatePipe.transform('users.errors.' + error.trim()),
                    this.translatePipe.transform('menu.ERROR')
                  );
                });
              }
              this.toastrService.onError(this.translatePipe.transform('users.errors.FAILED_TO_ADD_USER'), this.translatePipe.transform('menu.ERROR'));
            }

          );
        },
        error: (error) => {
          this.loaderService.hide();

          const errorDetails: string[] = error?.error?.detail?.split(',') || [];

          if (errorDetails.length === 0) {
            this.showToastError('FAILED_TO_ADD_USER');
            return;
          }

          errorDetails.forEach(errorCodeRaw => {
            const errorCode = errorCodeRaw.trim();
            this.showToastError(errorCode);
            this.setFieldErrorFromBackend(errorCode);
          });
        }});
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
    const regex = new RegExp('^[a-zA-Z ]*$');
    character = event.key;
    if (!regex.test(character)) {
      event.preventDefault();
      return false;
    }
    return true;
  }

  public onRegistrationMethodChange(type: string): void {
    this.selectedRegistrationMethod = type;

    switch (type) {
      case 'ehouwiya':
        this.configureForEhouwiya();
        break;

      case 'nationalId':
        this.configureForNationalId();
        break;

      case 'evax':
        this.configureForEvax();
        break;

      default:
        break;
    }

    this.subscriptionMethodFormGroup.updateValueAndValidity();
    this.informationFormGroup.updateValueAndValidity();
  }

  private configureForEhouwiya(): void {
    this.subscriptionMethodFormGroup.get('ehouwiya')?.setValue('ehouwiya');
    this.subscriptionMethodFormGroup.get('digitalId')?.setValidators([Validators.required]);
    this.subscriptionMethodFormGroup.get('pin')?.setValidators([Validators.required]);
    this.subscriptionMethodFormGroup.get('checkbox')?.setValue(true);
    this.informationFormGroup.get('eBarid')?.setValidators([Validators.email]);

    this.subscriptionMethodFormGroup.get('digitalId')?.updateValueAndValidity();
    this.subscriptionMethodFormGroup.get('pin')?.updateValueAndValidity();
    this.informationFormGroup.get('eBarid')?.updateValueAndValidity();

    this.clearValidatorsForControls(['cin', 'passportNumber', 'nationalId'], this.subscriptionMethodFormGroup);

    this.subscriptionMethodFormGroup.updateValueAndValidity();
    this.informationFormGroup.updateValueAndValidity();
  }

  private configureForNationalId(): void {
    const cinControl = this.subscriptionMethodFormGroup.get('cin');
    this.subscriptionMethodFormGroup.get('nationalId')?.setValue('nationalId');
    this.subscriptionMethodFormGroup.get('checkbox')?.setValue(false);
    this.clearValidatorsForControls(['digitalId', 'pin', 'eBarid', 'passportNumber'], this.subscriptionMethodFormGroup);
    this.clearFormValidator(this.subscriptionMethodFormGroup);
    this.clearFormValidator(this.informationFormGroup);

    this.applyCinValidators(cinControl);
    this.subscriptionMethodFormGroup.get('cin').valueChanges.subscribe((value) => {
      this.informationFormGroup.get('cin').setValue(value);
    });
    this.subscriptionMethodFormGroup.updateValueAndValidity();
    this.informationFormGroup.updateValueAndValidity();
  }

  private configureForEvax(): void {}

  private applyCinValidators(cinControl: AbstractControl | null): void {
    cinControl?.setValidators([Validators.required, Validators.maxLength(8), Validators.minLength(8), Validation.cinStartWithValidator()]);
    cinControl?.markAsPristine();
    cinControl?.markAsUntouched();
    cinControl?.updateValueAndValidity({ emitEvent: false });
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

      this.informationFormGroup.get('age').setValue(age);
    }
  }
  public fillUserData(userData): void {
    const user = Array.isArray(userData) ? userData[0] : userData;
    if (this.informationFormGroup && user) {
      this.informationFormGroup.patchValue({
        firstName: user.firstName || '',
        lastName: user.lastName || '',
        email: user.email || '',
        phoneNumber: user.phoneNumber || '',
        address: user.address || '',
        gender: user.gender || '',
        birthDate: user.birthDate || '',

      });
    }    const formValues = this.informationFormGroup.get('firstName').disabled
        ? this.informationFormGroup.getRawValue()
        : this.informationFormGroup.value;

      this.user.firstName = formValues.firstName;
      this.user.lastName = formValues.lastName;
      this.user.email = this.email;
      this.user.id = this.id;
      const matchedNationality = nationalitiesEnum.find(
        (n) => n.code === formValues.country?.code
      );
      console.log('matchedNationality :', matchedNationality);

      const tunisianNationality = nationalitiesEnum.find(
        (n) => n.code === Constants.TUNISIE.toUpperCase()
      );

      if (this.nationalityFormGroup.value.tunisienne) {
      this.user.nationalId = formValues.cin;
      this.user.nationality = tunisianNationality?.nationality;
      const tnCountry = this.countries.find((el) => el.code.toLowerCase() === 'tn');
      this.user.country = tnCountry?.label;
      this.user.phoneNumber = tnCountry?.phonePrefix + formValues.phoneNumber;
    } else {
      this.user.nationality = matchedNationality
        ? matchedNationality.nationality
        : tunisianNationality?.nationality;
      this.user.nationalId = formValues.passportNumber;
      this.user.country = formValues.country?.label;
      const countryData = this.countries.find((el) => el.code === formValues.country?.code);
      this.user.phoneNumber = countryData?.phonePrefix + formValues.phoneNumber;
    }

    if (formValues.birthDate) {
      this.user.birthDate = formatDate(formValues.birthDate, 'yyyy-MM-dd', 'en-US');
    }

    this.user.address = formValues.address;
    this.user.gender = formValues.gender;
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

  public checkNationId(stepper: MatStepper) {
    if (this.selectedRegistrationMethod === 'ehouwiya' && this.ehoweyaRecievedCode) {
      return ;
    }else if (this.selectedRegistrationMethod == 'ehouwiya') {
      this.redirectToAuth();
    } else {
      let nationalId = '';
      if (this.nationalityFormGroup.get('tunisienne').value !== '') {
        nationalId = this.subscriptionMethodFormGroup.get('cin').value;
      } else {
        nationalId = this.subscriptionMethodFormGroup.get('passportNumber').value;
        this.informationFormGroup.get('passportNumber')?.patchValue(nationalId);
      }
      this.loginService.verifyNationalId(nationalId).subscribe({
        next: () => {
          stepper.next();
        },

        error: (error) => {
          if (error.error?.detail === 'NATIONAL_ID_EXIST') {
            this.loginService.checkInternalUsername(nationalId).subscribe({
              next: (isInternalUser) => {
                if (isInternalUser) {
                  this.cinVerificationModal.show();
                } else {
                  this.toastrService.onError(
                      this.translatePipe.transform('users.errors.NATIONAL_ID_EXIST'),
                      this.translatePipe.transform('menu.ERROR')
                  );
                }
              },
              error: () => {
                this.toastrService.onError(
                    this.translatePipe.transform('users.errors.NATIONAL_ID_EXIST'),
                    this.translatePipe.transform('menu.ERROR')
                );
              }
            });
          }
        }
      });
    }
  }
  public async redirectToAuth(): Promise<void> {
    const encodedRedirectUri = encodeURIComponent(this.ehoweyaConfig.redirect_uri.toString());
    const encodedScope = encodeURIComponent(this.ehoweyaConfig.scope.toString());

    const authUrl = `https://193.95.63.241:8443/oauth2/authorize` +
      `?response_type=${this.ehoweyaConfig.response_type}` +
      `&client_id=${this.ehoweyaConfig.client_id}` +
      `&scope=${encodedScope}` +
      `&redirect_uri=${encodedRedirectUri}` +
      `&code_challenge=${this.ehoweyaConfig.code_challenge}` +
      `&code_challenge_method=${this.ehoweyaConfig.code_challenge_method}`;

    window.location.href = authUrl;
  }
  public clearFormValidator(form: FormGroup) {
    Object.keys(form.controls).forEach((controlName) => {
      const control = form.get(controlName);
      control?.clearValidators();
      control?.updateValueAndValidity();
      control?.valid;
    });
    console.log(form.valid);
  }
  public async getUserInfo(code: string): Promise<void> {
    try {
      let codeVerifier = sessionStorage.getItem('code_verifier');
      let userInfo ;
      this.ehouweyaService.getUserInfo(code,codeVerifier).subscribe(res =>{
        userInfo = res ;
        sessionStorage.removeItem('code_verifier');
        const ehouweyaUser = this.fillUserDataFromEhoweya(userInfo);

        this.informationFormGroup.patchValue(ehouweyaUser);
        this.informationFormGroup.get('cin').patchValue(ehouweyaUser.cin.toString());
        this.informationFormGroup.disable();
        this.informationFormGroup.get('address').enable();
      });
    } catch (err) {
      this.toastrService.onError(
        this.translatePipe.transform('users.errors.ERROR_LOAD_USER_FROM_EHOWEYA'),
        this.translatePipe.transform('menu.ERROR')
      );
      console.error('Failed to load user info:', err);
    }
  }

  private fillUserDataFromEhoweya(userInfo) {
    const ehouweyaUser = new User();
    ehouweyaUser.cin = userInfo?.identityNumber.toString() ;
    ehouweyaUser.firstName = userInfo?.given_name_fr ;
    ehouweyaUser.lastName = userInfo?.family_name_fr;
    ehouweyaUser.phoneNumber = userInfo?.phoneNumber ;
    ehouweyaUser.address = userInfo?.address ;
    ehouweyaUser.eBarid = userInfo?.preferred_username ;
    if(userInfo?.birthDate != null){
      const formattedBirthDate = formatDate(userInfo?.birthDate, 'yyyy-MM-dd', 'en-US');
      this.calculateAge(formattedBirthDate);
      ehouweyaUser.birthDate = formattedBirthDate ;
    }

    ehouweyaUser.email = this.email ;
    if(userInfo?.gender == "1"){
      ehouweyaUser.gender = GenderEnum.MALE;
    }else{
      ehouweyaUser.gender = GenderEnum.FEMALE;
    }

    this.informationFormGroup.patchValue(ehouweyaUser);
    this.informationFormGroup.get('cin').patchValue(ehouweyaUser.cin.toString());
    this.informationFormGroup.disable();
    return ehouweyaUser;
  }
  public handleNationalityChange(selectedNationality: any): void {
    const phoneControl = this.informationFormGroup.get('phoneNumber');

    if (selectedNationality?.label !== this.Nationalities.TUNISIAN) {
      this.informationFormGroup.get('cin')?.clearValidators();
      this.informationFormGroup.get('passport')?.setValidators([Validators.required, Validators.pattern(RegexConstants.PASSPORT)]);
      this.informationFormGroup.get('phoneNumber')?.setValidators([Validators.required, Validators.minLength(13), Validators.maxLength(18)]);

      if (selectedNationality) {
        const nationalityDetails = this.countries.find((n) => n.code === selectedNationality.code);
        if (nationalityDetails) {
          this.phoneNumberPrefix = nationalityDetails.phonePrefix;
          this.countryFlagUrl = nationalityDetails.flagUrl;
          this.selectedCountry = nationalityDetails.countryISO.replace(this.phoneNumberPrefix, '');
          phoneControl?.setValue(this.phoneNumberPrefix);
        }
      }
    } else if (selectedNationality?.value === this.Nationalities.TUNISIAN) {
      this.informationFormGroup.get('passport')?.clearValidators();
      this.informationFormGroup.get('cin')?.setValidators([Validators.required, Validators.pattern(RegexConstants.CIN)]);

      this.phoneNumberPrefix = '';
      this.countryFlagUrl = '';

      this.informationFormGroup.get('phoneNumber')?.setValidators([Validators.required, Validators.minLength(8), Validators.maxLength(8)]);

      phoneControl?.setValue('');
      phoneControl?.markAsUntouched();
      phoneControl?.markAsPristine();
    }

    phoneControl?.updateValueAndValidity();
    this.informationFormGroup.get('cin')?.updateValueAndValidity();
    this.informationFormGroup.get('passport')?.updateValueAndValidity();
  }
  preventPrefixRemoval(event: KeyboardEvent): void {
    const input = event.target as HTMLInputElement;
    if (event.key === 'Backspace' || event.key === 'Delete') {
      if (input.selectionStart !== null && input.selectionStart <= this.phoneNumberPrefix.length) {
        event.preventDefault();
        input.value = this.phoneNumberPrefix;
      }
    }
  }

  restorePrefix(): void {
    const phoneControl = this.informationFormGroup.get('phoneNumber');
    const value = phoneControl?.value?.trim() || '';
    if (!value || value.length < this.phoneNumberPrefix.length || !value.startsWith(this.phoneNumberPrefix)) {
      phoneControl?.setValue(this.phoneNumberPrefix);
    }
  }
  public  async cinVerification(stepper:MatStepper){
    let nationalId =this.subscriptionMethodFormGroup.get('cin').value;
    let username = this.subscriptionMethodFormGroups.get('username').value;

    this.loginService.checkUsernameMatch(nationalId,username).subscribe({
          next: (IsMatchUsername) => {
            if (IsMatchUsername) {
              this.toastrService.onSuccess(this.translatePipe.transform('users.success.VERIFICATION_SUCCESS_USERNAME'), this.translatePipe.transform('menu.SUCCESS'));
              this.cinVerificationModal.hide()
              this.loginService.getAllByNationalId(nationalId).subscribe({
                next: (response) => {
                  this.fillUserData(response);
                  stepper.next();
                },
              });
            }
            else {
              this.toastrService.onError(
                  this.translatePipe.transform('users.errors.VERIFICATION_FAILED_USERNAME'),
                  this.translatePipe.transform('menu.ERROR')
              );
            }
          },
        }
    );

  }
}
