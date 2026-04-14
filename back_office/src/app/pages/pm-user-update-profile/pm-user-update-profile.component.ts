import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Group} from "../../shared/models/group.model";
import {Role} from "../../shared/models/role.model";
import {NationalityList} from "../../shared/utils/Nationalities";
import {CountryISO} from "ngx-intl-tel-input";
import {RegexConstants} from "../../shared/utils/regex-constants";
import {
  AbstractControl,
  FormArray,
  FormBuilder,
  FormGroup,
  ValidationErrors,
  Validators
} from "@angular/forms";
import {forkJoin, Observable, Subscription, throwError} from "rxjs";
import {HistoricTaskInstance} from "../../shared/models/historicTaskInstance.model";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";
import {NavigationExtras, Router} from "@angular/router";
import {AppToastNotificationService} from "../../shared/services/appToastNotification.service";
import {UserService} from "../../shared/services/user.service";
import {RoleService} from "../../shared/services/role.service";
import {GroupsService} from "../../shared/services/groups.service";
import {GenderEnumsTranslationService} from "../../shared/enums/gender.enum";
import {TokenUtilsService} from "../../shared/services/token-utils.service";
import {StatusEnumsTranslationService} from "../../shared/enums/status.enum";
import {LegalStatusClass} from "../../shared/enums/legal-status.enum";
import {RegistryStatusClass} from "../../shared/enums/registryStatus.enum";
import {ReportService} from "../../shared/services/report.service";
import {nationalitiesEnum} from "../../shared/enums/nationalities-enum";
import {CompanyUserModel} from "../../shared/models/company-user-model";
import {Constants} from "../../shared/utils/constants";
import {catchError, map} from "rxjs/operators";
import {Components} from "formiojs";
import file = Components.components.file;
import {ActivityType} from "../../shared/models/activity-type";
import Validation from "../../shared/services/validation";
import {Nationalities} from "../../shared/enums/nationality.enum";

