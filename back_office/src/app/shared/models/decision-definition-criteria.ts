export class DecisionDefinitionCriteria {
  id: string;
  name: string;
  version: number;
  key: string;

  constructor(id?: string, name?: string, version?: number, key?: string) {
    this.id = id;
    this.name = name;
    this.version = version;
    this.key = key;
  }
}
