import { Form } from './form.model';

export class HistoricTaskInstance {
  id: string;
  processDefinitionKey: string;
  processDefinitionId: string;
  rootProcessInstanceId: string;
  processInstanceId: string;
  executionId: string;
  caseDefinitionKey: string;
  caseDefinitionId: string;
  caseInstanceId: string;
  caseExecutionId: string;
  activityInstanceId: string;
  name: string;
  description: string;
  deleteReason: string;
  owner: string;
  assignee: string;
  startTime: string;
  endTime: string;
  durationInMillis: number;
  taskDefinitionKey: string;
  priority: number;
  dueDate: Date;
  parentTaskId: string;
  followUpDate: Date;
  tenantId: string;
  removalTime: Date;
  isCollapsed: boolean;
  form?: Form;
  submission: any;
  submissionDTO: any;
  userDTO: any;
}
