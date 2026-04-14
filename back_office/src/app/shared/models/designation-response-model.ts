import {User} from "./user.model";
import {HttpHeaders} from "@angular/common/http";
import {DesignationsList} from "./designation";

export class DesignationResponseModel {
  body: DesignationsList[];
  headers: HttpHeaders;
  ok: boolean;
  status: number;
  statusText: string;
  type: number;
  url: string;
}
