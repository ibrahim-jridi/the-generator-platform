export class ProcessInstanceHistoryCriteria {
  id: string;
  startTime: Date;
  processDefinitionName: string;
  state: string;
  currentTaskName:string;
  endTime: Date;

  constructor(id?: string, startTime?: Date,processDefinitionName?: string,state?: string,  currentTaskName?:string , endTime?: Date) {
    this.id = id;
    this.state = state;
    this.processDefinitionName = processDefinitionName;
    this.startTime = startTime;
    this.currentTaskName=currentTaskName
    this.endTime = endTime;
  }

}
