import { Injectable } from '@angular/core';
import { GlobalService } from './global.service';
import { BehaviorSubject, Observable, Subscription } from 'rxjs';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { RequestType } from '../enums/requestType';

@Injectable({
  providedIn: 'root'
})
export class NotificationSocketService {

  // TODO to change with the gateway-url
  private NOTIFICATION_MANAGEMENT_URL: string = this.globalService.BASE_NOTIFICATION_URL;
  private notificationWebsocketEndPoint: string = this.NOTIFICATION_MANAGEMENT_URL + 'websocket';
  private stompClient: any;

  private websocketConnected: boolean = false;
  notificationSubscription: Subscription;

  notificationsSubject = new BehaviorSubject<any[]>([]);
  notificationsObservable = this.notificationsSubject.asObservable();

  constructor(private globalService: GlobalService) { }

  public _connectToWebsocketNotificationAndTopic(userId: string, topic?: string): void {
    const ws = new SockJS(this.notificationWebsocketEndPoint);
    this.stompClient = Stomp.over(ws);
    const _this = this;
    this.stompClient.connect({}, (frame) => {
      setTimeout(() => {
        _this.websocketConnected = true;
        this.notificationSubscription = _this.stompClient.subscribe("/user/" + userId + "/" + topic, (data: any) => {
          const notification = JSON.parse(data.body);
          this.notificationsSubject.next([notification]);
        });
      }, 500);
    });
  }

  public _disconnect(): void {
    if (this.stompClient != undefined) { 
      this.stompClient.disconnect(() => {
        this.websocketConnected = false;
        this.notificationSubscription.unsubscribe();
      });
    }
  }

  public getAllNotificationsByUserId(destinataireId: string): Observable<Notification[]> {
    return this.globalService.call(
      RequestType.GET, this.NOTIFICATION_MANAGEMENT_URL  + 'api/notifications/' + destinataireId);
  }
}
