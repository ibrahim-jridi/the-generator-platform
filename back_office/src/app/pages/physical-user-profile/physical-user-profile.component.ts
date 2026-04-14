import { Component, ViewChild } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NavigationExtras, Router } from '@angular/router';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { CountryISO } from 'ngx-intl-tel-input';
import { Subscription } from 'rxjs';
import { GenderEnumsTranslationService } from 'src/app/shared/enums/gender.enum';
import { LegalStatusClass } from 'src/app/shared/enums/legal-status.enum';
import { nationalitiesEnum } from 'src/app/shared/enums/nationalities-enum';
import { Nationalities } from 'src/app/shared/enums/nationality.enum';
import { Form } from 'src/app/shared/models/form.model';
import { Group } from 'src/app/shared/models/group.model';
import { HistoricTaskInstance } from 'src/app/shared/models/historicTaskInstance.model';
import { PaginationArgs, PaginationSortArgs, PaginationSortOrderType } from 'src/app/shared/models/paginationArgs.model';
import { Role } from 'src/app/shared/models/role.model';
import { User } from 'src/app/shared/models/user.model';
import { AppToastNotificationService } from 'src/app/shared/services/appToastNotification.service';
import { GroupsService } from 'src/app/shared/services/groups.service';
import { RoleService } from 'src/app/shared/services/role.service';
import { TokenUtilsService } from 'src/app/shared/services/token-utils.service';
import { UserService } from 'src/app/shared/services/user.service';
import { Constants } from 'src/app/shared/utils/constants';
import { NationalityList } from 'src/app/shared/utils/Nationalities';
import { RegexConstants } from 'src/app/shared/utils/regex-constants';

@Component({
  selector: 'app-physical-user-profile',
  templateUrl: './physical-user-profile.component.html',
  styleUrl: './physical-user-profile.component.scss'
})
export class PhysicalUserProfileComponent {

  protected user: any;
  protected userForm: any;
  protected groups: Group[];
  protected roles: Role[];
  public countries: any[];
  disabled: boolean = true;
  public nationalityList = NationalityList;
  public selectedCountry: CountryISO = CountryISO.Tunisia;
  public phoneNumberPrefix: string = RegexConstants.PHONE_PREFIX;
  public countryISOValues = Object.values(CountryISO);
  public userCountry: { phonePrefix: string; flagClass: string; code: string; label: string };
  public genderList: { id: string; label: any }[] = [];
  protected readonly Constants = Constants;
  public currentGender: string;

  private subscriptions: Subscription = new Subscription();
  public physique: boolean = false;
  isLoadingFilePatent: boolean = false;
  protected connectedUserId: string;
  public hasActiveTasks: boolean = false;
  private readonly paginationSortArgs: PaginationSortArgs = new PaginationSortArgs('last_modified_date', PaginationSortOrderType.DESC);
  private paginationArgs: PaginationArgs;
  public totalCount: number;
  protected userId: string;
  protected tasks: any[] = [];
  protected taskId: string;
  protected formhistoric: any;

  private historicTasks: HistoricTaskInstance[] = [];
  constructor(
    private translatePipe: TranslatePipe,
    private router: Router,
    private toastrService: AppToastNotificationService,
    private fb: FormBuilder,
    private userService: UserService,
    private roleService: RoleService,
    private groupsService: GroupsService,
    private translateservice: TranslateService,
    private genderEnumsTranslationService: GenderEnumsTranslationService,
    private tokenUtilisService: TokenUtilsService,

  ) {
  }

  public ngOnInit(): void {
   this.userId = this.tokenUtilisService.getUserId();
    this.subscriptions.add(
      this.genderEnumsTranslationService.genderList$.subscribe((list) => {
        this.genderList = list;
      })
    );

    this.translateservice.onLangChange.subscribe(() => {
      this.mapToCountryISO(this.nationalityList);
      this.patchFormData();
    });
    this.countries = this.mapToCountryISO(this.nationalityList);
    this.initUserForm();
    this.handleOnGetUser(this.userId);
    this.loadRoles();

  }

  private initUserForm(): void {
    this.userForm = this.fb.group({
      firstName: [
        {
          value: '',
          disabled: true
        },
        [Validators.required, Validators.pattern(RegexConstants.FIRST_NAME_LAST_NAME_REGEX)]
      ],
      lastName: [
        {
          value: '',
          disabled: true
        },
        ,
        [Validators.required, Validators.pattern(RegexConstants.FIRST_NAME_LAST_NAME_REGEX)]
      ],
      username: [
        {
          value: this.user?.username,
          disabled: true
        },
        [Validators.required, Validators.required]
      ],
      email: [
        {
          value: '',
          disabled: true
        },
        ,
        [Validators.required, Validators.email, Validators.pattern(RegexConstants.EMAIL)]
      ],
      birthDate: [
        {
          value: '',
          disabled: true
        },
        ,
        [Validators.required]
      ],
      group: [
        {
          value: '',
          disabled: true
        },
        [Validators.required]
      ],
      role: [
        {
          value: '',
          disabled: true
        },
        [Validators.required]
      ],
      country: [
        {
          value: '',
          disabled: true
        }
      ],
      passport: [
        {
          value: '',
          disabled: true
        },
        [Validators.required, Validators.pattern(RegexConstants.PASSPORT)]
      ],
      cin: [
        {
          value: '',
          disabled: true
        },
        ,
        [Validators.required, Validators.pattern(RegexConstants.CIN)]
      ],
      phoneNumber: [
        {
          value: this.phoneNumberPrefix,
          disabled: true
        }
      ],
      address: [
        {
          value: '',
          disabled: true
        },
        ,
        [Validators.required, Validators.pattern(RegexConstants.ADDRESS_LENGTH)]
      ],
      nationalId: [
        {
          value: '',
          disabled: true
        }
      ],
      status: [
        {
          value: '',
          disabled: true
        },
        ,
        Validators.required
      ],
      gender: [
        {
          value: '',
          disabled: true
        },
        ,
        Validators.required
      ]
    });
  }

