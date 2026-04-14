import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "../models/user.model";
import {RequestType} from "../enums/requestType";
import {GlobalService} from "./global.service";
import {EhoweyaConfig} from "../models/ehoweyaConfig";

@Injectable({
  providedIn: 'root'
})
export class EhouweyaService {
  constructor(private http: HttpClient, private globalService: GlobalService) {}

  getUserInfo(code: string, codeVerifier: string): Observable<any> {
    return this.http.get<any>(`${this.globalService.BASE_URL}open-api/get-ehoweya-user`,  {
      params: { code, codeVerifier }
    });
  }
    getEhoweyaConfig():Observable<any> {
      return this.http.get<any>(`${this.globalService.BASE_URL}open-api/ehouweya-config`);
    }
}
