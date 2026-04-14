CREATE TABLE jhi_date_time_wrapper
(
    id               BIGINT NOT NULL,
    instant          TIMESTAMP WITHOUT TIME ZONE,
    local_date_time  TIMESTAMP WITHOUT TIME ZONE,
    offset_date_time TIMESTAMP WITHOUT TIME ZONE,
    zoned_date_time  TIMESTAMP WITHOUT TIME ZONE,
    local_time       time WITHOUT TIME ZONE,
    offset_time      time WITHOUT TIME ZONE,
    local_date       date,
    CONSTRAINT pk_jhi_date_time_wrapper PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS bs_notification (
    id UUID NOT NULL,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    is_seen VARCHAR(50) NOT NULL,
    broadcast_channel VARCHAR(50) NOT NULL,
    destination_type VARCHAR(50),
    topic VARCHAR(50) NOT NULL,
    frequency_enum VARCHAR(50) NOT NULL,
    created_by UUID NOT NULL,
    created_date TIMESTAMP NOT NULL,
    last_modified_by UUID,
    last_modified_date TIMESTAMP NOT NULL,
    version INTEGER NOT NULL,
    deleted BOOLEAN NOT NULL,
    CONSTRAINT notification_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS  bs_notification_destination (
    id UUID NOT NULL,
    notification_id UUID NOT NULL,
    sender_id UUID NOT NULL,
    created_by UUID NOT NULL,
    created_date TIMESTAMP NOT NULL,
    last_modified_by UUID,
    last_modified_date TIMESTAMP NOT NULL,
    version INTEGER NOT NULL,
    deleted BOOLEAN NOT NULL,
    CONSTRAINT notification_destination_pkey PRIMARY KEY (id),
    CONSTRAINT fk_notification_destination__notification FOREIGN KEY (notification_id) REFERENCES bs_notification (id)
);

CREATE TABLE IF NOT EXISTS  bs_frequency (
    id UUID NOT NULL,
    notification_id UUID NOT NULL,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    is_repeat BOOLEAN,
    timing VARCHAR(255),
    configuration_day VARCHAR(15),
    frequency_config VARCHAR(15),
    created_by UUID NOT NULL,
    created_date TIMESTAMP NOT NULL,
    last_modified_by UUID,
    last_modified_date TIMESTAMP NOT NULL,
    version INTEGER NOT NULL,
    deleted BOOLEAN NOT NULL,
    CONSTRAINT frequency_pkey PRIMARY KEY (id),
    CONSTRAINT fk_frequency__notification FOREIGN KEY (notification_id) REFERENCES bs_notification (id)
);

CREATE TABLE IF NOT EXISTS  bs_days (
    id UUID NOT NULL,
    name VARCHAR(255),
    created_by UUID NOT NULL,
    created_date TIMESTAMP NOT NULL,
    last_modified_by UUID,
    last_modified_date TIMESTAMP NOT NULL,
    version INTEGER NOT NULL,
    deleted BOOLEAN NOT NULL,
    CONSTRAINT days_pkey PRIMARY KEY (id)
);
