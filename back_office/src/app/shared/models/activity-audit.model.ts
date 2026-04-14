export class ActivityAuditModel {
  id: string;
  submissionDate: Date;
  action: string;
  entity: string;
  details: string;
  userFullName: string
}
