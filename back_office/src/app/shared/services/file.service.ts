import { Injectable } from '@angular/core';
import { GlobalService } from './global.service';
import { Observable} from 'rxjs';
import { RequestType } from '../enums/requestType';
import {HttpClient, HttpParams} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class FileService {

  public FILE_URI = this.globalService.BASE_REPORTS_URL + this.globalService.API_V1_URL + 'files';

  constructor(private globalService: GlobalService, private http: HttpClient) {
  }

  public getImageReportUrl(name: string): Observable<Blob> {
    let params = new HttpParams().append('name', name);
    return this.globalService.call(
      RequestType.GET,
      `${this.FILE_URI}/logo-report-url`,
      { params, responseType: 'blob' as 'json' }
    );
  }


  public uploadLogoReport(file:File,name: string): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('name', name);

    return this.globalService.call(
      RequestType.POST, `${this.FILE_URI}/upload-logo-report`, formData);
  }

  public uploadTemplateReport(file: File, type: string): Observable<string> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('type', type);

    return this.globalService.call(
        RequestType.POST, `${this.FILE_URI}/upload-report`, formData, { responseType: 'text' });
  }

  public getTemplateReportUrl(name: string): Observable<any> {
    let params = new HttpParams().append('name', name);
    return this.globalService.call(
        RequestType.GET,`${this.FILE_URI}/report-url`,{params}, { responseType: 'text' });
  }

  public updateTemplateReport(file: File, id: string, type: string): Observable<string> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('id',id);
    formData.append('type',type);

    return this.globalService.call(
        RequestType.POST, `${this.FILE_URI}/update-report`, formData, { responseType: 'text' });
  }

  public deleteReport(id: string): Observable<any> {
    return this.globalService.call(
        RequestType.DELETE, this.FILE_URI + '/' + id
    )
  }

}


