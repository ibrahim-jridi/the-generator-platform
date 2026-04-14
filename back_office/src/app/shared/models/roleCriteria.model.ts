import { Authority } from './authority.model';

export class RoleCriteria {
  id : string;
  label : string;
  description : string;
  createdDate : Date;
  authorities : Authority[];

}
