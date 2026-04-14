import { Injectable } from '@angular/core';
import {GlobalService} from "./global.service";
import {Observable} from "rxjs";
import {HttpParams, HttpResponse} from "@angular/common/http";
import {RequestType} from "../enums/requestType";
import {PaginationArgs} from "../models/paginationArgs.model";
import {ReportTemplate} from "../models/report-template";

@Injectable({
  providedIn: 'root'
})
export class ReportTemplateService {

  constructor(private globalService: GlobalService) {
  }

  public Report_URI = this.globalService.BASE_REPORTS_URL + this.globalService.API_V1_URL + 'report-templates';

  public getAllReport(pageable: PaginationArgs): Observable<HttpResponse<any>> {
    const params = new HttpParams()
      .append('sort', pageable.sort)
      .append('page', pageable.page)
      .append('size', pageable.size);
    return this.globalService.call(
      RequestType.GET, this.Report_URI, {
        params: params,
        observe: 'response'
      }
    );
  }

  public getReportById(id: string): Observable<ReportTemplate> {
    return this.globalService.call(
      RequestType.GET, this.Report_URI + '/' + id);
  }

  public deleteReport(id: string): Observable<any> {
    return this.globalService.call(
      RequestType.DELETE, this.Report_URI + '/' + id
    )
  }

}
