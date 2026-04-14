import { Form } from './form.model';
import { User } from './user.model';

export class Task {
  id?: string;
  revision?: number;
  executionId?: string;
  processInstanceId?: string;
  processDefinitionId?: string;
  parentTaskId?: string;
  description?: string;
  taskDefinitionKey?: string;
  owner?: string;
  assignee?: string;
  delegationState?: string;
  priority?: number;
  createTime?: string;
  dueZonedDateTime?: string;
  category?: string;
  suspensionState?: number;
  tenantId?: string;
  formKey?: string;
  status?: string;
  startTime?: string;
  endTime?: string;
  createOrStartTime?: string;
  name?: string;
  display?: string;
  settings?: string;
  components?: string;
  userDTO?: User;
  form?: Form;
  submission?: any;
  isCollapsed?: boolean;
  disabled?: boolean;
  assigneeUserName?: string;
  variables?: any;
  businessKey?: string;
}
