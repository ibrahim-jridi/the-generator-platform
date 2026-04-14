import {Injectable} from '@angular/core';
import {GlobalService} from './global.service';
import {Observable} from 'rxjs';
import {RequestType} from '../enums/requestType';
import {ConfigurationReportModel} from "../models/configuration-report.model";

@Injectable({
  providedIn: 'root',
})
export class ConfigurationReportService {
  public CONFIG_URI =  this.globalService.BASE_REPORTS_URL + this.globalService.API_V1_URL +'configuration-reports';
  constructor(private globalService: GlobalService) {
  }

  public getConfigReport(): Observable<ConfigurationReportModel[]> {
    return this.globalService.call(RequestType.GET, this.CONFIG_URI);
  }

  public getConfigReportWithLogo(): Observable<ConfigurationReportModel> {
    return this.globalService.call(RequestType.GET, this.CONFIG_URI+'/config-report-with-logo');
  }
  public saveConfigReport(config: ConfigurationReportModel): Observable<ConfigurationReportModel> {
    return this.globalService.call(RequestType.POST, this.CONFIG_URI, config);
  }

  public updateConfigReport( id: number,config: ConfigurationReportModel): Observable<ConfigurationReportModel> {
    return this.globalService.call( RequestType.PUT,this.CONFIG_URI + '/' + id, config);
  }

  public demoReport(config: ConfigurationReportModel): Observable<any> {
    return this.globalService.call( RequestType.POST,this.CONFIG_URI + '/generate-demo-report', config, { responseType: 'blob' });
  }
}
