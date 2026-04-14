CREATE TABLE  IF NOT EXISTS bs_event_notification (
                                                   id UUID PRIMARY KEY,
                                                   notification_id UUID,
                                                   task_id VARCHAR(255),
                                                   task_name VARCHAR(255),
                                                   status VARCHAR(255) NOT NULL,
                                                   created_by UUID NOT NULL,
                                                   created_date TIMESTAMP NOT NULL,
                                                   last_modified_by UUID,
                                                   last_modified_date TIMESTAMP NOT NULL,
                                                   version INTEGER NOT NULL,
                                                   deleted BOOLEAN NOT NULL,
                                                   CONSTRAINT fk_event_notification__notification FOREIGN KEY (notification_id) REFERENCES bs_notification (id)
);

CREATE TABLE IF NOT EXISTS bs_notification_pushed (
            id UUID PRIMARY KEY,
            name VARCHAR(255) NOT NULL,
            description TEXT,
            message VARCHAR(255) NOT NULL,
            notification_date TIMESTAMP NOT NULL,
            recipient_id UUID NOT NULL,
            is_seen BOOLEAN NOT NULL,
            event_notification_id UUID,
            created_by UUID NOT NULL,
            created_date TIMESTAMP NOT NULL,
            last_modified_by UUID,
            last_modified_date TIMESTAMP NOT NULL,
            version INTEGER NOT NULL,
            deleted BOOLEAN NOT NULL,
   CONSTRAINT fk_event_notification_pushed__notification FOREIGN KEY (event_notification_id) REFERENCES bs_event_notification (id)
);
