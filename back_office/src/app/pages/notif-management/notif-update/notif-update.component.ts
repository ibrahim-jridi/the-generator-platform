import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NotificationService } from '../../../shared/services/notification.service';
import { Notification } from '../../../shared/models/notification.model';
import { AppToastNotificationService } from '../../../shared/services/appToastNotification.service';
import { TranslatePipe } from '@ngx-translate/core';
import { DestinationTypeEnum } from '../../../shared/enums/destination-type.enum';
import { PaginationArgs, PaginationSortArgs, PaginationSortOrderType } from '../../../shared/models/paginationArgs.model';
import { UserService } from '../../../shared/services/user.service';
import { FilterValuesModel } from '../../../shared/models/filter-values-model';
import { RoleCriteria } from '../../../shared/models/roleCriteria.model';
import { RoleService } from '../../../shared/services/role.service';
import { GroupsService } from '../../../shared/services/groups.service';
import { ConfigurationDayEnum } from '../../../shared/enums/configuration-day.enum';
import { Days } from '../../../shared/models/days.model';
import { Frequency } from '../../../shared/models/frequency.model';
import { FrequencyConfigEnum } from '../../../shared/enums/frequency-config.enum';
import { Subscription } from 'rxjs';
import { RegexConstants } from '../../../shared/utils/regex-constants';
import {EnumTranslationService} from "../../../shared/services/enum-translation.service";

@Component({
  selector: 'app-notif-update',
  templateUrl: './notif-update.component.html',
  styleUrl: './notif-update.component.scss'
})
export class NotifUpdateComponent implements OnInit {
  protected notifForm: FormGroup;
  protected dailyConfigForm: FormGroup;
  protected hourConfigForm: FormGroup;
  protected minuteConfigForm: FormGroup;
  private paginationArgs: PaginationArgs;
  private paginationSortArgs: PaginationSortArgs = new PaginationSortArgs('lastModifiedDate', PaginationSortOrderType.DESC);
  private notificationData: Notification;
  public state = window.history.state;
  private notifId: string = this.state?.notifId;
  protected notificationDestinationsOptions = [];
  protected isRealTime: boolean = false;
  protected isFrequencyDaily: boolean = false;
  protected isFrequencyHour: boolean = false;
  protected isFrequencyMinute: boolean = false;
  protected dailyConfigurationFrequency: boolean = false;
  protected weeklyConfigurationFrequency: boolean = false;
  protected customConfigurationFrequency: boolean = false;
  private pageIndex: number = 0;
  private pageSize: number;
  private filterValues: FilterValuesModel[];
  protected allowedExtensions: string[] = ['*'];
  protected isMailChecked: boolean = false;
  private criteria: RoleCriteria = new RoleCriteria();
  protected dailyFrequencyOptions: { id: string; label: any }[] = [];
  private subscriptions: Subscription = new Subscription();
  private selectedDailyFrequency: ConfigurationDayEnum;
  protected destinationTypeOptions: { label: any; id: string }[] = [];
  protected days: Days[] = [];
  private selectedFile: File = null;
  public fileError: string;
  public minDate: string;
  public minDateTime: string;
  constructor(
    private fb: FormBuilder,
    private notificationService: NotificationService,
    private router: Router,
    private toastrService: AppToastNotificationService,
    private translatePipe: TranslatePipe,
    private userService: UserService,
    private roleService: RoleService,
    private groupsService: GroupsService,
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
    this.notifId = this.state.notifId;
    this.initNotifForm();
    this.loadNotificationData();
    this.subscriptions.add(
        this.enumTranslationService.initTranslationList(ConfigurationDayEnum, 'notif.configuration_day')
        .subscribe((list) => {
          this.dailyFrequencyOptions = list;
        })
    );
    this.subscriptions.add(
        this.enumTranslationService.initTranslationList(DestinationTypeEnum, 'notif.destination_type')
        .subscribe((list) => {this.destinationTypeOptions = list})
    );
  }

