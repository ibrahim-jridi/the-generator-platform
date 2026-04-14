import {HttpHeaders} from "@angular/common/http";
import {Connector} from "./connector.model";

export class ConnectorResponse {
  body: Connector[];
  headers: HttpHeaders;
  ok: boolean;
  status: number;
  statusText: string;
  type: number;
  url: string;
}
