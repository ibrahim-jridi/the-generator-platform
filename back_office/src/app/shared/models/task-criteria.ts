export class TaskCriteria {
  id: string;
  startTime: Date;
  name: string;
  endTime: Date;
  businessKey: string;
  owner?: string;

  constructor(id?: string, startTime?: Date, name?: string, endTime?: Date, businessKey?: string,owner?:string) {
    this.id = id;
    this.startTime = startTime;
    this.name = name;
    this.endTime = endTime;
    this.businessKey = businessKey;
    this.owner = owner;
  }
}
