import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {
  AbstractControl,
  FormArray,
  FormBuilder, FormGroup,
  ValidationErrors,
  Validators
} from '@angular/forms';
import { Router } from '@angular/router';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { Group } from '../../../../shared/models/group.model';
import { Role } from '../../../../shared/models/role.model';
import { AppToastNotificationService } from 'src/app/shared/services/appToastNotification.service';
import { GroupsService } from 'src/app/shared/services/groups.service';
import { RoleService } from 'src/app/shared/services/role.service';
import { TokenUtilsService } from 'src/app/shared/services/token-utils.service';
import { UserService } from '../../../../shared/services/user.service';
import { User } from '../../../../shared/models/user.model';
import { Nationalities } from '../../../../shared/enums/nationality.enum';
import { NationalityList } from '../../../../shared/utils/Nationalities';
import { CountryISO } from 'ngx-intl-tel-input';
import { Constants } from '../../../../shared/utils/constants';
import { RegexConstants } from '../../../../shared/utils/regex-constants';
import { nationalitiesEnum } from '../../../../shared/enums/nationalities-enum';
import {forkJoin, Observable, Subscription, throwError} from 'rxjs';
import { GenderEnumsTranslationService } from '../../../../shared/enums/gender.enum';
import { StatusEnumsTranslationService } from '../../../../shared/enums/status.enum';
import {ActivityDomainClass} from "../../../../shared/enums/activity-domain.enum";
import {LegalStatusClass} from "../../../../shared/enums/legal-status.enum";
import {CompanyUserModel} from "../../../../shared/models/company-user-model";
import {RegistryStatusClass} from "../../../../shared/enums/registryStatus.enum";
import {ActivityType} from "../../../../shared/models/activity-type";
import {ReportService} from "../../../../shared/services/report.service";
import {catchError, map} from "rxjs/operators";

@Component({
  selector: 'app-internal-user-update',
  templateUrl: './internal-user-update.component.html',
  styleUrl: './internal-user-update.component.scss'
})
export class InternalUserUpdateComponent implements OnInit {
  protected userForm: any;
  public state = window.history.state;
  private userId: string = this.state?.userId;
  protected user: any;
  public companyUser: any;
  protected groups: Group[];
  protected roles: Role[];
  protected currentUserGroup: string;
  protected isExternalUser: boolean = false;
  protected disabled: boolean = false;
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
  public currentLegalStatus: { id: string; label: any };
  public currentRegistryStatus: { id: string; label: any };
  public activityTypesForm: FormGroup;
  protected registryStatusList: { id: string; label: any }[] = [];

