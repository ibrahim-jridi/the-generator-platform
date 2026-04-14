import {ActivityType} from "./activity-type";

export class CompanyUserModel {
  id: string;
  keycloakId?: string;
  username?: string;
  phoneNumber?: string;
  email?: string;
  address?: string;
  creationDate?: string;
  taxRegistration?: string ;
  socialReason?: string ;
  legalStatus?: string ;
  userType?: string ;
  country?: string;
  nationality?: string;
  status?: string;
  isActive?: boolean;
  fileStatus?: string;
  filePatent?: string;
  registryStatus?: string;
  denomination?: string;
  activitiesType?: ActivityType [];
}
