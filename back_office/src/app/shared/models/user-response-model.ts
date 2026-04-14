import {User} from "./user.model";
import {HttpHeaders} from "@angular/common/http";

export class UserResponseModel {
  body: User[];
  headers: HttpHeaders;
  ok: boolean;
  status: number;
  statusText: string;
  type: number;
  url: string;
}
