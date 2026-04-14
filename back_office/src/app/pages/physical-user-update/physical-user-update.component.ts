import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Constants} from "../../shared/utils/constants";
import {Group} from "../../shared/models/group.model";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";
import {NavigationExtras, Router} from "@angular/router";
import {AppToastNotificationService} from "../../shared/services/appToastNotification.service";
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ValidationErrors,
  Validators
} from "@angular/forms";
import {UserService} from "../../shared/services/user.service";
import {TokenUtilsService} from "../../shared/services/token-utils.service";
import {CountryISO} from "ngx-intl-tel-input";
import {Role} from "../../shared/models/role.model";
import {NationalityList} from "../../shared/utils/Nationalities";
import {RegexConstants} from "../../shared/utils/regex-constants";
import {Subscription} from "rxjs";
import {RoleService} from "../../shared/services/role.service";
import {GroupsService} from "../../shared/services/groups.service";
import {GenderEnumsTranslationService} from "../../shared/enums/gender.enum";
import {StatusEnumsTranslationService} from "../../shared/enums/status.enum";
import {ReportService} from "../../shared/services/report.service";
import {LegalStatusClass} from "../../shared/enums/legal-status.enum";
import {RegistryStatusClass} from "../../shared/enums/registryStatus.enum";
import {User} from "../../shared/models/user.model";
import {nationalitiesEnum} from "../../shared/enums/nationalities-enum";
import {Nationalities} from "../../shared/enums/nationality.enum";
import {CamundaService} from "../../shared/services/camunda.service";
import {ProcessInstance} from "../../shared/models/processInstance.model";
import {Country} from "ngx-intl-tel-input/lib/model/country.model";
import Validation from "../../shared/services/validation";
import {HistoricTaskInstance} from "../../shared/models/historicTaskInstance.model";

@Component({
  selector: 'app-physical-user-update',
  templateUrl: './physical-user-update.component.html',
  styleUrl: './physical-user-update.component.scss'
})
export class PhysicalUserUpdateComponent implements OnInit {

  protected userForm: any;
  public state = window.history.state;
  private userId: string = this.state?.userId;
  protected user: any;
  public companyUser: any;
  protected groups: Group[];
  protected roles: Role[];
  protected currentUserGroup: string;
  protected isExternalUser: boolean = false;
  protected disabled: boolean = true;
  protected username: string;
  public countries: any[];
  public nationalityList = NationalityList;
  protected readonly Nationalities = Nationalities;
  public selectedNationality: any;
  public selectedCountry: CountryISO = CountryISO.Tunisia;
  public phoneNumberPrefix: string = RegexConstants.PHONE_PREFIX;
  public countryFlagUrl: string = '';
  public countryISOValues = Object.values(CountryISO);
  public userCountry: { phonePrefix: string; flagClass: string; code: string; label: string };
  public statusList: { id: string; label: any }[] = [];
  public genderList: { id: string; label: any }[] = [];
  private subscriptions: Subscription = new Subscription();
  protected readonly Constants = Constants;
  public currentStatus: { id: string; label: any };
  public currentGender: { id: string; label: any };
  public minDate: string;
  public currentLanguage: string;
  public companyUserForm: any;
  public physique: boolean = true;
  public morale: boolean = false;
  public legalStatusList: any[];
  public activityTypesForm: FormGroup;
  protected registryStatusList: { id: string; label: any }[] = [];
  public isLoading: boolean = false;
  public fileStatus: File;
  public filePatent: File;
  @ViewChild('fileInput') fileInput: ElementRef;
  @ViewChild('filePatentInput') filePatentInput: ElementRef;
  public allowedExtensions: string[] = ['pdf'];
  private processDefKey: string = 'COMPLETE_PROFILE_PP';

