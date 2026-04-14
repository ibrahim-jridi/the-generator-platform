import { Form } from './form.model';

export class Deploy {
  id?: string;
  name?: string;
  deploymentTime?: Date;
  source?: string;
  tenantId?: string;
  version?: number;
  form?: Form;
}
