import {HttpHeaders} from "@angular/common/http";
import {Form} from "./form.model";

export class FormResponseModel {
  body: Form[];
  headers: HttpHeaders;
  ok: boolean;
  status: number;
  statusText: string;
  type: number;
  url: string;
}
