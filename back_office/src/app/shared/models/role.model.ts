import { Authority } from "./authority.model";

export class Role {
  id : string;
  isActive: boolean;
  label : string;
  description : string;
  createdBy : string;
  createdDate: Date;
  lastModifiedBy: string;
  lastModifiedDate: Date;
  hasUsers: boolean;
  deleted: boolean;
  authorities : Authority[];

  constructor(id?: string, isActive?: boolean, label?: string, description?: string, createdBy?: string, createdDate?: Date, lastModifiedBy?: string, lastModifiedDate?: Date, hasUsers?: boolean, deleted?: boolean, authorities?: Authority[]) {
    this.id = id;
    this.isActive = isActive;
    this.label = label;
    this.description = description;
    this.createdBy = createdBy;
    this.createdDate = createdDate;
    this.lastModifiedBy = lastModifiedBy;
    this.lastModifiedDate = lastModifiedDate;
    this.hasUsers = hasUsers;
    this.deleted = deleted;
    this.authorities = authorities;
  }
}
