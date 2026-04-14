import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  ElementRef,
  OnInit,
  ViewChild
} from '@angular/core';
import {
  AbstractControl, FormArray,
  FormBuilder,
  FormGroup,
  ValidationErrors,
  Validators
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { Group } from '../../../../shared/models/group.model';
import { Role } from '../../../../shared/models/role.model';
import { User } from 'src/app/shared/models/user.model';
import { AppToastNotificationService } from 'src/app/shared/services/appToastNotification.service';
import { GroupsService } from 'src/app/shared/services/groups.service';
import { RoleService } from 'src/app/shared/services/role.service';
import { TokenUtilsService } from 'src/app/shared/services/token-utils.service';
import { UserService } from '../../../../shared/services/user.service';
import { Nationalities } from '../../../../shared/enums/nationality.enum';
import { RegexConstants } from '../../../../shared/utils/regex-constants';
import { CountryISO } from 'ngx-intl-tel-input';
import { NationalityList } from '../../../../shared/utils/Nationalities';
import { Constants } from '../../../../shared/utils/constants';
import { nationalitiesEnum } from '../../../../shared/enums/nationalities-enum';
import { GenderEnum, GenderEnumsTranslationService } from '../../../../shared/enums/gender.enum';
import { forkJoin, Observable, Subscription } from 'rxjs';
import {LegalStatusClass, LegalStatusEnum} from "../../../../shared/enums/legal-status.enum";
import {ActivityDomainClass} from "../../../../shared/enums/activity-domain.enum";
import {StatusEnumsTranslationService} from "../../../../shared/enums/status.enum";
import {CompanyUserModel} from "../../../../shared/models/company-user-model";
import {RegistryStatusClass} from "../../../../shared/enums/registryStatus.enum";
import {ActivityType} from "../../../../shared/models/activity-type";
import {ReportService} from "../../../../shared/services/report.service";
import {catchError, map} from "rxjs/operators";
import {UiModalComponent} from "../../../../theme/shared/components/modal/ui-modal/ui-modal.component";
import {LoginService} from "../../../../shared/services/login.service";
@Component({
  selector: 'app-internal-user-add',
  templateUrl: './internal-user-add.component.html',
  styleUrl: './internal-user-add.component.scss'
})
export class InternalUserAddComponent implements OnInit {
  protected readonly Constants = Constants;
  private subscriptions: Subscription = new Subscription();
  protected userForm: any;
  protected companyUserForm: any;
  protected activityTypesForm: any;
  protected user = new User();
  protected companyUser = new CompanyUserModel();
  protected groups: Group[];
  protected roles: Role[];
  protected currentUserGroup: string;
  protected isAdmin: boolean = true;
  protected username: string;
  public countryFlagUrl: string = '';
  public minDate: string;
  public ccurrentLanguage: string;
  public countries: any[];
  public legalStatusList: any[];
  public registryStatusList: { id: string; label: any }[] = [];
  public genderList: { id: string; label: any }[] = [];
  public activityDomainList: any[];
  public statusList: any[];
  public defaultCountry: { phonePrefix: string; flagClass: string; code: string; label: string };

  public isLoading: boolean = false;
  public validFileFormat: boolean = true;
  public validFilePatentFormat: boolean = true;
  public selectedFileName: string | null = null;
  public progressBarValue: number = 0;
  public photoFile: File | null = null;
  public photoFilePatent: File | null = null;

  @ViewChild('fileInput') fileInput: ElementRef;
  @ViewChild('filePatentInput') filePatentInput: ElementRef;

  public allowedExtensions: string[] = ['pdf'];
  public progressBarValuefilePatent: number = 0;
  public isLoadingFilePatent: boolean = false;
  public countryISOValues = Object.values(CountryISO);
  public nationalityList = NationalityList;
  public selectedCountry: CountryISO = CountryISO.Tunisia;
  public phoneNumberPrefix: string = RegexConstants.PHONE_PREFIX;
  public fileStatus: File;
  public filePatent: File;
  public showCin: boolean = true;
  public showPassport: boolean = false;
  public physique: boolean = true;
  public morale: boolean = false;
  public externalUserData: any
  @ViewChild('cinVerificationsModal') protected cinVerificationsModal: UiModalComponent;

  constructor(
    private translatePipe: TranslatePipe,
    private router: Router,
    private route: ActivatedRoute,
    private toastrService: AppToastNotificationService,
    private fb: FormBuilder,
    private roleService: RoleService,
    private tokenUtilsService: TokenUtilsService,
    private groupsService: GroupsService,
    private userService: UserService,
    private translateservice: TranslateService,
    private genderEnumsTranslationService: GenderEnumsTranslationService,
    private legalStatusClass: LegalStatusClass,
    private activityDomainClass: ActivityDomainClass,
    private statusEnumsTranslationService: StatusEnumsTranslationService,
    private registryStatusTranslatioService: RegistryStatusClass,
    private reportService: ReportService,
    private loginService:LoginService
  ) {
    this.username = this.tokenUtilsService.getUsername();
    this.defaultCountry = this.nationalityList.find((item) => item.code === Constants.TUNISIE.toUpperCase());
    this.legalStatusList = this.legalStatusClass.getLegalStatus();
    this.activityDomainList = this.activityDomainClass.getActivity();
    const today = new Date();
    const minDate = new Date(today.getFullYear() - 18, today.getMonth(), today.getDate());
    this.minDate = minDate.toISOString().split('T')[0];
  }

  public ngOnInit(): void {
    this.subscriptions.add(
      this.statusEnumsTranslationService.statusList$.subscribe((list) => {
        this.statusList = list;
      })
    );
    this.mapToCountryISO(this.nationalityList);

    this.translateservice.onLangChange.subscribe(() => {
      this.mapToCountryISO(this.nationalityList);
      this.ccurrentLanguage = this.translateservice.currentLang;
    });
    this.subscriptions.add(
      this.genderEnumsTranslationService.genderList$.subscribe((list) => {
        this.genderList = list;
      })
    );
    this.subscriptions.add(
        this.registryStatusTranslatioService.registryStatusList$.subscribe((list) => {
          this.registryStatusList = list ;
        })
    )
    this.initUserForm();
    this.currentUserGroup = this.tokenUtilsService.getUserGroup();
    this.loadGroups();
    this.loadRoles();
    this.showNationalId();

  }


  private initUserForm(): void {
    this.userForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.pattern(RegexConstants.FIRST_NAME_LAST_NAME_REGEX)]],
      lastName: ['', [Validators.required, Validators.pattern(RegexConstants.FIRST_NAME_LAST_NAME_REGEX)]],
      email: ['', [Validators.required, Validators.email, Validators.pattern(RegexConstants.EMAIL)]],
      birthdate: ['', [Validators.required], this.dateValidator()],
      group: [[], [Validators.required]],
      role: [[], [Validators.required]],
      country: [this.defaultCountry, [Validators.required]],
      passport: ['', [Validators.required, Validators.pattern(RegexConstants.PASSPORT)]],
      cin: ['', [Validators.pattern(RegexConstants.CIN)]],
        phoneNumber: [
          {   number: this.phoneNumberPrefix,
            dialCode: this.phoneNumberPrefix,
            countryCode: this.selectedCountry},
          {
            validators: [Validators.required, this.phoneNumberValidator(this.selectedCountry)],
            updateOn: 'blur'
          }
        ],

      address: ['', [Validators.required, Validators.pattern(RegexConstants.ADDRESS_LENGTH)]],
      gender: ['', Validators.required],
    });
    this.userForm.get('country')?.setValue(this.defaultCountry);
    this.handleCountryChange(this.defaultCountry);
  }

  private initCompanyUserForm(): void {
    this.companyUserForm = this.fb.group({
      email: ['', [Validators.required, Validators.email, Validators.pattern(RegexConstants.EMAIL)]],
      phoneNumber: [
        {   number:this.phoneNumberPrefix,
          dialCode: this.phoneNumberPrefix,
          countryCode: this.selectedCountry},
        {
          validators: [Validators.required, this.phoneNumberValidator(this.selectedCountry)],
          updateOn: 'blur'
        }
      ],
      address: ['', [Validators.required, Validators.pattern(RegexConstants.ADDRESS_LENGTH)]],
      taxRegistration: ['', [Validators.required, Validators.pattern(RegexConstants.LOWERCASE)]],
      socialReason: ['', [Validators.required]],
      legalStatus: ['', [Validators.required]],
      country: [this.defaultCountry, [Validators.required]],
      denomination: ['', [Validators.required]],
      registryStatus: ['', [Validators.required]],
      fileStatus: ['', [Validators.required]],
      filePatent: ['', [Validators.required]]
    });
    this.handleCountryChange(this.defaultCountry);
    this.initActivityTypesForm();
  }
  private initActivityTypesForm(): void {
    this.activityTypesForm = this.fb.group({
      basicActivityTypeCode: ['', Validators.required],
      basicActivityTypeName: ['', Validators.required],
      secondaryActivityTypes: this.fb.array([])
    });
  }

  get secondaryActivityTypes(): FormArray {
    return this.activityTypesForm.get('secondaryActivityTypes') as FormArray;
  }

  public addSecondaryActivityType(): void {
    const secondaryActivityGroup = this.fb.group({
      code: [''],
      name: ['']
    });
    this.secondaryActivityTypes.push(secondaryActivityGroup);
  }

  public removeSecondaryActivityType(index: number): void {
    this.secondaryActivityTypes.removeAt(index);
  }

  public get file() {
    return this.companyUserForm.get('file');
  }
  public get legalStatus() {
    return this.userForm?.get('legalStatus')!;
  }
  public get creationDate() {
    return this.userForm?.get('creationDate')!;
  }
  public get taxRegistration() {
    return this.userForm?.get('taxRegistration')!;
  }
  public get socialReason() {
    return this.userForm?.get('socialReason')!;
  }
  public get email() {
    return this.userForm?.get('email')!;
  }

  public get group() {
    return this.userForm?.get('group')!;
  }

  public get role() {
    return this.userForm?.get('role')!;
  }

  public get firstName() {
    return this.userForm?.get('firstName')!;
  }

  public get lastName() {
    return this.userForm?.get('lastName')!;
  }

  public get passport() {
    return this.userForm?.get('passport');
  }

  public get cin() {
    return this.userForm?.get('cin');
  }

  public get phoneNumber() {
    return this.userForm?.get('phoneNumber');
  }

  public get address() {
    return this.userForm?.get('address');
  }

  public get gender() {
    return this.userForm?.get('gender');
  }

  public get birthdate() {
    return this.userForm?.get('birthdate');
  }

  protected back(): void {
    this.router.navigate(['../'], { relativeTo: this.route });
  }

  loadGroups(): void {
    this.groupsService.getGroups().subscribe({
      next: (response: any) => {
        this.groups = response.map((group) => {
          return  {
            ...group,
            disabled: group.isActive == false

          };
        });
      },
      error: (error) => {
        this.toastrService.onError(this.translatePipe.transform('groups.FAILED_TO_LOAD_GROUP'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }

  loadRoles(): void {
    this.roleService.findInterneRoles().subscribe(
      (res) => {
        this.roles = res.map((role) => {
          return {
            ...role,
            disabled: (role.label === 'DEFAULT_ROLE' || role.isActive == false)
          };
        });
      },
      (error) => {
        this.toastrService.onError(this.translatePipe.transform('roles.FAILED_TO_LOAD_ROLE'), this.translatePipe.transform('menu.ERROR'));
      }
    );
  }

  protected addUser(confirm:boolean): void {
    this.fillUserData();
    this.userService.addUser(this.user,confirm).subscribe({
      next: (response) => {
        if (response?.username?.startsWith("ext-")){
          this.externalUserData = response ;
          this.cinVerificationsModal.show();
          this.userForm.patchValue({
            firstName: this.externalUserData.firstName,
            lastName: this.externalUserData.lastName,
            birthDate: this.externalUserData.birthDate
          });
        } else {
          this.userForm.reset();
          this.toastrService.onSuccess(
              this.translatePipe.transform('users.successToAddUser'),
              this.translatePipe.transform('menu.SUCCESS')
          );
          this.router.navigate(['../'], { relativeTo: this.route });
        }
      },
      error: (error) => {
        let errorDetails: string[] = error.error.detail.split(',');
        if (errorDetails.length > 0) {
          errorDetails.forEach((error) => {
            this.toastrService.onError(this.translatePipe.transform('users.errors.' + error.trim()), this.translatePipe.transform('menu.ERROR'));
          });
        }
        this.toastrService.onError(
            this.translatePipe.transform('users.errors.FAILED_TO_ADD_USER'),
            this.translatePipe.transform('menu.ERROR')
        );
      } });
  }

  protected addCompanyUser(): void {
    this.fillCompanyUser();

    const taxRegistration = this.companyUser.taxRegistration.replace(/\s/g, "");

    if (!this.fileStatus || !this.filePatent) {
      this.toastrService.onError(
          this.translatePipe.transform('users.errors.FILES_ARE_REQUIRED'),
          this.translatePipe.transform('menu.ERROR')
      );
      return;
    }

    const uploadTasks = [
      this.uploadFile(this.fileStatus, taxRegistration),
      this.uploadFile(this.filePatent, taxRegistration),
    ];

    forkJoin(uploadTasks).subscribe({
      next: ([statusResponse, patentResponse]) => {

        this.companyUser.fileStatus = this.fileStatus.name;
        this.companyUser.filePatent = this.filePatent.name;

        this.userService.addCompanyUser(this.companyUser).subscribe({
          next: () => {
            this.userForm.reset();
            this.toastrService.onSuccess(
                this.translatePipe.transform('users.success.USER_SAVED_SUCCESSFULLY'),
                this.translatePipe.transform('menu.SUCCESS')
            );
            this.router.navigate(['../'], { relativeTo: this.route });
          },
          error: (addUserError) => {
            let errorDetails: string[] = addUserError.error.detail.split(',');
            if (errorDetails.length > 0) {
              errorDetails.forEach((error) => {
                this.toastrService.onError(this.translatePipe.transform('users.errors.' + error.trim()), this.translatePipe.transform('menu.ERROR'));
              });
            }
            this.toastrService.onError(
                this.translatePipe.transform('users.errors.FAILED_TO_ADD_USER'),
                this.translatePipe.transform('menu.ERROR')
            );
          },
        });
      },
      error: (uploadError) => {
        this.toastrService.onError(
            this.translatePipe.transform('file.FAILED_TO_UPLOAD_FILES'),
            this.translatePipe.transform('menu.ERROR')
        );
      },
    });
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
  public fillUserData(): void {
    this.user.firstName = this.userForm.value.firstName;
    this.user.lastName = this.userForm.value.lastName;
    this.user.email = this.userForm.value.email;
    const { selectedGroups, selectedRoles } = this.getSelectedGroupsAndRoles();
    this.user.groups = selectedGroups;
    this.user.roles = selectedRoles;
    this.user.phoneNumber = this.userForm.value.phoneNumber.number;
    this.user.country = this.userForm.value.country?.label;
    const tunisianNationality = nationalitiesEnum.find((n) => n.code === Constants.TUNISIE.toUpperCase());
    const matchedNationality = nationalitiesEnum.find((n) => n.code === this.userForm.value.country?.code);

    if (matchedNationality) {
      this.user.nationality = matchedNationality.nationality;
    } else {
      this.user.nationality = 'STRANGER';
    }
    if (this.user.nationality === tunisianNationality.nationality) {
      this.user.nationalId = this.userForm.value.cin;
    } else {
      this.user.nationalId = this.userForm.value.passport;
    }

    const birthdate = new Date(this.userForm.get('birthdate')?.value);
    const formattedDate = this.formatDate(birthdate);
    this.user.birthDate = formattedDate;
    this.user.address = this.userForm.value.address;
    this.user.gender = this.userForm.value.gender.id;
  }

  public fillCompanyUser() {
    this.companyUser.email = this.companyUserForm.value.email;
    this.companyUser.country = this.companyUserForm.value.country?.label;
    this.companyUser.address = this.companyUserForm.value.address;
    this.companyUser.legalStatus = this.companyUserForm.value.legalStatus.id;
    this.companyUser.socialReason = this.companyUserForm.value.socialReason;
    this.companyUser.taxRegistration = this.companyUserForm.value.taxRegistration;
    this.companyUser.phoneNumber = this.companyUserForm.value.phoneNumber.number;
    const matchedNationality = nationalitiesEnum.find((n) => n.code === this.companyUserForm.value.country?.code);

    if (matchedNationality) {
      this.companyUser.nationality = matchedNationality.nationality;
    } else {
      this.companyUser.nationality = 'STRANGER';
    }
    this.companyUser.denomination = this.companyUserForm.value.denomination;
    this.companyUser.registryStatus = this.companyUserForm.value.registryStatus.id;
    this.getActivityTypes();
  }

  private getActivityTypes() {
    const { basicActivityTypeCode, basicActivityTypeName, fixedSecondaryActivityCode, fixedSecondaryActivityName, secondaryActivityTypes } = this.activityTypesForm.value;

    const createActivityType = (code: string, name: string): ActivityType | null => {
      return code && name ? { code, name } : null;
    };
    const basicActivityType = createActivityType(basicActivityTypeCode, basicActivityTypeName);
    const secondaryActivityType = createActivityType(fixedSecondaryActivityCode, fixedSecondaryActivityName);

    const otherActivityList: ActivityType[] = (secondaryActivityTypes || [])
    .filter(activity => activity?.code && activity?.name)
    .map(activity => createActivityType(activity.code, activity.name) as ActivityType);

    const allActivityCompany: ActivityType[] = [
      ...(basicActivityType ? [basicActivityType] : []),
      ...(secondaryActivityType ? [secondaryActivityType] : []),
      ...otherActivityList
    ];


    this.companyUser.activitiesType = allActivityCompany;
  }

  private getSelectedGroupsAndRoles() {
    const selectedGroupIds: any[] = this.userForm.value.group;
    const selectedRoleIds: any[] = this.userForm.value.role;
    const selectedGroups: Group[] = [];
    const selectedRoles: Role[] = [];
    selectedGroupIds.forEach((el) => {
      selectedGroups.push(this.groups.find((item) => item.id === el));
    });
    selectedRoleIds.forEach((el) => {
      selectedRoles.push(this.roles.find((item) => item.id === el));
    });

    return { selectedGroups, selectedRoles };
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
  public handleCountryChange(selectedCountry: any): void {
    if (selectedCountry) {
      const nationalityDetails = this.countries.find((n) => n.code === selectedCountry.code);

      if (nationalityDetails) {
        this.phoneNumberPrefix = nationalityDetails.phonePrefix;
        this.countryFlagUrl = nationalityDetails.flagClass;
        this.selectedCountry = nationalityDetails.countryISO;
      }

      let form: FormGroup;
      if (this.physique) {
        form = this.userForm;
      } else {
        form = this.companyUserForm;
      }

      if (!form) return;

      const phoneNumberControl = form.get('phoneNumber');
      const cinControl = form.get('cin');
      const passportControl = form.get('passport');
      const addressControl = form.get('address');

      if (selectedCountry?.code.toLowerCase() === Constants.TUNISIE) {
        passportControl?.clearValidators();
        passportControl?.updateValueAndValidity();

        cinControl?.setValidators([Validators.required, Validators.pattern(RegexConstants.CIN)]);
        cinControl?.updateValueAndValidity();

        phoneNumberControl?.setValidators([Validators.required, this.phoneNumberValidator(selectedCountry)]);
      } else {
        cinControl?.clearValidators();
        cinControl?.updateValueAndValidity();

        passportControl?.setValidators([Validators.required, Validators.pattern(RegexConstants.PASSPORT)]);
        passportControl?.updateValueAndValidity();

        phoneNumberControl?.setValidators([Validators.required, this.phoneNumberValidator(selectedCountry)]);
      }

      phoneNumberControl?.setValue(null);
      phoneNumberControl?.patchValue({ number: this.phoneNumberPrefix, dialCode: this.phoneNumberPrefix });
      phoneNumberControl?.updateValueAndValidity();

      addressControl?.setValidators([Validators.required, Validators.pattern(RegexConstants.ADDRESS_LENGTH)]);
      addressControl?.updateValueAndValidity();
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

  public preventPrefixRemoval(event: KeyboardEvent): void {
    const input = event.target as HTMLInputElement;
    const cursorPosition = input.selectionStart || 0;
    const prefixLength = this.phoneNumberPrefix.length;

    if (cursorPosition <= prefixLength && (event.key === 'Backspace' || event.key === 'Delete')) {
      event.preventDefault();
    }

    if (cursorPosition < prefixLength && /[0-9]/.test(event.key)) {
      event.preventDefault();
    }
  }

  public showNationalId(): void {
    if (this.userForm.get('country')?.value?.label !== undefined && this.userForm.get('country')?.value?.label !== Nationalities.TUNISIAN) {
      this.showCin = false;
      this.showPassport = true;
    }
  }

  public dateValidator(): (control: AbstractControl) => ValidationErrors | null {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;
      if (!value) {
        return null;
      }
      const dateRegex = RegexConstants.DATE_FORMAT;
      if (!dateRegex.test(value)) {
        return { invalidDateFormat: true };
      }
      const [day, month, year] = value.split('/').map(Number);
      const date = new Date(year, month - 1, day);
      if (date.getFullYear() !== year || date.getMonth() + 1 !== month || date.getDate() !== day) {
        return { invalidDateFormat: true };
      }

      return null;
    };
  }

  public formatDate(date: Date): string {
    const day = ('0' + date.getDate()).slice(-2);
    const month = ('0' + (date.getMonth() + 1)).slice(-2);
    const year = date.getFullYear();

    return `${day}/${month}/${year}`;
  }

  public preventKeyInput(event: KeyboardEvent): void {
    event.preventDefault();
  }
  public showComponent(choice: any): void {
    if (choice === Constants.PHYSICAL) {
      this.initUserForm();
      this.physique = true;
      this.morale = false;
      this.userForm.get('country')?.setValue(this.defaultCountry);
      this.handleCountryChange(this.defaultCountry);

    } else if (choice === Constants.COMPANY) {
      this.initCompanyUserForm();
      this.physique = false;
      this.morale = true;
      this.companyUserForm.get('country')?.setValue(this.defaultCountry);
      this.handleCountryChange(this.defaultCountry);

    }
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
      event.target.value = null; // Reset input if multiple files are selected
    }
  }

  private processSelectedFile(): void {
    if (!this.photoFile) {
      return;
    }

    this.selectedFileName = this.photoFile.name;
    const fileExtension = this.photoFile.name.split('.').pop().toLowerCase();

    if (!this.allowedExtensions.includes(fileExtension)) {
      this.validFileFormat = false;
      this.toastrService.onError(this.translatePipe.transform('file.EXTENSION'), this.translatePipe.transform('menu.ERROR'));
      return;
    }

    this.isLoading = true;
    this.validFileFormat = true;
    this.progressBarValue = 0;

    this.updateProgressBar('progressBarValue', 'isLoading');
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

    this.updateProgressBar('progressBarValuefilePatent', 'isLoadingFilePatent');
  }
  protected updateProgressBar(
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
  phoneNumberValidator(selectedCountry: any) {
    return (control: AbstractControl) => {
      const value = control.value;
      const pattern = RegexConstants.TUNISIAN_PHONE_NUMBER;

      if (!value || !value.number) {
        return { required: true };
      }

      if (value.number === this.phoneNumberPrefix) {
        return { required: true };
      }
      if (selectedCountry?.code.toLowerCase() === Constants.TUNISIE) {
        if (!pattern.test(value.number)) {
          return { pattern: true };
        }
      }
      return null;
    };
  }
  public confirmAddUserWithExternalCIN(){
    this.cinVerificationsModal.hide();
    this.addUser(true);
  }
}
