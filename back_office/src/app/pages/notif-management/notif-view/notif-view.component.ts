import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Notification } from '../../../shared/models/notification.model';
import { NotificationService } from '../../../shared/services/notification.service';
import { Subscription } from 'rxjs';
import { ReportService } from '../../../shared/services/report.service';
import { LangChangeEvent, TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-notif-view',
  templateUrl: './notif-view.component.html',
  styleUrl: './notif-view.component.scss'
})
export class NotifViewComponent implements OnInit {
  protected notifForm: FormGroup;
  private notification: Notification;
  private state = window.history.state;
  private notifId: string = this.state?.notifId;
  protected fileName: string;
  protected combinedDatesAndTimes: any;
  public langChangeSubscription: Subscription;

  constructor(
    private fb: FormBuilder,
    private notificationService: NotificationService,
    private reportService: ReportService,
    private translate: TranslateService
  ) {}

  public ngOnInit(): void {
    this.notifId = this.state.notifId;
    this.initializeForm();
    this.loadNotif();
    this.notifForm.disable();
    this.subscribeToLanguageChanges();
  }

  private initializeForm(): void {
    this.notifForm = this.fb.group({
      name: [''],
      destinationType: [''],
      notificationDestinations: [''],
      broadcastChannel: [''],
      frequencyEnum: [''],
      customDate: [''],
      startDate: [null],
      endDate: [null],
      timing: [null],
      repeatDailyNotification: [null],
      days: this.fb.array([]),
      description: [''],
      frequencyConfig: [''],
      configurationDay: [''],
      frequencies: this.fb.array([]),
      fileName: [''],
      isFromTask: ['']
    });
  }

  public get daysControls() {
    return (this.notifForm.get('days') as FormArray).controls;
  }

  private loadNotif(): void {
    this.notificationService.getNotificationById(this.notifId).subscribe({
      next: (data: Notification) => {
        this.notification = data;
        this.populateForm(data);
      },
      error: (err) => {
        console.error('Error fetching notification:', err);
      }
    });
  }

  private populateForm(data: Notification): void {
    if (data.fileName) {
      this.fileName = data.fileName;
    }

    const firstFrequency = data.frequencies?.[0];
    const firstFrequencyConfig = firstFrequency?.frequencyConfig ?? '';
    const configurationDay = firstFrequency?.configurationDay ?? '';
    const combinedDatesAndTimes = data.frequencies?.map((frequency) => `${frequency.startDate ?? ''} ${frequency.timing ?? ''}`) ?? [];
    const startDate = data.frequencies?.[0]?.startDate ?? '';
    const endDate = data.frequencies?.[0]?.endDate ?? '';
    const timing = data.frequencies?.[0]?.timing ?? '';
    const isRepeat = data.frequencies?.[0]?.isRepeat ?? '';
    const daysArray = this.notifForm.get('days') as FormArray;
    daysArray.clear();

    const days = data.frequencies?.[0]?.days?.map((day) => day.name) ?? [];
    days.forEach((day) => {
      daysArray.push(new FormControl(day));
    });

    this.notifForm.patchValue({
      name: data.name ?? '',
      description: data.description ?? '',
      broadcastChannel: data.broadcastChannel ?? '',
      destinationType: this.getTranslatedDestinationType(data.destinationType) ?? '',
      frequencyEnum: data.frequencyEnum ?? '',
      fileName: data.fileName ?? '',
      notificationDestinations: data.extractedLabel ?? '',
      frequencyConfig: firstFrequencyConfig,
      configurationDay: configurationDay,
      customDate: combinedDatesAndTimes,
      startDate: startDate,
      endDate: endDate,
      timing: timing,
      repeatDailyNotification: isRepeat,
      days: days,
      isFromTask: data.isFromTask
    });

    this.combinedDatesAndTimes = combinedDatesAndTimes;
  }

  private getTranslatedDestinationType(destinationType: string): string {
    if (!destinationType) return '';

    switch (destinationType) {
      case 'GROUP':
        return this.translate.instant('notif.destination_type.GROUP');
      case 'USER':
        return this.translate.instant('notif.destination_type.USER');
      case 'ROLE':
        return this.translate.instant('notif.destination_type.ROLE');
      default:
        return destinationType;
    }
  }

  private subscribeToLanguageChanges(): void {
    this.langChangeSubscription = this.translate.onLangChange.subscribe((event: LangChangeEvent) => {
      const currentDestinationType = this.notification?.destinationType;
      if (currentDestinationType) {
        const translatedDestinationType = this.getTranslatedDestinationType(currentDestinationType);
        this.notifForm.patchValue({ destinationType: translatedDestinationType }, { emitEvent: false });
      }
    });
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
}
