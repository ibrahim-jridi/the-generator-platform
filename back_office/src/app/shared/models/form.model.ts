import {Task} from './task.model';

export class Form {
  id?: string;
  formId ?: string;
  display?: string;
  settings?: any;
  components?: any[];
  label?: string;
  description?: string;
  fields?: string;
  createdDate?: Date | null;
  updatedDate?: Date | null;
  createdBy?: string | null;
  version?: number | null;
  isDeleted?: boolean | null;
  task?: Task;
}
