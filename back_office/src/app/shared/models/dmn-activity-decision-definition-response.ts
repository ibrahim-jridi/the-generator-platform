import {HttpHeaders} from "@angular/common/http";
import {DmnActivityDecisionDefinition} from "./dmn-activity-decision-definition.model";

export class DmnActivityDecisionDefinitionResponse {
  body: DmnActivityDecisionDefinition[];
  headers: HttpHeaders;
  ok: boolean;
  status: number;
  statusText: string;
  type: number;
  url: string;
}
