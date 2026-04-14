import { Injectable } from '@angular/core';
import { GlobalService } from './global.service';
import { Observable } from 'rxjs';
import { Authority } from '../models/authority.model';
import { RequestType } from '../enums/requestType';
import { Role } from '../models/role.model';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class AuthorityService {

  constructor(private globalService: GlobalService) { }
  private authorityUri = this.globalService.BASE_USER_MANAGEMENT_URL + this.globalService.API_V1_URL + 'authorities'
  public getAllAuthorities():Observable<Authority[]>{
    return this.globalService.call(RequestType.GET, this.authorityUri+'/findAll')
  }
  public getAuthoritiesNotInRole(id: string): Observable<Authority[]> {
    return this.globalService.call(
      RequestType.GET, this.authorityUri + '/getAuthorities/' + id);
  }
}