  constructor(
      private translatePipe: TranslatePipe,
      private router: Router,
      private toastrService: AppToastNotificationService,
      private fb: FormBuilder,
      private roleService: RoleService,
      private tokenUtilsService: TokenUtilsService,
      private userService: UserService,
      private groupsService: GroupsService,
      private translateservice: TranslateService,
      private genderEnumsTranslationService: GenderEnumsTranslationService,
      private statusEnumsTranslationService: StatusEnumsTranslationService,
      private reportService: ReportService,
      private legalStatusClass: LegalStatusClass,
      private registryStatusTranslatioService: RegistryStatusClass,
      private camundaService: CamundaService

  ) {
    this.initUserForm();
    this.legalStatusList = this.legalStatusClass.getLegalStatus();
  }

  public ngOnInit(): void {
    this.currentLanguage = this.translateservice.currentLang;
    this.subscriptions.add(
        this.genderEnumsTranslationService.genderList$.subscribe((list) => {
          this.genderList = list;
        })
    );
    this.subscriptions.add(
        this.statusEnumsTranslationService.statusList$.subscribe((list) => {
          this.statusList = list;
        })
    );
    this.subscriptions.add(
        this.registryStatusTranslatioService.registryStatusList$.subscribe((list) => {
          this.registryStatusList = list;
        })
    );

    this.mapToCountryISO(this.nationalityList);
    this.translateservice.onLangChange.subscribe(() => {
      this.mapToCountryISO(this.nationalityList);
      this.patchFormData();
    });
    this.username = this.tokenUtilsService.getUsername();
    this.currentUserGroup = this.tokenUtilsService.getUserGroup();
    this.state = window.history.state;
    this.userId = this.state.userId;

    this.handleOnGetUser(this.userId);
    this.loadRoles();
  }

