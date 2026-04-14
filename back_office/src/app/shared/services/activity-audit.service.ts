import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {RequestType} from "../enums/requestType";
import {GlobalService} from "./global.service";
import {HttpParams} from "@angular/common/http";
import {PaginationArgs} from "../models/paginationArgs.model";
import {ActivityAuditModel} from "../models/activity-audit.model";
import {FilterValuesModel} from "../models/filter-values-model";
import {ActivityAuditResponseModel} from "../models/activity-audit-response.model";

@Injectable({
  providedIn: 'root'
})
export class ActivityAuditService {
  constructor(private globalService: GlobalService) {
  }

  public ACTIVITY_AUDIT_URI = this.globalService.BASE_AUDIT_URL + this.globalService.API_V1_URL + 'activity-audits';

  public getActivityAudits(criteria: FilterValuesModel[], pageable: PaginationArgs): Observable<ActivityAuditResponseModel> {
    let params = new HttpParams()
      .set('page', pageable.page)
      .set('size', pageable.size)
      .set('sort', pageable.sort);

    if (criteria != undefined && criteria.length > 0) {
      criteria.forEach((element) => {
        params = params.append(`${element.columnName}.contains`, element.filterValue);
      });
    }

    const options = {
      params: params,
      observe: 'response'
    };

    return this.globalService.call(RequestType.GET, `${this.ACTIVITY_AUDIT_URI}`, options);
  }

  public getActivityAuditById(id: string): Observable<ActivityAuditModel> {
    return this.globalService.call(
        RequestType.GET, this.ACTIVITY_AUDIT_URI + '/' + id
    )
  }

}
