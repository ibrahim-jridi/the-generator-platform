export class Group {
  id : string;
  label: string;
  description: string;
  parent?: Group;
  numberOfInternalUsers?: number
  children?: Group[];
  createdBy?: string | null;
  createdDate?: Date;
  lastModifiedBy?: string | null;
  lastModifiedDate?: Date;
  version?: number;
  deleted?: boolean;
  isActive?: boolean;
}
