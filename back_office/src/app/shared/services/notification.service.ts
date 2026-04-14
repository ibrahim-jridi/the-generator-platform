import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GlobalService } from './global.service';
import { Notification } from '../models/notification.model';
import { Days } from '../models/days.model';
import { RequestType } from '../enums/requestType';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private readonly BASE_NOTIF_URL = this.globalService.BASE_NOTIFICATION_URL + this.globalService.API_V1_URL + 'notifications';
  private readonly DAYS_URL = this.globalService.BASE_NOTIFICATION_URL + this.globalService.API_V1_URL + 'days';

  constructor(private readonly globalService: GlobalService) {}

  public createNotification(notification: Notification): Observable<Notification> {
    return this.globalService.call(RequestType.POST, this.BASE_NOTIF_URL, notification);
  }

  public getNotification(): Observable<Notification[]> {
    return this.globalService.call(RequestType.GET, this.BASE_NOTIF_URL);
  }
  public getNotificationAsPromise(): Promise<Notification[]> {
    return this.getNotification().toPromise();
  }

  public getDays(): Observable<Days[]> {
    return this.globalService.call(RequestType.GET, this.DAYS_URL);
  }

  public getNotificationById(id: string): Observable<Notification> {
    return this.globalService.call(RequestType.GET, this.BASE_NOTIF_URL + '/' + id);
  }

  public updateNotification(id: string, notification: Notification): Observable<Notification> {
    return this.globalService.call(RequestType.PUT, this.BASE_NOTIF_URL + '/' + id, notification);
  }

  public deleteNotification(id: string): Observable<void> {
    return this.globalService.call(RequestType.DELETE, this.BASE_NOTIF_URL + '/' + id);
  }
}
