export class Authority {
  id : string;
  label: string;
  checked?: boolean;
  description: string;
  isActive: boolean;
  createdBy : string;
  createdDate: Date;
  lastModifiedBy: string;
  lastModifiedDate: Date;
  version: number;
  deleted: boolean;


  constructor(id?: string, label?: string, checked?: boolean, description?: string, isActive?: boolean, createdBy?: string, createdDate?: Date, lastModifiedBy?: string, lastModifiedDate?: Date, version?: number, deleted?: boolean) {
    this.id = id;
    this.label = label;
    this.checked = checked;
    this.description = description;
    this.isActive = isActive;
    this.createdBy = createdBy;
    this.createdDate = createdDate;
    this.lastModifiedBy = lastModifiedBy;
    this.lastModifiedDate = lastModifiedDate;
    this.version = version;
    this.deleted = deleted;
  }
}
