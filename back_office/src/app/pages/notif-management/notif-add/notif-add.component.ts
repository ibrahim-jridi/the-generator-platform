import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { NotificationService } from '../../../shared/services/notification.service';
import { DestinationTypeEnum } from '../../../shared/enums/destination-type.enum';
import { TopicEnum } from '../../../shared/enums/topic.enum';
import { UserService } from '../../../shared/services/user.service';
import { FilterValuesModel } from '../../../shared/models/filter-values-model';
import { PaginationArgs, PaginationSortArgs, PaginationSortOrderType } from '../../../shared/models/paginationArgs.model';
import { RoleService } from '../../../shared/services/role.service';
import { GroupsService } from '../../../shared/services/groups.service';
import { RoleCriteria } from '../../../shared/models/roleCriteria.model';
import { FrequencyTypeEnum } from '../../../shared/enums/frequencyType.enum';
import { Notification } from '../../../shared/models/notification.model';
import { BroadcastChannelEnum } from '../../../shared/enums/broadcast-channel.enum';
import { ConfigurationDayEnum } from '../../../shared/enums/configuration-day.enum';
import { FrequencyConfigEnum } from '../../../shared/enums/frequency-config.enum';
import { AppToastNotificationService } from '../../../shared/services/appToastNotification.service';
import { TranslatePipe } from '@ngx-translate/core';
import { Days } from '../../../shared/models/days.model';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { RegexConstants } from '../../../shared/utils/regex-constants';
import {EnumTranslationService} from "../../../shared/services/enum-translation.service";

@Component({
  selector: 'app-notif-add',
  templateUrl: './notif-add.component.html',
  styleUrl: './notif-add.component.scss'
})
export class NotifAddComponent implements OnInit {
  protected notification = new Notification();
  protected isReadOnly: boolean = true;
  protected notificationForm: FormGroup;
  protected pageSize: number;
  protected FrequencyEnum = FrequencyTypeEnum;
  protected FrequencyConfigEnum = FrequencyConfigEnum;
  private subscriptions: Subscription = new Subscription();
  protected ConfigurationDay = ConfigurationDayEnum;
  protected notificationDestinationsList = [];
  protected selectedFrequency: FrequencyTypeEnum | null = null;
  protected activeOption: string | null = null;
  protected selectedDailyFrequency: ConfigurationDayEnum | null = null;
  protected dailyFrequencyOptions: { id: string; label: any }[] = [];
  protected intervalOptions: { label: any; id: string }[] = [];
  protected destinationTypeOptions: { label: any; id: string }[] = [];
  public selectedDays: string[] = [];
  protected days: Days[] = [];
  private filterValues: FilterValuesModel[];
  private paginationArgs: PaginationArgs;
  private paginationSortArgs: PaginationSortArgs = new PaginationSortArgs('lastModifiedDate', PaginationSortOrderType.DESC);
  public pageIndex: number = 0;
  private criteria: RoleCriteria = new RoleCriteria();
  private selectedFile: File;
  public fileError: string;
  protected allowedExtensions: string[] = ['*'];
  public minDate: string;
  public minDateTime: string;

  constructor(
    private fb: FormBuilder,
    private notificationService: NotificationService,
    private userService: UserService,
    private roleService: RoleService,
    private groupsService: GroupsService,
    private toastrService: AppToastNotificationService,
    private translatePipe: TranslatePipe,
    private router: Router,
    private route: ActivatedRoute,
    private enumTranslationService: EnumTranslationService
  ) {}

  public ngOnInit(): void {
    const today = new Date();
    this.minDate = today.toISOString().split('T')[0];
    const year = today.getFullYear();
    const month = ('0' + (today.getMonth() + 1)).slice(-2);
    const day = ('0' + today.getDate()).slice(-2);
    const hours = ('0' + today.getHours()).slice(-2);
    const minutes = ('0' + today.getMinutes()).slice(-2);
    this.minDateTime = `${year}-${month}-${day}T${hours}:${minutes}`;
    this.subscriptions.add(
        this.enumTranslationService.initTranslationList(ConfigurationDayEnum, 'notif.configuration_day')
        .subscribe((list) => (this.dailyFrequencyOptions = list))
    );

    this.subscriptions.add(
        this.enumTranslationService.initTranslationList(FrequencyConfigEnum, 'notif.frequency_config')
        .subscribe((list) => (this.intervalOptions = list))
    );

    this.subscriptions.add(
        this.enumTranslationService.initTranslationList(DestinationTypeEnum, 'notif.destination_type')
        .subscribe((list) => (this.destinationTypeOptions = list))
    );
    this.initNotifsForm();
    this.pageSize = 5;
    this.loadDays();
  }

