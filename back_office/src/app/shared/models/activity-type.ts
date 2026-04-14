export class ActivityType {
  id?: string;
  code?: string;
  name?: string;
  userId?: string;
  createdBy?: string | null;
  createdDate?: Date;
  lastModifiedBy?: string | null;
  lastModifiedDate?: Date;
  version?: number;
  deleted?: boolean;
  isActive?: boolean;
}
