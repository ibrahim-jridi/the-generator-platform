import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { NavigationExtras, Router } from '@angular/router';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { Group } from '../../../../shared/models/group.model';
import { AppToastNotificationService } from 'src/app/shared/services/appToastNotification.service';
import { UserService } from '../../../../shared/services/user.service';
import { Constants } from '../../../../shared/utils/constants';
import { RegexConstants } from '../../../../shared/utils/regex-constants';
import { User } from '../../../../shared/models/user.model';
import { nationalitiesEnum } from '../../../../shared/enums/nationalities-enum';
import { NationalityList } from '../../../../shared/utils/Nationalities';
import { CountryISO } from 'ngx-intl-tel-input';
import { Nationalities } from '../../../../shared/enums/nationality.enum';
import { Role } from '../../../../shared/models/role.model';
import { RoleService } from '../../../../shared/services/role.service';
import { GroupsService } from '../../../../shared/services/groups.service';
import { GenderEnumsTranslationService } from '../../../../shared/enums/gender.enum';
import { StatusEnumsTranslationService } from '../../../../shared/enums/status.enum';
import {Observable, Subscription, throwError} from 'rxjs';
import {ActivityDomainEnum} from "../../../../shared/enums/activity-domain.enum";
import {CompanyUserModel} from "../../../../shared/models/company-user-model";
import {LegalStatusClass} from "../../../../shared/enums/legal-status.enum";
import {RegistryStatusClass} from "../../../../shared/enums/registryStatus.enum";
import {catchError, map} from "rxjs/operators";
import {ReportService} from "../../../../shared/services/report.service";
import {TokenUtilsService} from "../../../../shared/services/token-utils.service";

@Component({
  selector: 'app-internal-user-view',
  templateUrl: './internal-user-view.component.html',
  styleUrl: './internal-user-view.component.scss'
})
export class InternalUserViewComponent implements OnInit {
  protected user: any;
  protected companyUser: CompanyUserModel;
  public state = window.history.state;
  protected userId: string = this.state?.userId;
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
  public statusList: { id: string; label: any }[] = [];
  public genderList: { id: string; label: any }[] = [];
  protected readonly Constants = Constants;
  public currentStatus: string;
  public currentGender: string;

