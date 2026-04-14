import {HttpHeaders} from "@angular/common/http";
import {Group} from "./group.model";

export class GroupResponse {
  body: Group[];
  headers: HttpHeaders;
  ok: boolean;
  status: number;
  statusText: string;
  type: number;
  url: string;
}