  private initNotifForm(): void {
    this.notifForm = this.fb.group({
      name: ['', [Validators.required, Validators.pattern(RegexConstants.NOTIF_PATTERN_LABEL)]],
      description: ['', [Validators.required, Validators.maxLength(50)]],
      broadcastChannel: [[], [this.atLeastOneChannelValidator()]],
      destinationType: [''],
      frequencyEnum: [''],
      notificationDestinations: [''],
      frequencyConfig: [''],
      configurationDay: [''],
      isFromTask: ['']
    });
  }

  private atLeastOneChannelValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const selectedChannels = control.value || [];
      return selectedChannels.length > 0 ? null : { atLeastOneChannelRequired: true };
    };
  }

  protected onTaskRecipientToggle(isFromTask: boolean): void {
    this.notifForm.get('isFromTask')?.setValue(isFromTask);
    if (isFromTask === true) {
      this.notifForm.get('destinationType')?.clearValidators();
      this.notifForm.get('destinationType')?.reset();
      this.notifForm.get('destinationType')?.updateValueAndValidity();
      this.notifForm.get('notificationDestinations')?.clearValidators();
      this.notifForm.get('notificationDestinations')?.reset();
      this.notifForm.get('notificationDestinations')?.updateValueAndValidity();
    } else if (isFromTask === false) {
      this.notifForm.get('destinationType')?.setValidators([Validators.required]);
      this.notifForm.get('notificationDestinations')?.setValidators([Validators.required]);
      this.notifForm.get('destinationType')?.updateValueAndValidity();
      this.notifForm.get('notificationDestinations')?.updateValueAndValidity();
    }
  }

  protected switchFrequency(): void {
    this.isRealTime = !this.isRealTime;
    this.isFrequencyDaily = !this.isFrequencyDaily;
    this.changeFrequencyConfig();
  }

  private changeFrequencyConfig(): void {
    this.isFrequencyDaily ? this.notifForm.patchValue({ frequencyConfig: 'DAY' }) : this.notifForm.patchValue({ frequencyConfig: '' });
  }

  protected onFrequencyTypeChange(event: any): void {
    this.selectedDailyFrequency = event.id as ConfigurationDayEnum;

    switch (this.selectedDailyFrequency) {
      case 'DAILY':
        this.changeToDaily();
        break;
      case 'WEEKLY':
        this.changeToWeekly();
        break;
      case 'CUSTOM':
        this.changeToCustom();
        break;
      default:
        break;
    }
  }

  private loadNotificationData(): void {
    this.notificationService.getNotificationById(this.notifId).subscribe({
      next: (data: Notification) => {
        this.notificationData = data;
        if (this.notificationData.frequencyEnum === 'REAL_TIME') {
          this.isRealTime = true;
        }
        if (this.notificationData.broadcastChannel === 'EMAIL' || this.notificationData.broadcastChannel === 'BOTH') {
          this.isMailChecked = true;
        }
        this.patchNotificationData();
        this.populateNotificationDestinationsOptions(this.notificationData.destinationType);
      },
      error: () => {
        this.toastrService.onError(
          this.translatePipe.transform('notif.ERRORS.FAILED_TO_LOAD_NOTIFICATION'),
          this.translatePipe.transform('menu.ERROR')
        );
      }
    });
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
    this.minuteConfigForm.get('timing')?.setValue(input.value);
  }

  private loadUsers(): void {
    this.userService.getAllPhysicalUsers().subscribe({
      next: (response) => {
        this.notificationDestinationsOptions = response.map((user) => ({
          id: user.id,
          label: `${user.firstName} ${user.lastName}`
        }));
        this.setPreselectedDestinations();
      },
      error: (err) => {}
    });
  }

  private loadRoles(): void {
    this.paginationArgs = {
      sort: this.paginationSortArgs.sort,
      page: this.pageIndex,
      size: this.pageSize
    };
    this.roleService.getAllRoles(this.criteria, this.paginationArgs).subscribe({
      next: (data) => {
        this.notificationDestinationsOptions = data.map((role) => ({
          id: role.id,
          label: role.label
        }));
        this.setPreselectedDestinations();
      },
      error: (err) => {}
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
        this.notificationDestinationsOptions = response.body.map((group) => ({
          id: group.id,
          label: group.label
        }));
        this.setPreselectedDestinations();
      },
      error: (err) => {}
    });
  }

  protected populateNotificationDestinationsOptions(item): void {
    if (item === DestinationTypeEnum.USER) {
      this.loadUsers();
    } else if (item === DestinationTypeEnum.GROUP) {
      this.loadGroups();
    } else if (item === DestinationTypeEnum.ROLE) {
      this.loadRoles();
    }
  }

  private setPreselectedDestinations(): void {
    const selectedSenderIds = this.notificationData?.notificationDestinations?.map((destination) => destination.senderId).filter((id) => !!id);
    if (selectedSenderIds?.length) {
      const selectedOptions = this.notificationDestinationsOptions.filter((option) => selectedSenderIds.includes(option.id));
      const selectedIdsToSet = selectedOptions.map((option) => option.id);
      this.notifForm.get('notificationDestinations')?.setValue(selectedIdsToSet);
    }
  }

  private getBroadcastChannels(channel: string): string[] {
    switch (channel) {
      case 'EMAIL':
        return ['EMAIL'];
      case 'NOTIFICATION_PUSH':
        return ['NOTIFICATION_PUSH'];
      case 'BOTH':
        return ['EMAIL', 'NOTIFICATION_PUSH'];
      default:
        return [];
    }
  }

  private patchNotificationData(): void {
    if (this.notificationData.fileName) {
      this.notifForm.addControl('fileName', this.fb.control(''));
      this.notifForm.patchValue({
        fileName: this.notificationData.fileName
      });
    }
    this.notifForm.patchValue({
      name: this.notificationData.name,
      description: this.notificationData.description,
      broadcastChannel: this.getBroadcastChannels(this.notificationData.broadcastChannel),
      destinationType: this.notificationData.destinationType,
      frequencyEnum: this.notificationData.frequencyEnum,
      isFromTask: this.notificationData.isFromTask
    });
    if (this.notificationData.frequencies.length != 0) {
      if (this.notificationData.frequencies.length == 1 && this.notificationData.frequencies[0].configurationDay !== 'CUSTOM') {
        let frequency: Frequency = this.notificationData.frequencies[0];
        switch (frequency.frequencyConfig) {
          case 'DAY':
            this.patchNotificationDaily(frequency);
            break;

          case 'HOUR':
            this.patchNotificationHourly(frequency);
            break;

          case 'MINUTE':
            this.patchNotificationMinutely(frequency);
            break;
        }
      } else {
        this.initCustomConfigForm();
        this.patchCustomConfigForm(this.notificationData.frequencies);
      }
    }
  }

  private loadDays(): void {
    this.notificationService.getDays().subscribe({
      next: (data) => {
        this.days = data;
        this.initializeDayCheckboxes();
      },
      error: (err) => {}
    });
  }

  protected onCheckboxChange(channel: string, isChecked: boolean): void {
    if (channel === 'EMAIL' && isChecked == true) {
      this.isMailChecked = true;
      this.notifForm.addControl('fileName', this.fb.control(''));
    } else if (channel === 'EMAIL' && isChecked == false) {
      this.isMailChecked = false;
      this.notifForm.removeControl('fileName');
    }
    const currentChannels = this.notifForm.get('broadcastChannel')?.value || [];

    if (isChecked) {
      if (!currentChannels.includes(channel)) {
        currentChannels.push(channel);
      }
    } else {
      const index = currentChannels.indexOf(channel);
      if (index > -1) {
        currentChannels.splice(index, 1);
      }
    }

    this.notifForm.get('broadcastChannel')?.setValue(currentChannels);
    this.notifForm.get('broadcastChannel')?.markAsTouched();
  }

  private convertChannelsToString(channels: string[]): string {
    if (channels.includes('EMAIL') && channels.includes('NOTIFICATION_PUSH')) {
      return 'BOTH';
    } else if (channels.includes('EMAIL')) {
      return 'EMAIL';
    } else if (channels.includes('NOTIFICATION_PUSH')) {
      return 'NOTIFICATION_PUSH';
    } else {
      return '';
    }
  }

  protected updateNotification(): void {
    const formValues = { ...this.notifForm.value };
    if (formValues.broadcastChannel === 'NOTIFICATION_PUSH') {
      delete formValues.fileName;
    }

    let fileBase64: string = '';
    formValues.broadcastChannel = this.convertChannelsToString(formValues.broadcastChannel);
    let updatedNotification: Notification = {
      id: this.notificationData.id,
      topic: this.notificationData.topic,
      ...formValues,

      destinationType: formValues.isFromTask === true ? null : this.notifForm.value.destinationType.id,
      notificationDestinations:
        formValues.isFromTask === true
          ? []
          : this.notifForm.value.notificationDestinations.map((senderId: string) => {
              const existingDestination = this.notificationData.notificationDestinations.find((destination) => destination.senderId === senderId);
              if (existingDestination) {
                return {
                  id: existingDestination.id,
                  senderId: existingDestination.senderId
                };
              }
              return { senderId };
            })
    };

    updatedNotification = this.prepareNotificationData(updatedNotification);
    if (this.selectedFile != null) {
      this.convertFileToBase64(this.selectedFile)
        .then((base64) => (fileBase64 = base64))
        .finally(() => {
          updatedNotification.fileBase64 = fileBase64;
          updatedNotification.fileName = updatedNotification.name + '.' + this.selectedFile?.name.split('.')[1];
          this.updateNotif(updatedNotification);
        });
    } else {
      this.updateNotif(updatedNotification);
    }
  }

  private updateNotif(updatedNotification: any): void {
    this.notificationService.updateNotification(this.notifId, updatedNotification).subscribe({
      next: (data) => {
        this.toastrService.onSuccess(
          this.translatePipe.transform('notif.SUCCESS_TO_UPDATE_NOTIFICATION'),
          this.translatePipe.transform('menu.SUCCESS')
        );
        this.router.navigate(['pages/notif-management']);
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
              .map((str) => 'notif.ERRORS.' + str)
              .filter(Boolean);
            errors.forEach((error) => this.toastrService.onError(this.translatePipe.transform(error), this.translatePipe.transform('menu.ERROR')));
          }
        } else {
          this.toastrService.onError(this.translatePipe.transform('notif.ERRORS.FAILED_TO_UPDATE_NOTIFICATION'), this.translatePipe.transform('menu.ERROR'));
        }
      }
    });
  }

  private prepareNotificationData(updatedNotification: Notification): Notification {
    if (this.isRealTime) {
      updatedNotification.frequencies = null;
      return updatedNotification;
    } else {
      updatedNotification.frequencies = [];
      if (this.isFrequencyDaily) {
        if (this.customConfigurationFrequency) {
          let freqArray: FormArray = this.dailyConfigForm.get('customRows') as FormArray;
          freqArray.controls.forEach((freq) => {
            let frequency: Frequency = new Frequency();
            frequency.configurationDay = ConfigurationDayEnum.CUSTOM;
            frequency.frequencyConfig = FrequencyConfigEnum.DAY;
            const [date, time] = freq.get('dateTime').value.split('T');
            frequency.timing = time;
            frequency.startDate = date;
            frequency.isRepeat = false;
            updatedNotification.frequencies.push(frequency);
          });
        } else if (this.weeklyConfigurationFrequency) {
          let frequency: Frequency = this.dailyConfigForm.getRawValue();
          frequency.configurationDay = ConfigurationDayEnum.WEEKLY;
          frequency.frequencyConfig = FrequencyConfigEnum.DAY;
          frequency.days = this.days.filter((_, i) => this.dailyConfigForm.get('days').value[i]);
          updatedNotification.frequencies.push(frequency);
        } else {
          let frequency: Frequency = this.dailyConfigForm.getRawValue();
          frequency.configurationDay = ConfigurationDayEnum.DAILY;
          frequency.frequencyConfig = FrequencyConfigEnum.DAY;
          updatedNotification.frequencies.push(frequency);
        }
      } else if (this.isFrequencyHour) {
        let frequency: Frequency = this.hourConfigForm.getRawValue();
        frequency.frequencyConfig = FrequencyConfigEnum.HOUR;
        frequency.configurationDay = null;
        updatedNotification.frequencies.push(frequency);
      } else {
        let frequency: Frequency = this.minuteConfigForm.getRawValue();
        frequency.frequencyConfig = FrequencyConfigEnum.MINUTE;
        frequency.configurationDay = null;
        frequency.timing = this.minuteConfigForm.getRawValue().timing.split(':')[1];
        updatedNotification.frequencies.push(frequency);
      }
      return updatedNotification;
    }
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
    this.notifForm.get('file')?.setValue(null);
    if (this.notificationData) {
      this.notificationData.fileName = null;
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

  public back(): void {
    this.router.navigate(['/pages/notif-management']);
  }

  private initDailyConfigForm(): void {
    this.dailyConfigForm = null;
    this.hourConfigForm = null;
    this.minuteConfigForm = null;
    this.dailyConfigForm = this.fb.group({
      startDate: [null, Validators.required],
      endDate: [null],
      timing: ['', Validators.required],
      isRepeat: [true]
    });
  }

  private initWeeklyConfigForm(): void {
    this.dailyConfigForm = null;
    this.hourConfigForm = null;
    this.minuteConfigForm = null;
    this.dailyConfigForm = this.fb.group({
      endDate: [null],
      timing: ['', Validators.required],
      isRepeat: [false],
      days: this.fb.array(this.days.map(() => this.fb.control(false)))
    });
    this.dailyConfigForm.get('days')?.setValidators(this.atLeastOneDayValidator());
    this.dailyConfigForm.get('days')?.updateValueAndValidity();
  }

  private atLeastOneDayValidator(): ValidatorFn {
    return (formArray: AbstractControl): ValidationErrors | null => {
      const selected = (formArray as FormArray).controls.some((control) => control.value === true);
      return selected ? null : { atLeastOneDayRequired: true };
    };
  }

  private initHourFormGroup(): void {
    this.dailyConfigForm = null;
    this.minuteConfigForm = null;
    this.hourConfigForm = null;
    this.hourConfigForm = this.fb.group({
      endDate: [null],
      timing: ['', Validators.required],
      isRepeat: [false]
    });
  }

  private initCustomConfigForm(): void {
    this.dailyConfigForm = null;
    this.minuteConfigForm = null;
    this.hourConfigForm = null;
    this.dailyConfigForm = this.fb.group({
      customRows: this.fb.array([this.createRow()])
    });
  }

  private initMinuteFormGroup(): void {
    this.dailyConfigForm = null;
    this.minuteConfigForm = null;
    this.hourConfigForm = null;
    this.minuteConfigForm = this.fb.group({
      endDate: [null],
      timing: ['', Validators.required],
      isRepeat: [false]
    });
  }

  protected createRow(): FormGroup {
    return this.fb.group({
      dateTime: ['', Validators.required]
    });
  }

  get customRows(): FormArray {
    return this.dailyConfigForm.get('customRows') as FormArray;
  }

  protected addRow(): void {
    this.customRows.push(this.createRow());
  }

  protected removeRow(index: number): void {
    this.customRows.removeAt(index);
  }

  private changeToDaily(): void {
    this.dailyConfigurationFrequency = true;
    this.weeklyConfigurationFrequency = false;
    this.customConfigurationFrequency = false;
    this.initDailyConfigForm();
  }

  private changeToWeekly(): void {
    this.loadDays();
    this.dailyConfigurationFrequency = false;
    this.weeklyConfigurationFrequency = true;
    this.customConfigurationFrequency = false;
    this.initWeeklyConfigForm();
  }

  private changeToCustom(): void {
    this.dailyConfigurationFrequency = false;
    this.weeklyConfigurationFrequency = false;
    this.customConfigurationFrequency = true;
    this.initCustomConfigForm();
  }

  protected changeFrequencyType(event: string): void {
    switch (event) {
      case 'DAY':
        this.onChangeFrequencyTypeToDay();
        break;
      case 'HOUR':
        this.onChangeFrequencyTypeToHour();
        break;
      case 'MINUTE':
        this.onChangeFrequencyTypeToMinute();
        break;
    }
  }

  private onChangeFrequencyTypeToDay(): void {
    this.isFrequencyDaily = true;
    this.isFrequencyHour = false;
    this.isFrequencyMinute = false;
    this.initDailyConfigForm();
  }

  private onChangeFrequencyTypeToHour(): void {
    this.isFrequencyDaily = false;
    this.isFrequencyHour = true;
    this.isFrequencyMinute = false;
    this.initHourFormGroup();
  }

  private onChangeFrequencyTypeToMinute(): void {
    this.isFrequencyDaily = false;
    this.isFrequencyHour = false;
    this.isFrequencyMinute = true;
    this.initMinuteFormGroup();
  }

  private patchNotificationMinutely(frequency: Frequency): void {
    this.notifForm.patchValue({ frequencyConfig: 'MINUTE' });
    this.isFrequencyDaily = false;
    this.isFrequencyHour = false;
    this.isFrequencyMinute = true;
    frequency.timing = '00:' + frequency.timing;
    this.initMinuteFormGroup();
    this.minuteConfigForm.patchValue({
      isRepeat: frequency.isRepeat,
      timing: frequency.timing,
      endDate: frequency.endDate
    });
  }

  private patchNotificationDaily(frequency: Frequency): void {
    this.notifForm.patchValue({ frequencyConfig: 'DAY' });
    this.isFrequencyDaily = true;
    if (frequency.configurationDay === 'WEEKLY') {
      this.notifForm.patchValue({ configurationDay: 'WEEKLY' });
      this.loadDays();
      this.weeklyConfigurationFrequency = true;
      this.dailyConfigurationFrequency = false;
      this.initWeeklyConfigForm();
      this.dailyConfigForm.patchValue({
        isRepeat: frequency.isRepeat,
        timing: frequency.timing,
        endDate: frequency.endDate,
        days: this.days
      });
    } else {
      this.notifForm.patchValue({ configurationDay: 'DAILY' });
      this.dailyConfigurationFrequency = true;
      this.weeklyConfigurationFrequency = false;
      this.initDailyConfigForm();
      this.dailyConfigForm.patchValue({
        startDate: frequency.startDate,
        isRepeat: frequency.isRepeat,
        timing: frequency.timing,
        endDate: frequency.endDate
      });
    }
    this.isFrequencyHour = false;
    this.isFrequencyMinute = false;
  }

  private initializeDayCheckboxes(): void {
    const daysFormArray = this.dailyConfigForm.get('days') as FormArray;

    daysFormArray.clear();

    const selectedDays = this.notificationData?.frequencies.find((frequency) => frequency.configurationDay === ConfigurationDayEnum.WEEKLY)?.days || [];

    this.days.forEach((day) => {
      const isSelected = selectedDays.some((selectedDay: any) => selectedDay.id === day.id);
      daysFormArray.push(this.fb.control(isSelected));
    });
  }

  private patchNotificationHourly(frequency: Frequency): void {
    this.notifForm.patchValue({ frequencyConfig: 'HOUR' });
    this.isFrequencyDaily = false;
    this.isFrequencyHour = true;
    this.isFrequencyMinute = false;
    this.initHourFormGroup();
    this.hourConfigForm.patchValue({
      isRepeat: frequency.isRepeat,
      timing: frequency.timing,
      endDate: frequency.endDate
    });
  }

  private patchCustomConfigForm(frequencies: Frequency[]): void {
    this.notifForm.patchValue({ frequencyConfig: 'DAY' });
    this.notifForm.patchValue({ configurationDay: 'CUSTOM' });
    this.isFrequencyDaily = true;
    this.customConfigurationFrequency = true;
    this.isFrequencyHour = false;
    this.isFrequencyMinute = false;
    const customRows = this.dailyConfigForm.get('customRows') as FormArray;
    customRows.clear();
    frequencies.forEach((frequency) => {
      const customRow = this.createRow();
      customRow.patchValue({
        dateTime: frequency.startDate + ' ' + frequency.timing
      });
      customRows.push(customRow);
    });
  }
}
