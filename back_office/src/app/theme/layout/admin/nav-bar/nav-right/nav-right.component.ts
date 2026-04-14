import { Component, OnInit } from '@angular/core';
import { NgbDropdownConfig } from '@ng-bootstrap/ng-bootstrap';
import { LoginService } from '../../../../../shared/services/login.service';
import { TokenUtilsService } from '../../../../../shared/services/token-utils.service';
import { TranslateService } from '@ngx-translate/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { NotificationSocketService } from '../../../../../shared/services/notification-socket.service';
import { NotificationTopic } from 'src/app/shared/enums/notification-topic';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { NotificationPushed } from '../../../../../shared/models/notification-pushed.model';
import { NotificataionPushedService } from '../../../../../shared/services/notification-pushed.service';
import { formatDate } from '@angular/common';
import { UserService } from 'src/app/shared/services/user.service';
import { CompanyUserModel } from 'src/app/shared/models/company-user-model';

@Component({
  selector: 'app-nav-right',
  templateUrl: './nav-right.component.html',
  styleUrls: ['./nav-right.component.scss'],
  providers: [NgbDropdownConfig]
})
export class NavRightComponent implements OnInit {
  firstName: string;
  userId: string;
  username: string;
  currentLanguage: string;
  public language: string;
  public notifications$: Observable<NotificationPushed[]>;
  public expiredNotifications: NotificationPushed[];
  public firstNamePart: string = '';
  public lastNamePart: string = '';
  public showBadge: boolean = false;
  protected notifications: NotificationPushed[] = [];
  private allNotificationsSubject = new BehaviorSubject<any[]>([]);
  private allNotifications: any[] = [];
  public notificationsCount: number = 0;
  public userDenomination: string;
  public companyUser: any;
  protected user: any;
  public morale: boolean = false;
  constructor(
    private loginService: LoginService,
    private tokenUtilsService: TokenUtilsService,
    private translateService: TranslateService,
    private notificataionService: NotificationSocketService,
    private route: ActivatedRoute,
    private router: Router,
    private notificataionPushedService: NotificataionPushedService,
    private userService: UserService
  ) {
    this.firstName = this.tokenUtilsService.getName();
    this.username = this.tokenUtilsService.getUsername();
    this.userId = this.tokenUtilsService.getUserId();
    this.getCompanyUserDenomination( this.userId );
  }

  ngOnInit() {
    // listen to socket notifications for current user
    this.listenToNotificationSocketByRole();
    // get expired notification for current user
    this.getAllNotificationsByCurrentUserId(this.userId);
    // Retrieve stored language from localStorage
    this.currentLanguage = localStorage.getItem('language') || 'fr';
    this.language = this.currentLanguage.charAt(0).toUpperCase() + this.currentLanguage.slice(1).toLowerCase();
    this.translateService.setDefaultLang(this.currentLanguage);
    this.translateService.use(this.currentLanguage);
    this.displayUsername();
  }
  public getCompanyUserDenomination(id): void {
    this.userService.getUserById(id).subscribe(
        (res) => {
          this.user = res;
          if (this.user?.userType == 'COMPANY') {
            this.morale = true;
            this.companyUser = res;
            this.userDenomination = this.companyUser.denomination;
          } else {
            this.morale = false;
          }
        },
        (error) => {
          // error handling
        }
    );
  }

  logout() {
    this.loginService.logout();
  }

  public displayUsername(): void {
    const parts = this.firstName.split(' ');
    if (parts.length === 2) {
      this.firstNamePart = parts[0];
      this.lastNamePart = parts[1];
    } else {
      this.firstNamePart = this.firstName;
      this.lastNamePart = '';
    }
  }

  protected listenToNotificationSocketByRole(role?: string) {
    // TODO LISTEN TO NOTIFICATIONS BY USER ROLE OR GROUP AND corresponding TOPIC
    this.connectToNotificationTopic(NotificationTopic.WORKFLOW_EVENT_TO_USER);
  }
  protected connectToNotificationTopic(topic?: string): void {
    this.notificataionService._connectToWebsocketNotificationAndTopic(this.userId, topic);

    this.notificataionService.notificationsObservable.subscribe((data: any[]) => {
      if (data && data.length > 0) {
        const formattedData = data.map((notification) => {
          if (!notification.notificationDate) {
            notification.notificationDate = formatDate(new Date(), 'dd/MM/yyyy HH:mm:ss', 'en-US');
          }
          return notification;
        });

        this.allNotifications = [...formattedData, ...this.allNotifications];
        this.allNotificationsSubject.next(this.allNotifications);
        this.notifications = [...this.notifications, ...data];
        this.notificationsCount = this.notifications.length;
        this.showBadge = this.notificationsCount > 0;
      }
    });

    this.notifications$ = this.allNotificationsSubject.asObservable();
  }

  protected onChangeIsSeen(notification: any): void {
    notification.isSeen = true;
    this.allNotificationsSubject.next(this.allNotifications);
    this.notificataionPushedService.updateNotificationPushed(notification.id, notification).subscribe((value) => {
      if (value) {
        notification = value;
      }
    });
  }

  protected getAllNotificationsByCurrentUserId(userId: string): void {
    this.notificataionPushedService.getNotificationPushed(userId).subscribe((result) => {
      this.expiredNotifications =
        result
          ?.map((notification) => ({
            ...notification,
            notificationDate: formatDate(notification.notificationDate, 'dd/MM/yyyy HH:mm:ss', 'en-US')
          }))
          ?.sort((a, b) => {
            return new Date(b.notificationDate).getTime() - new Date(a.notificationDate).getTime();
          }) || [];
    });
  }

  switchLanguage(lang: string, isRtl: boolean = false) {
    this.translateService.setDefaultLang(lang);
    localStorage.setItem('language', lang);
    this.translateService.use(lang);
    this.setRtlLayout(isRtl);
  }

  changeRtlLayout(flag: boolean): void {
    if (flag) {
      document.querySelector('body')?.classList.add('bs-theme-rtl');
    } else {
      document.querySelector('body')?.classList.remove('bs-theme-rtl');
    }
  }

  setRtlLayout(isRtl: boolean): void {
    this.changeRtlLayout(isRtl);
  }

  public handleOnViewUser(): void {
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      state: {
        userId: this.userId
      }
    };
    let isExternalUser = this.tokenUtilsService.isUserExternal();
    let isPhysicalUser= this.tokenUtilsService.isPP();
    let isInternalUser= this.tokenUtilsService.isPM();
    if (isExternalUser) {
      this.router.navigate(['user-management/external-user-management/view-user-external', this.userId], navigationExtras);
    } else this.router.navigate(['user-management/internal-user-management/view-user', this.userId], navigationExtras);
    if(isPhysicalUser){
    this.router.navigate(['physical-user-profile', this.userId], navigationExtras);
    }
    if(isInternalUser){
      this.router.navigate(['pm-user-profile', this.userId], navigationExtras);
    }
  }

  protected onLanguageChanged(newLanguage: string): void {
    this.language = newLanguage.charAt(0).toUpperCase() + newLanguage.slice(1).toLowerCase();
  }

  protected onDropdownToggle(): void {
    this.showBadge = false;
    this.notifications = [];
  }
}
