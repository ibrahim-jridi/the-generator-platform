import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {RequestType} from "../enums/requestType";
import {GlobalService} from "./global.service";
import {HttpParams} from "@angular/common/http";
import {PaginationArgs} from "../models/paginationArgs.model";
import {ActivityAuditModel} from "../models/activity-audit.model";
import {FilterValuesModel} from "../models/filter-values-model";
import {ActivityAuditResponseModel} from "../models/activity-audit-response.model";
import {NotificationPushed} from "../models/notification-pushed.model";

@Injectable({
  providedIn: 'root'
})
export class NotificataionPushedService {
  constructor(private globalService: GlobalService) {
  }

  public NOTIFICATION_PUSHED_URI = this.globalService.BASE_NOTIFICATION_URL + this.globalService.API_V1_URL + 'notification-pushed';

  public getNotificationPushed(recipientId: string): Observable<NotificationPushed[]> {
    return this.globalService.call(
          RequestType.GET, this.NOTIFICATION_PUSHED_URI + '/by-recipient/' + recipientId
      );
  }

  public getNotificationPushedById(id: string): Observable<ActivityAuditModel> {
    return this.globalService.call(
        RequestType.GET, this.NOTIFICATION_PUSHED_URI + '/' + id
    )
  }

  public updateNotificationPushed(id: string, notification: NotificationPushed): Observable<NotificationPushed> {
    return this.globalService.call(RequestType.PUT,`${this.NOTIFICATION_PUSHED_URI}/${id}`, notification);
  }

}
