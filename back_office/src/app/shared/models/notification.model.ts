import {BroadcastChannelEnum} from "../enums/broadcast-channel.enum";
import {DestinationTypeEnum} from "../enums/destination-type.enum";
import {TopicEnum} from "../enums/topic.enum";
import {FrequencyTypeEnum} from "../enums/frequencyType.enum";
import {NotificationDestination} from "./notification-destination.model";
import {Frequency} from "./frequency.model";

export  class Notification {
  id?: string;
  name?: string;
  description?: string;
  broadcastChannel?: BroadcastChannelEnum;
  destinationType?: DestinationTypeEnum;
  topic?: TopicEnum;
  frequencyEnum?: FrequencyTypeEnum;
  notificationDestinations?: NotificationDestination[];
  frequencies?: Frequency[];
  fileName?: string;
  createdDate?:string;
  fileBase64?:string;
  extractedLabel?:string;
  isFromTask?:boolean;

}
