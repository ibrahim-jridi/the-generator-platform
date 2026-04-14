import {StatusWaitingList} from "../enums/StatusWaitingList.enum";
import {Governorate} from "../enums/governorate.enum";
import {Category} from "../enums/category.enum";

export interface WaitingListDTO {
  id: string;
  idUser: string;
  rank: number;
  category: Category;
  governorate: Governorate;
  delegation?: string;
  municipality?: string;
  status: StatusWaitingList;
  dateRenewal?: string;
}