@Component({
  selector: 'app-pm-user-update-profile',
  templateUrl: './pm-user-update-profile.component.html',
  styleUrl: './pm-user-update-profile.component.scss'
})
export class PmUserUpdateProfileComponent implements OnInit{
  protected user: any;
  protected companyUserForm: any;
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
  public activityTypesForm: FormGroup;
  public currentLegalStatus: { id: string; label: any };
  public state = window.history.state;
  public morale: boolean = false;
  public isLoading: boolean = false;
  public validFileFormat: boolean = true;
  public validFilePatentFormat: boolean = true;
  public selectedFileName: string | null = null;
  public progressBarValue: number = 0;
  photoFile: any = null;  // Declare these if not already (for the template's *ngIf)
  photoFilePatent: any = null;
  private subscriptions: Subscription = new Subscription();
  public physique: boolean = false;
  isLoadingFilePatent: boolean = false;
  public totalCount: number;
  protected userId: string;
  protected tasks: any[] = [];
  protected taskId: string;
  public currentRegistryStatus: { id: string; label: any };
  protected registryStatusList: { id: string; label: any }[] = [];
  public statusList: { id: string; label: any }[] = [];
  protected userForm: any;
  public currentStatus: string;
  public allowedExtensions: string[] = ['pdf'];
  public progressBarValuefilePatent: number = 0;
  public fileStatus: File;
  public filePatent: File;
  public legalStatusList: any[];
  private historicTasks: HistoricTaskInstance[] = [];
  @ViewChild('fileInput') fileInput: ElementRef;
  @ViewChild('filePatentInput') filePatentInput: ElementRef;
  public countryFlagUrl: string = '';
  public defaultCountry: { phonePrefix: string; flagClass: string; code: string; label: string };
  public otherActivities: boolean = false;
  protected readonly Nationalities = Nationalities;

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
      private statusEnumsTranslationService: StatusEnumsTranslationService,
      private legalStatusClass: LegalStatusClass,
      private registryStatusTranslatioService: RegistryStatusClass,
      private reportService: ReportService,

  ) {
    this.legalStatusList = this.legalStatusClass.getLegalStatus();
  }

  public ngOnInit(): void {
    this.userId = this.tokenUtilisService.getUserId();
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
    this.mapToCountryISO(this.nationalityList);
    this.translateservice.onLangChange.subscribe(() => {
      this.mapToCountryISO(this.nationalityList);
      this.patchFormData();
    });
    // this.countries = this.mapToCountryISO(this.nationalityList);
    this.state = window.history.state;
    this.userId = this.state.userId;
    this.initCompanyUserForm()
    this.handleOnGetUser(this.userId);
    this.loadRoles();
  }

  private initCompanyUserForm(): void {
    this.companyUserForm = this.fb.group({
      email: [{value: '', disabled: false}, [Validators.required, Validators.email, Validators.pattern(RegexConstants.EMAIL)]],
      phoneNumber: [{ value: '' ,  disabled: false} , Validators.required],
      address: [{value: '', disabled: false}, [Validators.required, Validators.pattern(RegexConstants.ADDRESS_LENGTH)]],
      taxRegistration:[{value: '', disabled: true}],
      socialReason:[{value: '', disabled: false},[Validators.required]],
      legalStatus:[{value: '', disabled: false},[Validators.required]],
      denomination: [{value: '', disabled: false},[Validators.required]],
      registryStatus: [{value: '', disabled: false},Validators.required],
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
  public fillCompanyUser() {
    this.user.address = this.companyUserForm.value?.address;
    this.user.filePatent = this.companyUserForm.value?.filePatent;
    this.user.legalStatus = this.companyUserForm.value?.legalStatus.id;
    this.user.socialReason = this.companyUserForm.value?.socialReason;
    const rawFormValue = this.companyUserForm.getRawValue();
    this.user.taxRegistration = rawFormValue?.taxRegistration;
    this.user.phoneNumber = this.companyUserForm.value.phoneNumber.number;
    this.user.email = this.companyUserForm.value?.email;
    this.user.denomination = this.companyUserForm.value?.denomination;
    this.user.registryStatus = this.companyUserForm.value?.registryStatus.id;
    this.user.fileStatus = this.companyUserForm.value?.fileStatus;
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

    this.user.activitiesType = allActivityCompany;
  }

  private getOtherActivityValues(secondaryActivityTypes, createOrUpdateActivityType: (initialActivity: Partial<ActivityType>, code: string, name: string) => (ActivityType | null)) {
    const otherActivityList: ActivityType[] = (secondaryActivityTypes || [])
    .filter((activity) => activity?.code && activity?.name)
    .map((activity) => {
      const originalActivity = this.user.activitiesType?.find((act) => act.code === activity.code) || {};
      return createOrUpdateActivityType(originalActivity, activity.code, activity.name) as ActivityType;
    });
    return otherActivityList;
  }

  private getBasicActivityValues(createOrUpdateActivityType: (initialActivity: Partial<ActivityType>, code: string, name: string) => (ActivityType | null), basicActivityTypeId, basicActivityTypeCode, basicActivityTypeName) {
    const basicActivityType = createOrUpdateActivityType(
        this.user.activitiesType?.find((act) => act.id === basicActivityTypeId) || {},
        basicActivityTypeCode,
        basicActivityTypeName
    );
    return basicActivityType;
  }
  public updateProgressBar(value: number): void {
    this.progressBarValuefilePatent = value;
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
  private patchFormData(): void {
    const roleLabels = this.user?.roles.map((role) => role.description);
      const registryStatus = this.getCurrentCompanyStatus(this.user);
      const legalStatus = this.getCurrentLegalStatus(this.user);
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
      legalStatus: legalStatus ?? '',
      socialReason: this.user?.socialReason ?? '',
      phoneNumber: this.user?.phoneNumber ?? '',
      registryStatus: registryStatus ?? '',
      email: this.user?.email ?? '',
      address: this.user?.address ?? '',
      denomination: this.user?.denomination ?? '',
      fileStatus: this.user?.fileStatus ?? '',
      filePatent: this.user?.filePatent ?? '',
      activityTypes: this.user?.activitiesType ?? [],
      role: [roleLabels?.join(', ') ?? ''],
    });
      if (this.user.activitiesType) {
        if (this.user.activitiesType.length > 0) {
          const activities = [...this.user.activitiesType];

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
     this.photoFile = this.user.fileStatus ? { name: this.user.fileStatus } : null;
     this.photoFilePatent = this.user.filePatent ? { name: this.user.filePatent } : null;

        this.currentLegalStatus = this.legalStatusList.find(el => el.id === this.user.legalStatus);

        this.initCompanyUserForm();
        this.initActivityTypesForm();

        this.patchFormData();
      },
      error: () => {
        this.toastrService.onError(this.translatePipe.transform('users.errors.FAILED_TO_LOAD_USER'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }
  public handleNationalityChange(selectedNationality: any): void {
    if (selectedNationality?.label !== this.Nationalities.TUNISIAN) {
      this.companyUserForm.get('cin')?.clearValidators();
      this.companyUserForm.get('passport')?.setValidators([Validators.required, Validators.pattern(RegexConstants.PASSPORT)]);
    } else if (selectedNationality?.value === this.Nationalities.TUNISIAN) {
      this.companyUserForm.get('passport')?.clearValidators();
      this.companyUserForm.get('cin')?.setValidators([Validators.required, Validators.pattern(RegexConstants.CIN)]);
      this.companyUserForm.get('phoneNumber')?.setValidators([Validators.required, Validators.minLength(8), Validators.maxLength(8)]);

    }
    if (selectedNationality) {
      const nationalityDetails = this.countries.find((n) => n.code === selectedNationality.code);

      if (nationalityDetails) {
        this.phoneNumberPrefix = nationalityDetails.phonePrefix;
        this.countryFlagUrl = nationalityDetails.flagUrl;
        this.selectedCountry = nationalityDetails.countryISO.replace(this.phoneNumberPrefix, '');
        this.companyUserForm.get('phoneNumber')?.setValue(this.phoneNumberPrefix);
      }
    }
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
  public get denomination() {
    return this.companyUserForm?.get('denomination')!;
  }

  public get socialReason() {
    return this.companyUserForm?.get('socialReason')!;
  }

  public get taxRegistration() {
    return this.companyUserForm?.get('taxRegistration')!;
  }

  public get email() {
    return this.companyUserForm?.get('email')!;
  }

  public get legalStatus() {
    return this.companyUserForm?.get('legalStatus')!;
  }

  public get registryStatus() {
    return this.companyUserForm?.get('registryStatus')!;
  }

  public get phoneNumber() {
    return this.companyUserForm?.get('phoneNumber');
  }

  public get address() {
    return this.companyUserForm?.get('address');
  }


  public get activityDomain() {
    return this.companyUserForm?.get('activityDomain');
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
  public handleDragOver(event: DragEvent): void {
    event.preventDefault();
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
    setTimeout(incrementProgress, interval);}



  phoneNumberValidator(selectedCountry: any) {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;

      if (!value || typeof value !== 'object' || !value.nationalNumber) {
        return { required: true };
      }

      const nationalNumber = value.nationalNumber.trim();

      if (!nationalNumber) {
        return { required: true };
      }

      if (selectedCountry?.code?.toLowerCase() === Constants.TUNISIE) {
        if (!RegexConstants.TUNISIAN_PHONE_NUMBER.test(nationalNumber)) {
          return { pattern: true };
        }
      }

      return null;
    };
  }
  protected back(): void {
    this.router.navigate(['/pages/user-management/internal-user-management']);
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
  public viewToasterSuccess(): void {
    this.toastrService.onSuccess(this.translatePipe.transform('users.UPDATE_SUCCESS'), this.translatePipe.transform('menu.SUCCESS'));
    this.router.navigate(['/pages/pm-user-profile',this.userId]);

  }

  protected fetchAndDownloadFile(fileName: string): void {
    const bucketName = 'notification';
    this.reportService.getFileUrl(fileName, bucketName).subscribe({
      next: (fileUrl: string) => {
        this.downloadFile(fileUrl, fileName);
      },
      error: (error) => {
        alert('Failed to fetch the file. Please try again.');
      }
    });
  }

  private downloadFile(fileUrl: string, fileName: string): void {
    const link = document.createElement('a');
    link.href = fileUrl;
    link.download = fileName;
    link.target = '_blank';
    link.click();
  }
  protected updateUserCompany(): void {
    this.fillCompanyUser();
    const taxRegistration = this.companyUserForm.get('taxRegistration')?.value
    .toLowerCase()
    .replace(/[^a-z0-9-]/g, '-')
    .replace(/-+/g, '-')
    .replace(/^-|-$/g, '')
    .slice(0, 50);

    const hasNewFileStatus = this.photoFile && this.photoFile instanceof File;
    const hasNewFilePatent = this.photoFilePatent && this.photoFilePatent instanceof File;

    if (!hasNewFileStatus && !hasNewFilePatent) {
      this.userService.updateCompanyUser(this.userId, this.user).subscribe({
        next: () => {
          this.viewToasterSuccess();
          this.router.navigate(['/pages/user-management/internal-user-management']);
        },
        error: (addUserError) => {
          this.handleUpdateError(addUserError);
        }
      });
      return;
    }

    const uploadTasks: Observable<any>[] = [];

    if (hasNewFileStatus) {
      uploadTasks.push(this.uploadFile(this.photoFile, taxRegistration));
    }

    if (hasNewFilePatent) {
      uploadTasks.push(this.uploadFile(this.photoFilePatent, taxRegistration));
    }

    forkJoin(uploadTasks).subscribe({
      next: (responses) => {
        if (hasNewFileStatus) {
          this.user.fileStatus = this.photoFile.name;
        }

        if (hasNewFilePatent) {
          this.user.filePatent = this.photoFilePatent.name;
        }

        this.userService.updateCompanyUser(this.userId, this.user).subscribe({
          next: () => {
            this.viewToasterSuccess();
            this.router.navigate(['/pages/user-management/internal-user-management']);
          },
          error: (addUserError) => {
            this.handleUpdateError(addUserError);
          }
        });
      },
      error: (uploadError) => {
        console.error('Upload error:', uploadError);
        this.toastrService.onError(
            this.translatePipe.transform('file.FAILED_TO_UPLOAD_FILES'),
            this.translatePipe.transform('menu.ERROR')
        );
      }
    });
  }

  private handleUpdateError(addUserError: any): void {
    const errorDetail = addUserError.error?.detail;
    if (errorDetail) {
      let errorDetails: string[] = errorDetail.split(',');
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
    const phoneControl = this.companyUserForm.get('phoneNumber');
    phoneControl?.updateValueAndValidity();
    phoneControl?.markAsTouched();
  }


}