  public isLoading: boolean = false;
  public progressBarValue: number = 0;
  public fileStatus: File;
  public filePatent: File;
  public validFileFormat: boolean = true;
  public validFilePatentFormat: boolean = true;
  public selectedFileName: string | null = null;
  public photoFile: File | null = null;
  public photoFilePatent: File | null = null;
  @ViewChild('fileInput') fileInput: ElementRef;
  @ViewChild('filePatentInput') filePatentInput: ElementRef;
  public allowedExtensions: string[] = ['pdf'];
  public progressBarValuefilePatent: number = 0;
  public isLoadingFilePatent: boolean = false;
  public otherActivities: boolean = false;

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
    private registryStatusTranslatioService: RegistryStatusClass
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
    this.loadGroups();
    this.loadRoles();
    this.userForm.onValueChange((value) => {
      Object.keys(this.userForm.controls).forEach((con) => {
        const control = this.userForm.get(con);
        if (control) {
          control.clearValidators();
          control.updateValueAndValidity();
        }
      });
    });
  }

  private initUserForm(): void {
    this.userForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.pattern(RegexConstants.FIRST_NAME_LAST_NAME_REGEX)]],
      lastName: ['', [Validators.required, Validators.pattern(RegexConstants.FIRST_NAME_LAST_NAME_REGEX)]],
      userName: [{ value: this.username, disabled: true }, [Validators.required, Validators.required]],
      email: ['', [Validators.required, Validators.email, Validators.pattern(RegexConstants.EMAIL)]],
      birthDate: ['', [Validators.required, this.dateValidator()]],
      group: [[], [Validators.required]],
      role: [[], [Validators.required]],
      country: [this.userCountry],
      passport: ['', [Validators.required, Validators.pattern(RegexConstants.PASSPORT)]],
      cin: ['', [Validators.required, Validators.pattern(RegexConstants.CIN)]],
      phoneNumber: [''],
      address: ['', [Validators.required, Validators.pattern(RegexConstants.ADDRESS_LENGTH)]],
      nationalId: [''],
      status: ['', Validators.required],
      gender: ['', Validators.required]
    });
  }

  private initCompanyUserForm(): void {
    this.companyUserForm = this.fb.group({
      email: ['', [Validators.required, Validators.email, Validators.pattern(RegexConstants.EMAIL)]],
      phoneNumber: [this.phoneNumberPrefix, [Validators.required]],
      address: ['', [Validators.required, Validators.pattern(RegexConstants.ADDRESS_LENGTH)]],
      taxRegistration:[{value: '', disabled: true}],
      socialReason: [''],
      legalStatus: [''],
      country: [this.userCountry],
      denomination: [''],
      registryStatus: [''],
      fileStatus: [''],
      filePatent: [''],
      role: [{value: '', disabled: true}],
    });
    this.initActivityTypesForm();
  }

  private initActivityTypesForm(): void {
    this.activityTypesForm = this.fb.group({
      basicActivityTypeId: [''],
      basicActivityTypeCode: ['', Validators.required],
      basicActivityTypeName: ['', Validators.required],
      secondaryActivityTypes: this.fb.array([])
    });
  }

  get secondaryActivityTypes(): FormArray {
    return this.activityTypesForm.get('secondaryActivityTypes') as FormArray;
  }

  public addSecondaryActivityType(): void {
    this.otherActivities = true;
    const secondaryActivityGroup = this.fb.group({
      id: [null],
      code: ['', Validators.required],
      name: ['', Validators.required]
    });
    this.secondaryActivityTypes.push(secondaryActivityGroup);
  }

  public removeSecondaryActivityType(index: number): void {
    this.secondaryActivityTypes.removeAt(index);
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
    const groupIds = this.user.groups.map((group) => group.id);
    const roleIds = this.user.roles.map((role) => role.id);
    let nationalId = '';
    if (this.physique) {
      nationalId = this.getNationalId(nationalId);
      const { status, translatedStatusLabel, gender, translatedGenderLabel } = this.getAttributesTraduction();
      this.userForm.patchValue({
        firstName: this.user.firstName,
        lastName: this.user.lastName,
        email: this.user.email,
        username: this.user.username,
        password: this.user.password,
        confirmPassword: this.user.password,
        group: groupIds,
        role: roleIds,
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
    } else {
      const roleLabels = this.user?.roles.map((role) => role.description);
      const registryStatus = this.getCurrentCompanyStatus(this.companyUser);
      const legalStatus = this.getCurrentLegalStatus(this.companyUser);
      this.getFile(this.companyUser.fileStatus, 'bs-bucket-' + this.companyUser.taxRegistration.replace(/\s/g, '')).subscribe((fileStatusFile) => {
        this.photoFile = fileStatusFile;
      });

      this.getFile(this.companyUser.filePatent, 'bs-bucket-' + this.companyUser.taxRegistration.replace(/\s/g, '')).subscribe((filePatentFile) => {
        this.photoFilePatent = filePatentFile;
      });
      this.currentLegalStatus = this.legalStatusList?.find(
          el => el.id === this.user?.legalStatus
      ) ?? null;
      this.companyUserForm.patchValue({
        taxRegistration: this.companyUser.taxRegistration ?? '',
        legalStatus: legalStatus ?? '',
        socialReason: this.companyUser.socialReason ?? '',
        phoneNumber: this.companyUser.phoneNumber ?? '',
        country: this.userCountry ?? '',
        registryStatus: registryStatus ?? '',
        email: this.user.email ?? '',
        createdDate: this.companyUser.birthDate ?? '',
        address: this.companyUser.address ?? '',
        denomination: this.companyUser.denomination ?? '',
        fileStatus: this.companyUser.fileStatus ?? '',
        filePatent: this.companyUser.filePatent ?? '',
        activityTypes: this.companyUser.activitiesType ?? '',
        role: [roleLabels?.join(', ') ?? ''],
      });
      if (this.companyUser.activitiesType) {
        if (this.companyUser.activitiesType.length > 0) {
          const activities = [...this.companyUser.activitiesType];

          const basicActivityType = activities[0];
          this.activityTypesForm.patchValue({
            basicActivityTypeCode: basicActivityType?.code,
            basicActivityTypeName: basicActivityType?.name
          });
          this.activityTypesForm.get('basicActivityTypeId')?.setValue(basicActivityType?.id);

          while (this.secondaryActivityTypes.length > 0) {
            this.secondaryActivityTypes.removeAt(0);
          }

          for (let i = 1; i < activities.length; i++) {
            const secondaryActivity = activities[i];

            this.addSecondaryActivityType();

            this.secondaryActivityTypes.controls[i - 1].patchValue({
              code: secondaryActivity.code,
              name: secondaryActivity.name
            });
            this.secondaryActivityTypes.controls[i - 1].get('id')?.setValue(secondaryActivity.id);
          }
        }
      }
      this.companyUserForm.updateValueAndValidity();
    }
  }

  private getFile(fileName: string, bucketName: string): Observable<File> {
    return this.reportService.getFile(fileName, bucketName).pipe(
      map((blob: Blob) => {
        this.isLoadingFilePatent = false;
        const file = new File([blob], fileName, { type: blob.type });
        this.updateProgressBar(100);
        return file;
      }),
      catchError((error) => {
        this.isLoadingFilePatent = false;
        return throwError(() => new Error('Error downloading file'));
      })
    );
  }

  public fetchAndDownloadFile(fileName: string): void {
    const bucketName = 'bs-bucket-' + this.companyUser.taxRegistration.replace(/\s/g, '');
    this.reportService.getFileUrl(fileName, bucketName).subscribe({
      next: (fileUrl: string) => {
        this.downloadFile(fileUrl, fileName);
      },
      error: (error) => {
        alert('Failed to fetch the file. Please try again.');
      }
    });
  }

  public downloadFile(fileUrl: string, fileName: string): void {
    const link = document.createElement('a');
    link.href = fileUrl;
    link.download = fileName;
    link.target = '_blank';
    link.click();
  }

  public updateProgressBar(value: number): void {
    this.progressBarValuefilePatent = value;
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
    if (this.user.nationality === Nationalities.TUNISIAN.toString()) {
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
          this.initCompanyUserForm();
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

  protected updateUser(): void {
    this.fillUserData();
    this.userService.updateUser(this.userId, this.user).subscribe({
      next: () => {
        this.viewToasterSuccess();
        this.router.navigate(['/pages/user-management/internal-user-management']);
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

  protected updateCompanyUser(): void {
    this.fillCompanyUser();
    const taxRegistration = this.companyUserForm.get('taxRegistration')?.value
    .toLowerCase()
    .replace(/[^a-z0-9-]/g, '-')
    .replace(/-+/g, '-')
    .replace(/^-|-$/g, '')
    .slice(0, 50);
    if (!this.photoFilePatent || !this.photoFile) {
      this.toastrService.onError(this.translatePipe.transform('users.errors.FILES_ARE_REQUIRED'), this.translatePipe.transform('menu.ERROR'));
      return;
    }

    const uploadTasks = [this.uploadFile(this.photoFile, taxRegistration), this.uploadFile(this.photoFilePatent, taxRegistration)];

    forkJoin(uploadTasks).subscribe({
      next: ([statusResponse, patentResponse]) => {
        this.companyUser.fileStatus = this.photoFile.name;
        this.companyUser.filePatent = this.photoFilePatent.name;
        this.userService.updateCompanyUser(this.userId, this.companyUser).subscribe({
          next: () => {
            this.viewToasterSuccess();
            this.router.navigate(['/pages/user-management/internal-user-management']);
          },
          error: (addUserError) => {
            let errorDetails: string[] = addUserError.error.detail.split(',');
            if (errorDetails.length > 0) {
              errorDetails.forEach((error) => {
                this.toastrService.onError(this.translatePipe.transform('users.errors.' + error.trim()), this.translatePipe.transform('menu.ERROR'));
              });
            }
            this.toastrService.onError(this.translatePipe.transform('users.errors.FAILED_TO_ADD_USER'), this.translatePipe.transform('menu.ERROR'));
          }
        });
      },
      error: (uploadError) => {
        this.toastrService.onError(this.translatePipe.transform('file.FAILED_TO_UPLOAD_FILES'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }

  public fillCompanyUser() {
    this.companyUser.address = this.companyUserForm.value?.address;
    this.companyUser.filePatent = this.companyUserForm.value?.filePatent;
    this.companyUser.legalStatus = this.companyUserForm.value?.legalStatus.id;
    this.companyUser.socialReason = this.companyUserForm.value?.socialReason;
    const rawFormValue = this.companyUserForm.getRawValue();
    this.companyUser.taxRegistration = rawFormValue?.taxRegistration;
    this.companyUser.phoneNumber = this.companyUserForm.value.phoneNumber.number;
    this.companyUser.email = this.companyUserForm.value?.email;
    this.companyUser.denomination = this.companyUserForm.value?.denomination;
    this.companyUser.registryStatus = this.companyUserForm.value?.registryStatus.id;
    this.companyUser.fileStatus = this.companyUserForm.value?.fileStatus;
    this.getActivityTypes()
  }

  private getActivityTypes() {
    const {basicActivityTypeId, basicActivityTypeCode, basicActivityTypeName, secondaryActivityTypes } = this.activityTypesForm.value;

    const createOrUpdateActivityType = (initialActivity: Partial<ActivityType> = {}, code: string, name: string): ActivityType | null => {
      if (!code || !name) return null;
      return {
        ...initialActivity,
        code,
        name
      };
    };
    const basicActivityType = this.getBasicActivityValues(createOrUpdateActivityType, basicActivityTypeId, basicActivityTypeCode, basicActivityTypeName);
    const otherActivityList = this.getOtherActivityValues(secondaryActivityTypes, createOrUpdateActivityType);

    const allActivityCompany: ActivityType[] = [
      ...(basicActivityType ? [basicActivityType] : []),
      ...otherActivityList
    ];

    this.companyUser.activitiesType = allActivityCompany;
  }

  private getOtherActivityValues(secondaryActivityTypes, createOrUpdateActivityType: (initialActivity: Partial<ActivityType>, code: string, name: string) => (ActivityType | null)) {
    const otherActivityList: ActivityType[] = (secondaryActivityTypes || [])
      .filter((activity) => activity?.code && activity?.name)
      .map((activity) => {
        const originalActivity = this.companyUser.activitiesType?.find((act) => act.code === activity.code) || {};
        return createOrUpdateActivityType(originalActivity, activity.code, activity.name) as ActivityType;
      });
    return otherActivityList;
  }

  private getBasicActivityValues(createOrUpdateActivityType: (initialActivity: Partial<ActivityType>, code: string, name: string) => (ActivityType | null), basicActivityTypeId, basicActivityTypeCode, basicActivityTypeName) {
    const basicActivityType = createOrUpdateActivityType(
      this.companyUser.activitiesType?.find((act) => act.id === basicActivityTypeId) || {},
      basicActivityTypeCode,
      basicActivityTypeName
    );
    return basicActivityType;
  }

  private fillUserData(): void {
    this.user.firstName = this.userForm.value.firstName;
    this.user.lastName = this.userForm.value.lastName;
    this.user.email = this.userForm.value.email;
    this.user.groups = this.processSelectedGroups(this.userForm.value.group);
    this.user.roles = this.processSelectedRoles(this.userForm.value.role);
    let status: boolean = true;
    if (this.userForm.get('status').value?.id == Constants.ACTIF_STATUS) {
      status = true;
    } else {
      status = false;
    }
    this.user.birthDate = this.userForm.value.birthDate;
    this.user.phoneNumber = this.userForm.value.phoneNumber.number;
    this.user.country = this.userForm.value.country?.code;
    if (this.userForm.value.country?.code !== Constants.TUNISIE.toUpperCase()) {
      this.user.nationalId = this.userForm.value.passport;
    } else {
      this.user.nationalId = this.userForm.value.cin;
    }
    this.user.nationality = this.user.nationality;
    this.user.address = this.userForm.value.address;
    this.user.isActive = status;
    this.user.gender = this.userForm.value.gender.id;
  }

  private processSelectedGroups(selectedGroupIds: any): any[] {
    const groups: any[] = [];

    if (typeof selectedGroupIds === 'string') {
      const group = this.groups.find((group) => group.id === selectedGroupIds);
      if (group) {
        groups.push(group);
      }
    } else if (Array.isArray(selectedGroupIds) && selectedGroupIds.length > 0) {
      selectedGroupIds.forEach((el) => {
        let foundGroups;
        if (typeof el === 'string') {
          foundGroups = this.groups.filter((group) => group.id === el);
        } else if (el.id !== undefined) {
          foundGroups = this.groups.filter((group) => group.id === el.id);
        }
        if (foundGroups) {
          groups.push(...foundGroups);
        }
      });
    }
    return groups;
  }

  private processSelectedRoles(selectedRoleIds: any): any[] {
    const roles: any[] = [];

    if (typeof selectedRoleIds === 'string') {
      const role = this.roles.find((role) => role.id === selectedRoleIds);
      if (role) {
        roles.push(role);
      }
    } else if (Array.isArray(selectedRoleIds) && selectedRoleIds.length > 0) {
      selectedRoleIds.forEach((role) => {
        let foundRoles;
        if (typeof role === 'string') {
          foundRoles = this.roles.filter((r) => r.id === role);
        } else if (role.id !== undefined) {
          foundRoles = this.roles.filter((r) => r.id === role.id);
        }
        if (foundRoles) {
          roles.push(...foundRoles);
        }
      });
    }
    return roles;
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
    }
    if (selectedNationality) {
      const nationalityDetails = this.countries.find((n) => n.id === selectedNationality.id);

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

  public handleDragOver(event: DragEvent): void {
    event.preventDefault();
  }

  public handleDrop(event: DragEvent): void {
    event.preventDefault();
    const files = event.dataTransfer?.files;
    if (files) {
      this.fileStatus = files[0];
      this.photoFile = files[0];
      this.processSelectedFile();
    } else {
      this.toastrService.onError(this.translatePipe.transform('file.SINGLE_FILE_ONLY'), this.translatePipe.transform('menu.ERROR'));
    }
  }

  public clearSelectedFile(): void {
    this.photoFile = null;
    this.selectedFileName = null;
    this.companyUserForm.get('fileStatus').setValue(null);
    this.validFileFormat = true;
  }

  public openFileInput(): void {
    this.fileInput.nativeElement.click();
  }

  public onFileSelected(event: any): void {
    const files = event.target.files;

    if (files) {
      this.fileStatus = files[0];
      this.photoFile = files[0];
      this.processSelectedFile();
    } else {
      this.toastrService.onError(this.translatePipe.transform('file.SINGLE_FILE_ONLY'), this.translatePipe.transform('menu.ERROR'));
      event.target.value = null;
    }
  }

  private processSelectedFile(): void {
    if (!this.photoFile) {
      return;
    }

    this.selectedFileName = this.photoFile.name;
    const fileExtension = this.photoFile.name.split('.').pop().toLowerCase();

    if (!this.allowedExtensions.includes(fileExtension)) {
      this.validFilePatentFormat = false;
      this.toastrService.onError(this.translatePipe.transform('file.EXTENSION'), this.translatePipe.transform('menu.ERROR'));
      return;
    }

    this.isLoading = true;
    this.validFileFormat = true;
    this.progressBarValue = 0;

    this.getProgressBar('progressBarValue', 'isLoading');
  }

  public handleDragOverPatent(event: DragEvent): void {
    event.preventDefault();
  }

  public handleDropPatent(event: DragEvent): void {
    event.preventDefault();
    const files = event.dataTransfer?.files;
    if (files) {
      this.fileStatus = files[0];
      this.photoFile = files[0];
      this.processSelectedFile();
    } else {
      this.toastrService.onError(this.translatePipe.transform('file.SINGLE_FILE_ONLY'), this.translatePipe.transform('menu.ERROR'));
    }
  }

  public clearSelectedFilePatent(): void {
    this.photoFilePatent = null;
    this.selectedFileName = null;
    this.companyUserForm.get('filePatent').setValue(null);
    this.validFileFormat = true;
  }

  public openFileInputPatent(): void {
    this.filePatentInput.nativeElement.click();
  }

  public onFileSelectedPatent(event: any): void {
    const files = event.target.files;

    if (files) {
      this.filePatent = files[0];
      this.photoFilePatent = files[0];
      this.processSelectedFilePatent();
    } else {
      this.toastrService.onError(this.translatePipe.transform('file.SINGLE_FILE_ONLY'), this.translatePipe.transform('menu.ERROR'));
      event.target.value = null;
    }
  }

  private processSelectedFilePatent(): void {
    if (!this.photoFilePatent) {
      return;
    }

    this.selectedFileName = this.photoFilePatent.name;
    const fileExtension = this.photoFilePatent.name.split('.').pop().toLowerCase();

    if (!this.allowedExtensions.includes(fileExtension)) {
      this.validFilePatentFormat = false;
      this.toastrService.onError(this.translatePipe.transform('file.EXTENSION'), this.translatePipe.transform('menu.ERROR'));
      return;
    }

    this.isLoadingFilePatent = true;
    this.validFilePatentFormat = true;
    this.progressBarValuefilePatent = 0;

    this.getProgressBar('progressBarValuefilePatent', 'isLoadingFilePatent');
  }

  protected getProgressBar(
    progressBarValueKey: keyof this,
    isLoadingKey: keyof this,
    interval: number = 10,
    totalIterations: number = 100
  ): void {
    const incrementProgress = () => {
      (this[progressBarValueKey] as number)++;
      if ((this[progressBarValueKey] as number) < totalIterations) {
        setTimeout(incrementProgress, interval);
      } else {
        (this[isLoadingKey] as boolean) = false ;
      }
    };
    setTimeout(incrementProgress, interval);
  }
  protected uploadFile(selectedFile: File, taxRegistration: string): Observable<any> {
    return this.reportService.uploadFile(selectedFile, taxRegistration).pipe(
      map((response) => {
        return response;
      }),
      catchError((error) => {
        throw error;
      })
    );
  }
}