  private initNotifsForm(): void {
    function atLeastOneChannelValidator(): ValidatorFn {
      return (control: AbstractControl): ValidationErrors => {
        const emailChannel = control.get('emailChannel')?.value;
        const pushChannel = control.get('pushChannel')?.value;
        if (!emailChannel && !pushChannel) {
          return { atLeastOneChannel: true };
        }
        return null;
      };
    }

    this.notificationForm = this.fb.group(
      {
        name: ['', [Validators.required, Validators.pattern(RegexConstants.NOTIF_PATTERN_LABEL)]],
        destinationType: ['', [Validators.required]],
        notificationDestinations: ['', [Validators.required]],
        emailChannel: [false],
        pushChannel: [false],
        frequencyEnum: [''],
        startDate: [null],
        endDate: [null],
        time: [''],
        customRows: this.fb.array([this.createRow()]),
        repeatDailyNotification: [false],
        description: ['', [Validators.required, Validators.maxLength(50)]],
        frequencies: this.fb.array([]),
        selectedDays: this.fb.array([]),
        dailyFrequency: [''],
        file: [null],
        isFromTask: [false]
      },
      { validators: atLeastOneChannelValidator() }
    );
  }

  protected onTaskRecipientToggle(isFromTask: boolean): void {
    this.notificationForm.get('isFromTask')?.setValue(isFromTask);
    if (isFromTask === true) {
      this.notificationForm.get('destinationType')?.clearValidators();
      this.notificationForm.get('destinationType')?.reset();
      this.notificationForm.get('destinationType')?.updateValueAndValidity();
      this.notificationForm.get('notificationDestinations')?.clearValidators();
      this.notificationForm.get('notificationDestinations')?.reset();
      this.notificationForm.get('notificationDestinations')?.updateValueAndValidity();
    } else if (isFromTask === false) {
      this.notificationForm.get('destinationType')?.setValidators([Validators.required]);
      this.notificationForm.get('notificationDestinations')?.setValidators([Validators.required]);
      this.notificationForm.get('destinationType')?.updateValueAndValidity();
      this.notificationForm.get('notificationDestinations')?.updateValueAndValidity();
    }
  }

  private loadUsers(): void {
    this.userService.getAllPhysicalUsers().subscribe({
      next: (response) => {
        this.notificationDestinationsList = response.map((user) => ({
          id: user.id,
          label: `${user.firstName} ${user.lastName}`
        }));
      }
    });
  }

  protected clearValidator(): void {
    if (this.notificationForm.get('repeatDailyNotification').value == false) {
      this.notificationForm.get('endDate').reset();
    }
  }

  private loadRoles(): void {
    this.paginationArgs = {
      sort: this.paginationSortArgs.sort,
      page: this.pageIndex,
      size: this.pageSize
    };
    this.roleService.getAllRoles(this.criteria, this.paginationArgs).subscribe({
      next: (data) => {
        this.notificationDestinationsList = data.map((role) => ({
          id: role.id,
          label: role.label
        }));
      }
    });
  }

  private loadGroups(): void {
    this.paginationArgs = {
      sort: this.paginationSortArgs.sort,
      page: this.pageIndex,
      size: this.pageSize
    };
    this.groupsService.getAllGroups(this.filterValues, this.paginationArgs).subscribe({
      next: (response) => {
        this.notificationDestinationsList = response.body.map((group) => ({
          id: group.id,
          label: group.label
        }));
      }
    });
  }

  protected populateNotificationDestinationsOptions(item): void {
    this.notificationForm.get('notificationDestinations')?.reset();
    this.notificationDestinationsList = [];
    if (item === DestinationTypeEnum.USER) {
      this.loadUsers();
    } else if (item === DestinationTypeEnum.GROUP) {
      this.loadGroups();
    } else if (item === DestinationTypeEnum.ROLE) {
      this.loadRoles();
    }
  }

  private loadDays(): void {
    this.notificationService.getDays().subscribe({
      next: (data) => {
        this.days = data;
        this.initializeDayCheckboxes();
      }
    });
  }

