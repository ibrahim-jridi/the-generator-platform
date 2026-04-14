import {HttpHeaders} from "@angular/common/http";
import {ActivityAuditModel} from "./activity-audit.model";

export class ActivityAuditResponseModel {
  body: ActivityAuditModel[];
  headers: HttpHeaders;
  ok: boolean;
  status: number;
  statusText: string;
  type: number;
  url: string;
}