  private initUserForm(): void {
    this.userForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.pattern(RegexConstants.FIRST_NAME_LAST_NAME_REGEX)]],
      lastName: ['', [Validators.required, Validators.pattern(RegexConstants.FIRST_NAME_LAST_NAME_REGEX)]],
      userName: [{ value: this.username, disabled: true }, [Validators.required, Validators.required]],
      email: ['', [Validators.required, Validators.email, Validators.pattern(RegexConstants.EMAIL)]],
      birthDate: [{value: ''},[Validators.required, this.dateValidator()]],
      role: [{value: '', disabled: true}, [Validators.required]],
      country: [{value: this.userCountry, disabled: true}, [Validators.required]],
      passport: [{value: '', disabled: true}],
      cin: [{value: '', disabled: true}, [Validators.required, Validators.pattern(RegexConstants.CIN)]],
      address: ['', [Validators.required, Validators.pattern(RegexConstants.ADDRESS_LENGTH)]],
      gender: [{value: '', disabled: true}, [Validators.required]],
      phoneNumber: ['', [Validators.required]]
    });
  }



  protected getCurrentStatus(user: User): { id: string; label: string } {
    if (user?.isActive === true) {
      this.currentStatus = this.statusList.find((item) => item?.id === Constants.ACTIF_STATUS);
      return this.currentStatus;
    } else {
      this.currentStatus = this.statusList.find((item) => item?.id === Constants.NOT_ACTIF_STATUS);
      return this.currentStatus;
    }
  }

  public getUserGender(user: User): { id: string; label: any } {
    if (user?.gender === Constants.MALE_ENDER) {
      this.currentGender = this.genderList.find((item) => item?.id === Constants.MALE_ENDER);
      return this.currentGender;
    } else {
      this.currentGender = this.genderList.find((item) => item?.id === Constants.FEMALE_GENDER);
      return this.currentGender;
    }
  }

  public getCountry(): { phonePrefix: string; flagClass: string; code: string; label: string } {
    const matchedNationality = nationalitiesEnum.find((n) => n.nationality.toUpperCase() === this.user.nationality.toString());

    if (matchedNationality) {
      this.userCountry = this.nationalityList.find((item) => item.code === matchedNationality.code);
      return this.userCountry;
    } else return this.userCountry;
  }

  private patchFormData(): void {
    let nationalId = '';
    const roleLabels = this.user?.roles.map((role) => role.description);
    nationalId = this.getNationalId(nationalId);
    const { status, translatedStatusLabel, gender, translatedGenderLabel } = this.getAttributesTraduction();
      this.userForm.patchValue({
        firstName: this.user.firstName,
        lastName: this.user.lastName,
        email: this.user.email,
        username: this.user.username,
        password: this.user.password,
        confirmPassword: this.user.password,
        role: roleLabels.join(' , '),
        country: this.userCountry,
        nationalId: this.user.nationalId,
        phoneNumber: this.user.phoneNumber,
        address: this.user.address,
        cin: nationalId,
        passport: nationalId,
        birthDate: this.user.birthDate,
        gender: { ...gender, label: translatedGenderLabel },
        status: { ...status, label: translatedStatusLabel }
      });
      this.userForm.updateValueAndValidity();

  }
  private getAttributesTraduction() {
    const country = this.getCountry();
    const translatedCountryLabel = country
        ? this.translateservice.instant(`users.nationalities.${country.label.replace(/\s+/g, '_').toLowerCase()}`) || country.label
        : '';
    this.phoneNumberPrefix = country.phonePrefix;
    const countryLabel = country.label;
    this.selectedCountry = CountryISO[countryLabel];

    const status = this.getCurrentStatus(this.user);
    const translatedStatusLabel = status ? this.translateservice.instant(`users.${status.label.toLowerCase()}`) || status.label : '';

    const gender = this.getUserGender(this.user);
    const translatedGenderLabel = gender ? this.translateservice.instant(`users.${gender.label.toLowerCase()}`) || gender.label : '';
    this.userCountry = { ...this.userCountry, label: translatedCountryLabel };
    return { status, translatedStatusLabel, gender, translatedGenderLabel };
  }

  private getNationalId(nationalId: string): string {
    if (this.user.nationality === this.Nationalities.TUNISIAN.toString()) {
      nationalId = this.user.nationalId;
      this.userForm.get('cin')?.setValidators([Validators.required, Validators.pattern(RegexConstants.CIN)]);
      this.userForm.get('passport')?.clearValidators();
    } else {
      nationalId = this.user.nationalId;
      this.userForm.get('cin')?.clearValidators();
      this.userForm.get('passport')?.setValidators([Validators.required, Validators.pattern(RegexConstants.PASSPORT)]);
      this.userForm.get('phoneNumber')?.clearValidators();
    }
    return nationalId;
  }

  public handleOnGetUser(id): void {
    this.userService.getUserById(id).subscribe(
        (res) => {
          this.user = res;
          if (this.user.username.slice(0, 3) === 'ext') {
            this.isExternalUser = true;
            this.disabled = true;
          } else {
            this.isExternalUser = false;
          }
          if (this.user?.userType == 'COMPANY') {
            this.companyUser = res;
            this.morale = true;
            this.physique = false;
          } else {
            this.morale = false;
            this.physique = true;
          }
          this.patchFormData();
        },
        (error) => {
          this.toastrService.onError(this.translatePipe.transform('user.failedToLoadUser'), this.translatePipe.transform('menu.ERROR'));
        }
    );
  }



  loadRoles(): void {
    this.roleService.findAllRoles().subscribe((res) => {
      this.roles = res.map((role) => {
        return {
          ...role,
          disabled: role.label === 'DEFAULT_ROLE'
        };
      });
    });
  }

  protected updateUser(): void {
    this.fillUserData();
    this.userService.updateUser(this.userId, this.user).subscribe({
      next: () => {
        this.viewToasterSuccess();
        this.router.navigate(['/pages/physical-user-profile',this.userId]);
      },
      error: (error) => {
        let errorDetails: string[] = error.error.detail.split(',');
        if (errorDetails.length > 0) {
          errorDetails.forEach((error) => {
            this.toastrService.onError(this.translatePipe.transform('users.errors.' + error.trim()), this.translatePipe.transform('menu.ERROR'));
          });
        }
        this.toastrService.onError(this.translatePipe.transform('users.errors.FAILED_TO_ADD_USER'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }


  private fillUserData(): void {
    this.user.firstName = this.userForm.value.firstName;
    this.user.lastName = this.userForm.value.lastName;
    this.user.email = this.userForm.value.email;
    this.user.birthDate = this.userForm.value.birthDate;
    this.user.phoneNumber = this.userForm.value.phoneNumber.number  ;
    this.user.country = this.userForm.getRawValue().country?.code;
    if (this.userForm.getRawValue().country?.code !== Constants.TUNISIE.toUpperCase()) {
      this.user.nationalId = this.userForm.getRawValue().passport;
    } else {
      this.user.nationalId = this.userForm.getRawValue().cin;
    }
    this.user.nationality = this.user.nationality;
    this.user.address = this.userForm.value.address;
    this.user.gender = this.userForm.getRawValue().gender.id;

  }


  public get email() {
    return this.userForm?.get('email')!;
  }

  public get firstName() {
    return this.userForm?.get('firstName')!;
  }

  public get lastName() {
    return this.userForm?.get('lastName')!;
  }

  public get password() {
    return this.userForm?.get('password')!;
  }

  public get confirmPassword() {
    return this.userForm?.get('confirmPassword')!;
  }

  public get nationalId() {
    return this.userForm?.get('nationalId')!;
  }

  public get phoneNumber() {
    return this.userForm?.get('phoneNumber');
  }

  public get address() {
    return this.userForm?.get('address');
  }

  public get cin() {
    return this.userForm?.get('cin');
  }

  public get passport() {
    return this.userForm?.get('passport');
  }

  public viewToasterSuccess(): void {
    this.toastrService.onSuccess(this.translatePipe.transform('users.UPDATE_SUCCESS'), this.translatePipe.transform('menu.SUCCESS'));
  }

  protected back(): void {
    this.router.navigate(['/pages/user-management/internal-user-management']);
  }

  public handleNationalityChange(selectedNationality: any): void {
    if (selectedNationality?.label !== this.Nationalities.TUNISIAN) {
      this.userForm.get('cin')?.clearValidators();
      this.userForm.get('passport')?.setValidators([Validators.required, Validators.pattern(RegexConstants.PASSPORT)]);
    } else if (selectedNationality?.value === this.Nationalities.TUNISIAN) {
      this.userForm.get('passport')?.clearValidators();
      this.userForm.get('cin')?.setValidators([Validators.required, Validators.pattern(RegexConstants.CIN)]);
      this.userForm.get('phoneNumber')?.setValidators([Validators.required, Validators.minLength(8), Validators.maxLength(8)]);

    }
    if (selectedNationality) {
      const nationalityDetails = this.countries.find((n) => n.code === selectedNationality.code);

      if (nationalityDetails) {
        this.phoneNumberPrefix = nationalityDetails.phonePrefix;
        this.countryFlagUrl = nationalityDetails.flagUrl;
        this.selectedCountry = nationalityDetails.countryISO.replace(this.phoneNumberPrefix, '');
        this.userForm.get('phoneNumber')?.setValue(this.phoneNumberPrefix);
      }
    }
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

  public restorePrefix(): void {
    const currentValue = this.userForm.get('phoneNumber')?.value || '';
    if (!currentValue.startsWith(this.phoneNumberPrefix)) {
      this.userForm.get('phoneNumber')?.setValue(`${this.phoneNumberPrefix} ${currentValue.trim()}`);
    }
  }

  public mapToCountryISO(nationalities: any[]): void {
    const keys = nationalities.map((country) => {
      const normalizedLabel = country.label.replace(/\s+/g, '_').toLowerCase();
      return `users.nationalities.${normalizedLabel}`;
    });

    this.translateservice.get(keys).subscribe((translations) => {
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

  public dateValidator(): (control: AbstractControl) => ValidationErrors | null {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;
      const dateRegex = /^\d{4}-\d{2}-\d{2}$/;

      if (!value) {
        return null;
      }

      if (!dateRegex.test(value)) {
        return { invalidDateFormat: true };
      }

      return null;
    };
  }

  public onCinKeydown(event: KeyboardEvent): void {
    const allowedKeys = ['Backspace', 'Tab', 'ArrowLeft', 'ArrowRight', 'Delete', 'Enter'];
    const key = event.key;
    if (allowedKeys.includes(key)) {
      return;
    }
    if (!RegexConstants.ONLY_NUMERIC.test(key)) {
      event.preventDefault();
    }
  }

  public preventKeyInput(event: KeyboardEvent): void {
    event.preventDefault();
  }
  public getAge(dateOfBirth: Date): number {
    const today = new Date();
    let age = today.getFullYear() - dateOfBirth.getFullYear();
    const monthDiff = today.getMonth() - dateOfBirth.getMonth();

    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < dateOfBirth.getDate())) {
      age--;
    }

    return age;
  }

  protected updateUserProfile(): void {
    const navigationExtras: NavigationExtras = {
      state: {
        userId: this.userId,
      }
    };
    this.camundaService.getProcessInstancesByUserId(this.userId, this.processDefKey).subscribe({
      next: (historicTaskInstance:HistoricTaskInstance) => {

        if (historicTaskInstance==null ||historicTaskInstance.processInstanceId === '' || historicTaskInstance.processInstanceId == null) {
          this.userService.getUserById(this.userId).subscribe({
            next: (data) => {
              const birthdate = new Date(data.birthDate);
              let age = this.getAge(birthdate);

              const vars = {
                starter: data.id,
                Last_name: data.lastName,
                First_name: data.firstName,
                Gender: data.gender,
                Date_of_birth: data.birthDate,
                Phone_number: data.phoneNumber,
                Age: age,
                national_id_number: data.nationalId,
                email_address: data.email,
                e_barid: data.eBarid,
                Address: data.address
              };

              this.camundaService.startProcessOnceByKey(this.processDefKey, vars).subscribe(() => {
                this.toastrService.onInfo(this.translatePipe.transform('process.YOU_HAVE_TASK_TO_DO'), this.translatePipe.transform('menu.INFO')
                );
              });
            }
          });
          return;
        }

        this.camundaService.startProcessByVariables(historicTaskInstance.processInstanceId, this.processDefKey).subscribe({
          next: (result: any) => {
            this.camundaService.getTaskByProcessInstanceId(result.processInstanceId).subscribe({
              next: (data) => {
                const navigationExtras: NavigationExtras = {
                  state: {
                    processInstanceId: data.executionId,
                    taskId: data.id
                  }
                };
                this.router.navigate(['pages/task-management/task-list/validate-task', data.name], navigationExtras);

                // Show info after navigation
                this.toastrService.onInfo(
                    this.translatePipe.transform('process.YOU_HAVE_TASK_TO_DO'),
                    this.translatePipe.transform('menu.INFO')
                );
              },
              error: (error) => {
                this.toastrService.onError(
                    this.translatePipe.transform('task.FAILED_TO_GET_TASK'),
                    this.translatePipe.transform('menu.ERROR')
                );
              }
            });

          },
          error: (error) => {
            this.toastrService.onError(
                this.translatePipe.transform('task.INVALID_ACTIVE_TASK_UPDATE_PP'),
                this.translatePipe.transform('menu.ERROR')
            );
          }
        });

      },
      error: (error) => {
        this.toastrService.onError(
            this.translatePipe.transform('task.INVALID_ACTIVE_TASK_UPDATE_PP'),
            this.translatePipe.transform('menu.ERROR')
        );
      }
    });
  }

}