  private initializeDayCheckboxes(): void {
    const selectedDaysArray = this.notificationForm.get('selectedDays') as FormArray;
    selectedDaysArray.clear();

    this.days.forEach(() => {
      selectedDaysArray.push(new FormControl(false));
    });
  }

  protected onCheckboxChange(index: number, event: Event): void {
    const selectedDaysArray = this.notificationForm.get('selectedDays') as FormArray;
    selectedDaysArray.at(index).setValue((event.target as HTMLInputElement).checked);
  }

  public get customRows(): FormArray {
    return this.notificationForm.get('customRows') as FormArray;
  }

  protected onFrequencyChange(value: string): void {
    this.selectedFrequency = value as FrequencyTypeEnum;
    this.activeOption = null;
  }

  protected toggleOption(option: { label: any; id: string }): void {
    this.activeOption = option.id;
    const selectedDaysArray = this.notificationForm.get('selectedDays') as FormArray;
    this.notificationForm.get('time')?.reset();
    const timeControl = this.notificationForm.get('time');
    const customRowsArray = this.notificationForm.get('customRows') as FormArray;
    if (this.activeOption === FrequencyConfigEnum.DAY) {
      this.selectedDailyFrequency = null;
      this.notificationForm.get('dailyFrequency')?.reset();
    }
    if (this.activeOption === FrequencyConfigEnum.HOUR || FrequencyConfigEnum.MINUTE) {
      timeControl?.setValidators([Validators.required]);
    }
    if (this.selectedDailyFrequency !== ConfigurationDayEnum.WEEKLY) {
      selectedDaysArray.clearValidators();
      selectedDaysArray.reset();
    }
    if (this.selectedDailyFrequency !== ConfigurationDayEnum.CUSTOM) {
      customRowsArray.controls.forEach((row) => {
        row.get('dateTime')?.clearValidators();
        row.get('dateTime')?.updateValueAndValidity();
      });
    }
    timeControl?.updateValueAndValidity();
    this.notificationForm.get('startDate')?.reset();
    this.notificationForm.get('time')?.reset();
    this.notificationForm.get('endDate')?.reset();
    this.notificationForm.get('repeatDailyNotification')?.reset();
  }

  private atLeastOneDayValidator(): ValidatorFn {
    return (formArray: AbstractControl): ValidationErrors | null => {
      const selected = (formArray as FormArray).controls.some((control) => control.value === true);
      return selected ? null : { atLeastOneDayRequired: true };
    };
  }

  protected onDailyFrequencyChange(event: any): void {
    this.selectedDailyFrequency = event.id as ConfigurationDayEnum;
    this.notificationForm.get('time')?.reset();
    this.notificationForm.get('startDate')?.reset();
    this.notificationForm.get('endDate')?.reset();
    const startDateControl = this.notificationForm.get('startDate');
    const timeControl = this.notificationForm.get('time');
    const selectedDaysArray = this.notificationForm.get('selectedDays') as FormArray;
    selectedDaysArray.clear();
    this.days.forEach(() => {
      selectedDaysArray.push(new FormControl(false));
    });
    const customRowsArray = this.notificationForm.get('customRows') as FormArray;
    if (this.selectedDailyFrequency === ConfigurationDayEnum.DAILY) {
      this.notificationForm.get('repeatDailyNotification')?.setValue(true);
      startDateControl?.setValidators([Validators.required]);
      timeControl?.setValidators([Validators.required]);
    } else if (this.selectedDailyFrequency === ConfigurationDayEnum.WEEKLY) {
      selectedDaysArray.setValidators(this.atLeastOneDayValidator());
      timeControl?.setValidators([Validators.required]);
    } else if (this.selectedDailyFrequency === ConfigurationDayEnum.CUSTOM) {
      customRowsArray.controls.forEach((row) => {
        row.get('dateTime')?.setValidators([Validators.required]);
        row.get('dateTime')?.updateValueAndValidity();
        this.notificationForm.get('time')?.clearValidators();
        this.notificationForm.get('time')?.updateValueAndValidity();
      });
    } else {
      selectedDaysArray.clearValidators();
      selectedDaysArray.reset();
      startDateControl?.clearValidators();
      timeControl?.clearValidators();
      this.notificationForm.get('repeatDailyNotification')?.setValue(false);
      customRowsArray.controls.forEach((row) => {
        row.get('dateTime')?.clearValidators();
        row.get('dateTime')?.updateValueAndValidity();
      });
    }
    startDateControl?.updateValueAndValidity();
    timeControl?.updateValueAndValidity();
    selectedDaysArray.updateValueAndValidity();
  }