  private patchFormData(): void {
    const groupLabels = this.user?.groups.map((group) => group.description);
    const roleLabels = this.user?.roles.map((role) => role.description);
    let nationalId = '';
    nationalId = this.getUserNationality();
    const {
      gender,
      translatedGenderLabel
    } = this.getFieldsTraduction();
    if (this.user.userType == "PHYSICAL") {
      this.userForm.patchValue({
        firstName: this.user.firstName,
        lastName: this.user.lastName,
        email: this.user.email,
        username: this.user.username,
        password: this.user.password,
        confirmPassword: this.user.password,
        group: groupLabels.join(' , '),
        role: roleLabels.join(' , '),
        country: this.userCountry.label,
        nationalId: this.user.nationalId,
        phoneNumber: this.user.phoneNumber,
        address: this.user.address,
        cin: nationalId,
        passport: nationalId,
        birthDate: this.user.birthDate,
        gender: translatedGenderLabel,
      });
    }

  }


  private getFieldsTraduction() {
    const country = this.getCountry();
    const translatedCountryLabel = country
      ? this.translateservice.instant(`users.nationalities.${country.label.replace(/\s+/g, '_').toLowerCase()}`) || country.label
      : '';

    this.phoneNumberPrefix = country?.phonePrefix;
    const countryLabel = country.label;
    this.selectedCountry = CountryISO[countryLabel];
    const gender = this.currentGender;
    const translatedGenderLabel = gender ? this.translateservice.instant(`users.${gender}`) || gender : '';
    this.userCountry = { ...this.userCountry, label: translatedCountryLabel };
    return { gender, translatedGenderLabel };
  }

  private getUserNationality() {
    let nationalId = '';
    if (this.user?.nationality === Nationalities.TUNISIAN.toString()) {
      nationalId = this.user.nationalId;
      this.userForm.get('cin')?.setValidators([Validators.required, Validators.pattern(RegexConstants.CIN)]);
      this.userForm.get('passport')?.clearValidators();
    } else {
      nationalId = this.user?.nationalId;
      this.userForm.get('cin')?.clearValidators();
      this.userForm.get('passport')?.setValidators([Validators.required, Validators.pattern(RegexConstants.PASSPORT)]);
      this.userForm.get('phoneNumber')?.clearValidators();
    }
    return nationalId;
  }

  public getUserGender(user: User) {
    if (user?.gender === Constants.MALE_ENDER) {
      const gender = this.genderList.find((item) => item?.id === Constants.MALE_ENDER);
      this.currentGender = gender.id;
    } else {
      const gender = this.genderList.find((item) => item?.id === Constants.FEMALE_GENDER);
      this.currentGender = gender.id;
    }
  }

  public getCountry(): { phonePrefix: string; flagClass: string; code: string; label: string } {
    const matchedNationality = nationalitiesEnum.find((n) => n.nationality.toUpperCase() === this.user?.nationality.toString());

    if (matchedNationality) {
      this.userCountry = this.nationalityList.find((item) => item.code === matchedNationality.code);
      return this.userCountry;
    } else return this.userCountry;
  }

  public handleOnGetUser(id: string): void {
    this.userService.getUserById(id).subscribe({
      next: (data: any) => {
        this.user = data;
        this.getUserGender(this.user);
        this.patchFormData();
      },
      error: () => {
        this.toastrService.onError(this.translatePipe.transform('users.errors.FAILED_TO_LOAD_USER'), this.translatePipe.transform('menu.ERROR'));
      }
    });
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

  public mapToCountryISO(nationalities: any[]): {
    code: string;
    label: string;
    phonePrefix: string;
    countryISO: CountryISO | null;
  }[] {
    return nationalities.map((country) => {
      const matchingISO = this.countryISOValues.find((iso) => iso.toLowerCase() === country?.code.toLowerCase());
      return {
        ...country,
        countryISO: matchingISO || null
      };
    });
  }
  protected updateUser(): void {
    const navigationExtras: NavigationExtras = {
      state: {
        userId: this.userId
      }
    };
    this.router.navigate(['/pages/physical-user-update',this.userId], navigationExtras);
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

}
