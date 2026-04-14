import { S } from "@fullcalendar/core/internal-common";
import { ProcessInstance } from "./processInstance.model";

export class ProcessInstanceHistory {
  id: string;
  state: string;
  processDefinitionName: string;
  startTime: Date;
  currentTaskName:string;
  endTime:Date;

}
