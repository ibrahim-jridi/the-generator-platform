export class ProcessDefinition {
  id: string;
  key: string;
  name: string;
  version: number;
  deploymentId: string;
  resourceName: string;
  suspensionState: boolean;
  startableInTasklist: string;
  actions: any[];
}
