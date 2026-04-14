import {Authority} from "./authority.model";
import {FrequenceTypeEnum} from "../enums/frequence-type.enum";

export class CostServiceCriteria {
  id?: string;
  serviceName?: string;
  categorieService?: string;
  frequenceType?: FrequenceTypeEnum;
  isFixed?: boolean;
  prix_ht?: number;
  prix_ttc?: number;
  tva?: number;
  isActive?: boolean;
  createdDate: Date;
  isRenewable?: boolean;
}
