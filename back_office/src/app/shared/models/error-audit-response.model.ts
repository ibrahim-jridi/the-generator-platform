import {HttpHeaders} from "@angular/common/http";
import {ErrorAuditModel} from "./error-audit.model";

export class ErrorAuditResponseModel {
  body: ErrorAuditModel[];
  headers: HttpHeaders;
  ok: boolean;
  status: number;
  statusText: string;
  type: number;
  url: string;
}
