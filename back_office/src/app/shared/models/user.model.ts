import { Roles } from '../enums/role.enum';
import { ItemRoleGroupRequestModel } from './ItemRoleGroupRequest.model';
import { Group } from './group.model';
import { Role } from './role.model';
import {UserType} from "../enums/user-type.enum";

export class User {
  id: string;
  keycloakId?: string;
  username?: string;
  firstName?: string;
  lastName?: string;
  phone?: string;
  profession?: string;
  status?: boolean;
  email?: string;
  emailActive?: string;
  setEmailActive?: boolean;
  password?: string;
  confirmPassword?: string;
  firstConnection?: boolean;
  address?: string;
  cin?: string;
  authority?: Roles;
  groups?: Group[];
  first_connection?: boolean;
  enabled?: boolean;
  items?: ItemRoleGroupRequestModel[];
  roles?: Role[];
  birthDate?: string;
  gender?: string;
  age?: string;
  phoneNumber?: string;
  eBarid?: string;
  isActive?: boolean;
  deleted?: boolean;
  profileCompleted?: boolean;
  nationality?: string;
  nationalId?: string
  country?: string;
  taxRegistration?: string;
  denomination?: string;
}
