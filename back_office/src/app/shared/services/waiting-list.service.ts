import { Injectable } from '@angular/core';
import { GlobalService } from './global.service';
import { BehaviorSubject, firstValueFrom, Observable, tap } from 'rxjs';
import { User } from '../models/user.model';
import { RequestType } from '../enums/requestType';
import {WaitingListDTO} from "../models/waitingList.model";

@Injectable({
  providedIn: 'root'
})
export class WaitingListService {

  public WAITING_LIST_URI = this.globalService.BASE_USER_MANAGEMENT_URL + this.globalService.API_V1_URL + 'waiting-list';

  private userUnsubscribedSubject = new BehaviorSubject<boolean>(false);
  userUnsubscribed = this.userUnsubscribedSubject.asObservable();
  constructor(private globalService: GlobalService) {
  }

  isUserUnsubscribed(): Observable<boolean> {
    return this.globalService.call(RequestType.GET, this.WAITING_LIST_URI + '/is-unsubscribed').pipe(
      tap((status: boolean) => this.userUnsubscribedSubject.next(status))
    );
  }

  getUserRank(): Observable<number> {
    return this.globalService.call(RequestType.GET, this.WAITING_LIST_URI +'/rank');
  }

  refreshUserStatus(): void {
    this.isUserUnsubscribed().subscribe();
  }
  getWaitingListByUserId(userId: string): Observable<WaitingListDTO[]> {
    return this.globalService.call(RequestType.GET,`${this.WAITING_LIST_URI}/${userId}`);

  }

}
