import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';
import {RequestType} from "../enums/requestType";


@Injectable()
export class GlobalService {

  public BASE_URL: string = environment.BACK_END_POINT;
  public BASE_OPEN_API_URL: string = this.BASE_URL + '/open-api';
  public API_URL : string = 'api/';
  public API_V1_URL : string = 'api/v1/';
  public GATEWAY_URL :string = this.BASE_URL ;
  public BASE_USER_MANAGEMENT_URL: string = this.BASE_URL + 'user-management/';
  public BASE_WORKFLOW_URL: string = this.GATEWAY_URL+"workflow-management/";
  public BASE_REPORTS_URL: string = this.GATEWAY_URL+"report-management/";
  public BASE_FORMS_URL: string = this.GATEWAY_URL+"form-management/";
  public BASE_CONNECTOR_URL: string = this.GATEWAY_URL + "connector-management/";
  public BASE_NOTIFICATION_URL: string = this.GATEWAY_URL+"notification-management/";
  public BASE_File_URL: string = this.GATEWAY_URL+"file-management/";
  public BASE_AUDIT_URL: string = this.GATEWAY_URL+"audit-management/";
  public BASE_PAYMENT_URL: string = this.GATEWAY_URL+"payment-management/";

  public url = {
    base: this.BASE_URL,
    user_profile: this.BASE_USER_MANAGEMENT_URL,
    openApi: this.BASE_OPEN_API_URL + '/open-api',
    appVersion: this.BASE_OPEN_API_URL + '/appVersion',
    task:this.BASE_WORKFLOW_URL,
    report:this.BASE_REPORTS_URL,
    form:this.BASE_FORMS_URL,
    connector: this.BASE_CONNECTOR_URL,
    file: this.BASE_File_URL,
    audit: this.BASE_AUDIT_URL
  };


  constructor(private httpClient: HttpClient) {
  }

  public call(
    method: RequestType,
    url: string,
    ...args: any[]
  ): Observable<any> {
    switch (method) {
      case RequestType.GET: {
        return this.httpClient.get<any>(url, args[0]);
      }
      case RequestType.POST: {
        return this.httpClient.post<any>(url, args[0], args[1]);
      }
      case RequestType.PUT: {
        return this.httpClient.put<any>(url, args[0], args[1]);
      }
      case RequestType.DELETE: {
        args[0] = {
          responseType: 'text',
        };
        return this.httpClient.delete<any>(url, args[0]);
      }
      default: {
        return null;
      }
    }
  }
}
