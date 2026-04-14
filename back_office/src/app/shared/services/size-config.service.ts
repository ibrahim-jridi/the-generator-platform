import {Injectable} from '@angular/core';
import {GlobalService} from "./global.service";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {SizeConfig} from "../models/sizeConfig.model";
import {RequestType} from "../enums/requestType";

@Injectable({
  providedIn: 'root'
})
export class SizeConfigService {
  constructor(private globalService: GlobalService) {
  }

  private BASE_FILE_URL = this.globalService.BASE_File_URL + this.globalService.API_V1_URL + '/size-configs';


  getSizeConfigs(): Observable<SizeConfig[]> {
    return this.globalService.call(RequestType.GET, this.BASE_FILE_URL)

  }


  public getSizeConfigById(id: string): Observable<SizeConfig> {
    return this.globalService.call(
      RequestType.GET, this.BASE_FILE_URL + '/' + id);
  }

  updateSizeConfig(id: string, sizeConfig: SizeConfig): Observable<SizeConfig> {
    return this.globalService.call(
      RequestType.PUT,
      `${this.BASE_FILE_URL}/${id}`, sizeConfig);

  }
}
