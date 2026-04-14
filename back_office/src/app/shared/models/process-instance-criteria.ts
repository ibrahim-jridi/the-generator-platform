export class ProcessInstanceCriteria {
  id: string;
  state: string;
  processDefinitionId: string;
  startTime: Date;

  constructor(id?: string, state?: string, processDefinitionId?: string, startTime?: Date) {
    this.id = id;
    this.state = state;
    this.processDefinitionId = processDefinitionId;
    this.startTime = startTime;
  }
}