  private subscriptions: Subscription = new Subscription();
  public companyUserForm: any;
  public physique: boolean = false;
  public morale: boolean = false;
  public activityTypesForm: FormGroup;
  public currentLegalStatus: { id: string; label: any };
  public currentRegistryStatus: { id: string; label: any };
  protected registryStatusList: { id: string; label: any }[] = [];
  public legalStatusList: any[];
  public photoFile: File | null = null;
  public photoFilePatent: File | null = null;
  isLoadingFilePatent: boolean = false;
  protected connectedUserId: string;
  @ViewChild('fileInput') fileInput: ElementRef;
  @ViewChild('filePatentInput') filePatentInput: ElementRef;
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
    private statusEnumsTranslationService: StatusEnumsTranslationService,
    private legalStatusClass: LegalStatusClass,
    private registryStatusTranslatioService: RegistryStatusClass,
    private reportService: ReportService,
    private tokenUtilisService: TokenUtilsService
  ) {
    this.legalStatusList = this.legalStatusClass.getLegalStatus();
  }

  public ngOnInit(): void {
    this.connectedUserId = this.tokenUtilisService.getUserId();
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
    )
    this.translateservice.onLangChange.subscribe(() => {
      this.mapToCountryISO(this.nationalityList);
      this.patchFormData();
    });
    this.countries = this.mapToCountryISO(this.nationalityList);
    this.state = window.history.state;
    this.userId = this.state.userId;
    this.initUserForm();
    this.initCompanyUserForm()
    this.handleOnGetUser(this.userId);
    this.loadGroups();
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
  private initCompanyUserForm(): void {
    this.companyUserForm = this.fb.group({
      email: [{value: '', disabled: true}, [Validators.required, Validators.email, Validators.pattern(RegexConstants.EMAIL)]],
      phoneNumber: [{value: '', disabled: true}, [Validators.required]],
      address: [{value: '', disabled: true}, [Validators.required, Validators.pattern(RegexConstants.ADDRESS_LENGTH)]],
      taxRegistration:[{value: '', disabled: true}],
      socialReason:[{value: '', disabled: true}],
      legalStatus:[{value: '', disabled: true}],
      country:[{value: '', disabled: true}],
      denomination: [{value: '', disabled: true}],
      registryStatus: [{value: '', disabled: true}],
      fileStatus: [{value: '', disabled: true}],
      filePatent: [{value: '', disabled: true}],
      role: [{value: '', disabled: true}],
    });

  }
  public initActivityTypesForm(): void {
    this.activityTypesForm = this.fb.group({
      basicActivityTypeCode: [{ value: '', disabled: true }],
      basicActivityTypeName: [{ value: '', disabled: true }],
      secondaryActivityTypeCode: [{ value: '', disabled: true }],
      secondaryActivityTypeName: [{ value: '', disabled: true }],
      secondaryActivityTypes: this.fb.array(
          this.user?.activitiesType.map(() => this.fb.group({
            code: [{ value: '', disabled: true }],
            name: [{ value: '', disabled: true }],
          }))
      ),
    });

  }

  addSecondaryActivityType(): void {
    const secondaryActivityGroup = this.fb.group({
      code: [{value: '', disabled: true}, Validators.required],
      name: [{value: '', disabled: true}, Validators.required]
    });
    this.secondaryActivityTypes.push(secondaryActivityGroup);
  }
  get secondaryActivityTypes(): FormArray {
    return this.activityTypesForm.get('secondaryActivityTypes') as FormArray;
  }

  private patchFormData(): void {
    const groupLabels = this.user?.groups.map((group) => group.label);
    const roleLabels = this.user?.roles.map((role) => role.label);
    let nationalId = '';
    if (this.user.userType == "PHYSICAL") {
      nationalId = this.getUserNationality();
      const {
        status,
        translatedStatusLabel,
        gender,
        translatedGenderLabel
      } = this.getFieldsTraduction();
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
        status: translatedStatusLabel
      });
    } else {
      const groupLabels = this.user?.groups.map((group) => group.label);
      const roleLabels = this.user?.roles.map((role) => role.description);
      const registryStatus = this.getCurrentCompanyStatus?.(this.user) ?? null;
      const legalStatus = this.getCurrentLegalStatus?.(this.user) ?? null;
      this.getFile(this.user.fileStatus, 'bs-bucket-' + this.user.taxRegistration.replace(/\s/g, '')).subscribe((fileStatusFile) => {
        this.photoFile = fileStatusFile;
      });
      this.getFile(this.user.filePatent, 'bs-bucket-' + this.user.taxRegistration.replace(/\s/g, '')).subscribe((filePatentFile) => {
        this.photoFilePatent = filePatentFile;
      });
      this.currentLegalStatus = this.legalStatusList?.find(
          el => el.id === this.user?.legalStatus
      ) ?? null;
      this.companyUserForm.patchValue({
        taxRegistration: this.user?.taxRegistration ?? '',
        legalStatus: legalStatus?.label ?? '',
        socialReason: this.user?.socialReason ?? '',
        phoneNumber: this.user?.phoneNumber ?? '',
        registryStatus: registryStatus?.label ?? '',
        email: this.user?.email ?? '',
        address: this.user?.address ?? '',
        denomination: this.user?.denomination ?? '',
        fileStatus: this.user?.fileStatus ?? '',
        filePatent: this.user?.filePatent ?? '',
        activityTypes: this.user?.activitiesType ?? [],
        role: [roleLabels?.join(', ') ?? ''],
      });
      if (this.user.activitiesType
      ) {
        const activities = [...this.user.activitiesType];

        const basicActivityType = activities[0];
        this.activityTypesForm.patchValue({
          basicActivityTypeCode: basicActivityType?.code || '',
          basicActivityTypeName: basicActivityType?.name || '',
        });
        this.activityTypesForm.get('basicActivityTypeId')?.setValue(basicActivityType?.id || null);
        const secondaryActivityType = activities[1];
        this.activityTypesForm.patchValue({
          secondaryActivityTypeCode: secondaryActivityType?.code || '',
          secondaryActivityTypeName: secondaryActivityType?.name || '',
        });
        this.activityTypesForm.disable();
        this.secondaryActivityTypes.controls.forEach(control => control.disable());
      }
      this.companyUserForm.updateValueAndValidity();
    }
  }


  private getFieldsTraduction() {
    const country = this.getCountry() ?? null ;
    const translatedCountryLabel = country
      ? this.translateservice.instant(`users.nationalities.${country.label.replace(/\s+/g, '_').toLowerCase()}`) || country.label
      : '';

    this.phoneNumberPrefix = country?.phonePrefix;
    const countryLabel = country.label;
    this.selectedCountry = CountryISO[countryLabel];

    const status = this.currentStatus;
    const translatedStatusLabel = status ? this.translateservice.instant(`users.${status}`) || status : '';

    const gender = this.currentGender;
    const translatedGenderLabel = gender ? this.translateservice.instant(`users.${gender}`) || gender : '';
    this.userCountry = { ...this.userCountry, label: translatedCountryLabel };
    return { status, translatedStatusLabel, gender, translatedGenderLabel };
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

  protected getCurrentStatus(user: User) {
    if (user?.isActive === true) {
      const status = this.statusList.find((item) => item?.id === Constants.ACTIF_STATUS);
      this.currentStatus = status.id;
    } else {
      const status = this.statusList.find((item) => item?.id === Constants.NOT_ACTIF_STATUS);
      this.currentStatus = status.id;
    }
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

  protected getCurrentCompanyStatus(companyUser: CompanyUserModel): { id: string; label: string } | null {
    if (!companyUser?.registryStatus) return null;

    const registryId = companyUser.registryStatus?.toUpperCase?.();
    const found = this.registryStatusList?.find(item => item?.id === registryId) ?? null;

    this.currentRegistryStatus = found;
    return found;
  }

  protected getCurrentLegalStatus(companyUser: CompanyUserModel): { id: string; label: string } | null {
    if (!companyUser?.legalStatus) return null;

    const legalId = companyUser.legalStatus?.toUpperCase?.();
    const found = this.legalStatusList?.find(item => item?.id === legalId) ?? null;

    this.currentLegalStatus = found;
    return found;
  }

  public handleOnGetUser(id: string): void {
    this.userService.getUserById(id).subscribe({
      next: (data: any) => {
        this.user = data;
        this.getCurrentStatus(this.user);
        this.getUserGender(this.user);
        if(this.user?.userType === 'COMPANY'){
          this.initActivityTypesForm();
          this.morale = true
          this.physique = false;
        }else{
          this.physique = true;
          this.morale = false
        }
        this.patchFormData();
      },
      error: () => {
        this.toastrService.onError(this.translatePipe.transform('users.errors.FAILED_TO_LOAD_USER'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }

  loadGroups(): void {
    this.groupsService.getGroups().subscribe({
      next: (response: any) => {
        this.groups = response || [];
        this.handleOnGetUser(this.userId);
      },
      error: (error) => {
        this.toastrService.onError(this.translatePipe.transform('groups.FAILED_TO_LOAD_GROUP'), this.translatePipe.transform('menu.ERROR'));
      }
    });
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

  protected deleteUser(): void {
    this.userService.deleteUser(this.userId).subscribe({
      next: () => {
        this.toastrService.onSuccess(this.translatePipe.transform('users.DELETE_SUCCESS'), 'User');
        this.router.navigate(['/pages/user-management/internal-user-management']);
      },
      error: () => {
        this.toastrService.onError(this.translatePipe.transform('users.DELETE_FAILED'), 'User');
      }
    });
  }

  protected updateUser(): void {
    const navigationExtras: NavigationExtras = {
      state: {
        userId: this.userId // Database id
      }
    };
    const fullName = `${this.user?.firstName} ${this.user?.lastName}`;
    this.router.navigate(['/pages/user-management/internal-user-management/update-user', fullName], navigationExtras);
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
  private getFile(fileName: string, bucketName: string): Observable<File> {
    return this.reportService.getFile(fileName, bucketName).pipe(
        map((blob: Blob) => {
          this.isLoadingFilePatent = false;
          const file = new File([blob], fileName, {type: blob.type});
          return file;
        }),
        catchError((error) => {
          this.isLoadingFilePatent = false;
          return throwError(() => new Error('Error downloading file'));
        })
    );
  }

  public fetchAndDownloadFile(fileName: string): void {
    const bucketName = 'bs-bucket-' + this.user.taxRegistration.replace(/\s/g, '')
    this.reportService
    .getFileUrl(fileName, bucketName)
    .subscribe({
      next: (fileUrl: string) => {
        this.downloadFile(fileUrl, fileName);
      },
      error: (error) => {
        alert("Failed to fetch the file. Please try again.");
      },
    });
  }

  public downloadFile(fileUrl: string, fileName: string): void {
    const link = document.createElement("a");
    link.href = fileUrl;
    link.download = fileName;
    link.target = "_blank";
    link.click();
  }
}