  private createRow(): FormGroup {
    return this.fb.group({
      dateTime: ['']
    });
  }

  protected addRow(): void {
    const newRow = this.fb.group({
      dateTime: ['']
    });

    if (this.selectedDailyFrequency === ConfigurationDayEnum.CUSTOM) {
      newRow.get('dateTime')?.setValidators([Validators.required]);
      newRow.get('dateTime')?.updateValueAndValidity();
    }

    this.customRows.push(newRow);
  }

  protected removeRow(index: number): void {
    this.customRows.removeAt(index);
  }

  protected onFocus(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.value) {
      input.value = '00:';
    }
  }

  protected onMinuteInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.value.startsWith('00:')) {
      input.value = '00:';
    }
    let minutes = input.value.slice(3);
    minutes = minutes.replace(/[^0-9]/g, '');
    if (minutes.length > 2) {
      minutes = minutes.slice(0, 2);
    }
    input.value = `00:${minutes}`;
    this.notificationForm.get('time')?.setValue(input.value);
  }

  protected onFileSelected(file: File): void {
    if (file) {
      this.selectedFile = file;
      this.fileError = null;
    } else {
      this.selectedFile = null;
      this.fileError = 'Aucun fichier sélectionné.';
    }
  }

  protected onFileCleared(): void {
    this.selectedFile = null;
    this.fileError = 'Aucun fichier sélectionné.';
  }

  protected back(): void {
    this.router.navigate(['/pages/notif-management']);
  }

  protected onSubmit(): void {
    if (this.notificationForm.valid) {
      const formValue = this.notificationForm.value;
      const broadcastChannel =
        formValue.emailChannel && formValue.pushChannel
          ? BroadcastChannelEnum.BOTH
          : formValue.emailChannel
          ? BroadcastChannelEnum.EMAIL
          : formValue.pushChannel
          ? BroadcastChannelEnum.NOTIFICATION_PUSH
          : undefined;

      let frequencies = null;

      if (formValue.frequencyEnum === FrequencyTypeEnum.CONFIGURE && this.selectedDailyFrequency === ConfigurationDayEnum.DAILY) {
        frequencies = [
          {
            startDate: formValue.startDate,
            endDate: formValue.endDate,
            isRepeat: formValue.repeatDailyNotification,
            timing: formValue.time,
            configurationDay: ConfigurationDayEnum.DAILY,
            frequencyConfig: FrequencyConfigEnum.DAY
          }
        ];
      }
      if (formValue.frequencyEnum === FrequencyTypeEnum.CONFIGURE && this.selectedDailyFrequency === ConfigurationDayEnum.WEEKLY) {
        const selectedDays = this.days
          .filter((_, index) => formValue.selectedDays[index])
          .map((day) => ({
            id: day.id,
            name: day.name
          }));

        frequencies = [
          {
            isRepeat: formValue.repeatDailyNotification,
            timing: formValue.time,
            endDate: formValue.endDate,
            frequencyConfig: FrequencyConfigEnum.DAY,
            configurationDay: ConfigurationDayEnum.WEEKLY,
            days: selectedDays
          }
        ];
      }
      if (formValue.frequencyEnum === FrequencyTypeEnum.CONFIGURE && this.selectedDailyFrequency === ConfigurationDayEnum.CUSTOM) {
        frequencies = formValue.customRows.map((row) => {
          if (!row.dateTime || !row.dateTime.includes('T')) {
            return null;
          }
          const [date, time] = row.dateTime.split('T');
          return {
            startDate: date,
            timing: time,
            isRepeat: false,
            frequencyConfig: FrequencyConfigEnum.DAY,
            configurationDay: ConfigurationDayEnum.CUSTOM
          };
        });
      }
      if (formValue.frequencyEnum === FrequencyTypeEnum.CONFIGURE && this.activeOption === FrequencyConfigEnum.HOUR) {
        frequencies = [
          {
            frequencyConfig: FrequencyConfigEnum.HOUR,
            isRepeat: formValue.repeatDailyNotification,
            timing: formValue.time,
            endDate: formValue.endDate
          }
        ];
      }
      if (formValue.frequencyEnum === FrequencyTypeEnum.CONFIGURE && this.activeOption === FrequencyConfigEnum.MINUTE) {
        const minutes = formValue.time?.substring(3, 5);
        frequencies = [
          {
            frequencyConfig: FrequencyConfigEnum.MINUTE,
            isRepeat: formValue.repeatDailyNotification,
            timing: minutes,
            endDate: formValue.endDate
          }
        ];
      }

      let notification: Notification = {
        name: formValue.name,
        description: formValue.description,
        broadcastChannel: broadcastChannel,
        destinationType: formValue.isFromTask ? null : formValue.destinationType.id,
        topic: TopicEnum.WORKFLOW_EVENT_TO_USER,
        frequencyEnum: formValue.frequencyEnum,
        notificationDestinations: formValue.isFromTask
          ? []
          : formValue.notificationDestinations.map((recipientId) => ({
              senderId: recipientId
            })),
        frequencies: frequencies,
        fileBase64: null,
        isFromTask: formValue.isFromTask
      };
      if (this.selectedFile && (broadcastChannel === BroadcastChannelEnum.BOTH || broadcastChannel === BroadcastChannelEnum.EMAIL)) {
        this.convertFileToBase64(this.selectedFile)
          .then((base64) => {
            notification.fileBase64 = base64;
            notification.fileName = notification.name + '.' + this.selectedFile?.name.split('.')[1];
          })
          .finally(() => {
            this.notificationService.createNotification(notification).subscribe({
              next: () => {
                this.notificationForm.reset();
                this.selectedDailyFrequency = null;
                this.activeOption = null;
                this.selectedFrequency = null;
                this.toastrService.onSuccess(
                  this.translatePipe.transform('notif.SUCCESS_TO_ADD_NOTIF'),
                  this.translatePipe.transform('menu.SUCCESS')
                );
                this.router.navigate(['../'], { relativeTo: this.route });
              },
              error: (error) => {
                if (error.error?.detail !== undefined) {
                  let errorDetails: string = error.error?.detail;
                  if (!errorDetails.includes(',')) {
                    this.toastrService.onError(
                      this.translatePipe.transform('notif.ERRORS.' + error.error?.detail),
                      this.translatePipe.transform('menu.ERROR')
                    );
                  } else {
                    let errors: string[] = error.error?.detail
                      .split(',')
                      .map((str) => str.trim())
                      .map((str) => 'notif.errors.' + str)
                      .filter(Boolean);
                    errors.forEach((error) =>
                      this.toastrService.onError(this.translatePipe.transform(error), this.translatePipe.transform('menu.ERROR'))
                    );
                  }
                } else {
                  this.toastrService.onError(this.translatePipe.transform('notif.ERRORS.FAILED_TO_ADD_NOTIF'), this.translatePipe.transform('menu.ERROR'));
                }
              }
            });
          });
      } else {
        this.notificationService.createNotification(notification).subscribe({
          next: () => {
            this.notificationForm.reset();
            this.selectedDailyFrequency = null;
            this.activeOption = null;
            this.selectedFrequency = null;
            this.toastrService.onSuccess(this.translatePipe.transform('notif.SUCCESS_TO_ADD_NOTIF'), this.translatePipe.transform('menu.SUCCESS'));
            this.router.navigate(['../'], { relativeTo: this.route });
          },
          error: (error) => {
            if (error.error?.detail !== undefined) {
              let errorDetails: string = error.error?.detail;
              if (!errorDetails.includes(',')) {
                this.toastrService.onError(
                  this.translatePipe.transform('notif.ERRORS.' + error.error?.detail),
                  this.translatePipe.transform('menu.ERROR')
                );
              } else {
                let errors: string[] = error.error?.detail
                  .split(',')
                  .map((str) => str.trim())
                  .map((str) => 'notif.errors.' + str)
                  .filter(Boolean);
                errors.forEach((error) =>
                  this.toastrService.onError(this.translatePipe.transform(error), this.translatePipe.transform('menu.ERROR'))
                );
              }
            } else {
              this.toastrService.onError(this.translatePipe.transform('notif.ERRORS.FAILED_TO_ADD_NOTIF'), this.translatePipe.transform('menu.ERROR'));
            }
          }
        });
      }
    }
  }

  private convertFileToBase64(file: File): Promise<string> {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();

      reader.readAsDataURL(file);

      reader.onload = () => {
        const base64String = (reader.result as string).split(',')[1];
        resolve(base64String);
      };

      reader.onerror = (error) => {
        reject(error);
      };
    });
  }
}
