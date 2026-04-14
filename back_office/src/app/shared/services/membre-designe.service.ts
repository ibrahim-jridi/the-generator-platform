import {Injectable} from '@angular/core';
import {GlobalService} from "./global.service";
import {Observable} from "rxjs";
import {RequestType} from "../enums/requestType";
import {FilterValuesModel} from "../models/filter-values-model";
import {PaginationArgs} from "../models/paginationArgs.model";
import {HttpParams} from "@angular/common/http";
import {DesignationResponseModel} from "../models/designation-response-model";
import {ApplicationsRequest} from "../models/ApplicationsRequest-model";

@Injectable({
  providedIn: 'root'
})
export class MembreDesigneService {

  constructor(private globalService: GlobalService) {
  }

  private MEMBERS_URI = this.globalService.BASE_USER_MANAGEMENT_URL + this.globalService.API_V1_URL + 'designations';

  public getListByPmUserId(
      criteria: FilterValuesModel[],
      pmUserId: string,
      paginationArgs: PaginationArgs
  ): Observable<DesignationResponseModel> {

    let params = new HttpParams()
    .append('sort', paginationArgs.sort)
    .append('page', paginationArgs.page)
    .append('size', paginationArgs.size);

    if (criteria != undefined && criteria.length > 0) {
      criteria.forEach((element) => {
        params = params.append(`${element.columnName}.contains`, element.filterValue.toString());
      });
    }

    const options = {
      params: params,
      observe: 'response'
    };
    return this.globalService.call(RequestType.GET, this.MEMBERS_URI + '/pm/' + pmUserId,options);
  }

  getApplicationNames(): Observable<ApplicationsRequest> {
    return this.globalService.call(RequestType.GET, this.MEMBERS_URI + '/external-applications');}

  public getListByDesignatedUserId(
    criteria: FilterValuesModel[],
    pmUserId: string,
    paginationArgs: PaginationArgs
  ): Observable<DesignationResponseModel> {

    let params = new HttpParams()
      .append('sort', paginationArgs.sort)
      .append('page', paginationArgs.page)
      .append('size', paginationArgs.size);

    if (criteria != undefined && criteria.length > 0) {
      criteria.forEach((element) => {
        params = params.append(`${element.columnName}.contains`, element.filterValue.toString());
      });
    }

    const options = {
      params: params,
      observe: 'response'
    };
    return this.globalService.call(RequestType.GET, this.MEMBERS_URI + '/designated-user/' + pmUserId,options);
  }


  public getEctdRoleByKeycloakId(
      keycloakId: string
  ): Observable<any> {
    const options = {
      observe: 'response'
    };

    return this.globalService.call(
        RequestType.GET,
        `${this.MEMBERS_URI}/ectd-role/${keycloakId}`,
        options
    );
  }

  public getApplicationIdsByKeycloakId(
      keycloakId: string
  ): Observable<any> {
    const options = {
      observe: 'response'
    };

    return this.globalService.call(
        RequestType.GET,
        `${this.MEMBERS_URI}/application-ids/${keycloakId}`,
        options
    );
  }


  public updateKeycloakUserDesignations(assignRoleApplication: any): Observable<string> {
    return this.globalService.call(RequestType.POST, this.MEMBERS_URI + '/ectd-role-applications', assignRoleApplication, {responseType: 'text'});
  }

  public deleteDesignation(id: string): Observable<void> {
    return this.globalService.call(RequestType.DELETE, this.MEMBERS_URI + '/' + id);
  }
}
