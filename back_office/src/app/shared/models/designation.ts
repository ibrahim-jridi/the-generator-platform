import {User} from "./user.model";
import {Role} from "./role.model";

export class DesignationsList{
  id: string;
  designatedUser: User;
  role: Role;
  createdDate: string;
  pmUser?: User;
  laboratoryUser?: User;

}
