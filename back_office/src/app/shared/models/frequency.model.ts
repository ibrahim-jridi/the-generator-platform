import {ConfigurationDayEnum} from "../enums/configuration-day.enum";
import {FrequencyConfigEnum} from "../enums/frequency-config.enum";
import {Days} from "./days.model";
import {Notification} from "./notification.model";

export class Frequency{
  id?: string;
  startDate?: Date;
  endDate?: Date;
  isRepeat?: boolean;
  timing?: string;
  configurationDay?: ConfigurationDayEnum;
  frequencyConfig?: FrequencyConfigEnum;
  notification?: Notification;
  days?: Days[];

}
