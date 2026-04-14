import {FrequenceTypeEnum} from "../enums/frequence-type.enum";

export class CostService {
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
  date_start?: Date;
  date_end?: Date;
  isRenewable?: boolean;
  renewalFrequenceType?: FrequenceTypeEnum;
  renewal_prix_ht?: number;
  renewal_tva?: number;
  renewal_prix_ttc?: number;
}
