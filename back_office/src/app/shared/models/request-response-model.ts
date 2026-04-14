import {DesignationsList} from "./designation";
import {HttpHeaders} from "@angular/common/http";
import {ApplicationsRequest} from "./ApplicationsRequest-model";

export class RequestResponseModel {
  body: ApplicationsRequest[];
  headers: HttpHeaders;
  ok: boolean;
  status: number;
  statusText: string;
  type: number;
  url: string;
}
