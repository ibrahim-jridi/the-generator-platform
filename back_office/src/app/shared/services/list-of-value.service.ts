import { HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable, of} from 'rxjs';
import { GlobalService } from './global.service';
import { RequestType } from '../enums/requestType';

@Injectable({
  providedIn: 'root'
})
export class ListOfValueService {
  private formDataSubject = new BehaviorSubject<any>(null);

  constructor( private globalService: GlobalService) { }

  private baseUrl = this.globalService.BASE_REPORTS_URL + this.globalService.API_V1_URL + 'reports';

  sendFormData(data: any) {
    this.formDataSubject.next(data);
  }

  getListOfValues(): Observable<any[]> {
    return this.globalService.call(RequestType.GET, `${this.baseUrl}list-of-values`);
  }

  getListOfValuesById(id: string): Observable<any> {
    return this.globalService.call(RequestType.GET, `${this.baseUrl}list-of-values/${id}`);
  }

  getChoicesOfListOfValueId(id: string): Observable<any> {
    return this.globalService.call(RequestType.GET, `${this.baseUrl}choices/listOfValue/${id}`);
  }

  saveListOfValue(listOfValue: Object): Observable<Object> {
    console.log(listOfValue);
    return this.globalService.call(
      RequestType.POST,
      `${this.baseUrl}list-of-values/send-ListOfValues`,
      listOfValue,
      { headers: new HttpHeaders().set('Content-Type', 'application/json') }
    );
  }

  updateListOfValue(listOfValue: Object, id: string): Observable<Object> {
    console.log(listOfValue);
    return this.globalService.call(
      RequestType.PUT,
      `${this.baseUrl}list-of-values/${id}`,
      listOfValue,
      { headers: new HttpHeaders().set('Content-Type', 'application/json') }
    );
  }
}
