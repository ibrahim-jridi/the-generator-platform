export class ProcessInstance{
    id: string;
    processInstanceId: string;
    businessKey: string;
    processDefinitionId: string;
    startTime: Date;          
    endTime: Date;
    duration: number;
    startUserId: string;
    startActivityId: string;
    endActivityId: string;
    superProcessInstanceId: string;
    deleteReason: string;
    tenantId: string;
    state: string;
    constructor(init?: Partial<ProcessInstance>) {
        if (init) {
          Object.assign(this, init);
        }
      }
}
