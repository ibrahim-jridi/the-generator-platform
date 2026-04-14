import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {RequestType} from "../enums/requestType";
import {GlobalService} from "./global.service";
import {ErrorAuditModel} from '../models/error-audit.model';
import {PaginationArgs} from "../models/paginationArgs.model";
import {HttpParams} from "@angular/common/http";
import {FilterValuesModel} from "../models/filter-values-model";
import {ErrorAuditResponseModel} from "../models/error-audit-response.model";

@Injectable({
  providedIn: 'root'
})
export class ErrorAuditService {
  constructor(private globalService: GlobalService) {
  }

  public ERROR_AUDIT_URI = this.globalService.BASE_AUDIT_URL + this.globalService.API_V1_URL + 'error-audits';

  public getAllErrorAudits(criteria: FilterValuesModel[]
    , pageable: PaginationArgs): Observable<ErrorAuditResponseModel> {
    let params = new HttpParams()
      .append('sort', pageable.sort)
      .append('page', pageable.page)
      .append('size', pageable.size);

    if (criteria != undefined && criteria.length > 0) {
      criteria.forEach((element) => {
        params = params.append(`${element.columnName}.contains`, element.filterValue);
      });
    }

    const options = {
      params: params,
      observe: 'response'
    };

    return this.globalService.call(
      RequestType.GET, this.ERROR_AUDIT_URI, options
    )
  }

  public getErrorAuditById(id: string): Observable<ErrorAuditModel> {
    return this.globalService.call(
      RequestType.GET, this.ERROR_AUDIT_URI + '/' + id
    )
  }
}
