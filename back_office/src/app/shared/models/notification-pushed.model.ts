import {EventNotification} from "./event-notification.model";
import {Task} from "./task.model";

export class NotificationPushed {
    id: string;
    name: string;
    description: string;
    message: string;
    notificationDate: string;
    recipientId: string;
    isSeen: boolean;
    createdDate: Date;
    eventNotification: EventNotification;
    taskDTO : Task
}
