CREATE TABLE IF NOT EXISTS public.bs_notification (
    "id" UUID NOT NULL,
    "sender_id" VARCHAR(50) NOT NULL,
    "sender_fullname" VARCHAR(50),
    "recipient_id" VARCHAR(50) NOT NULL,
    "role_id" VARCHAR(50),
    "topic" VARCHAR(50),
    "type" VARCHAR(50),
    "subject" VARCHAR(250),
    "body" VARCHAR(250),
    "created_by" UUID NOT NULL,
    "created_date" TIMESTAMP NOT NULL,
    "last_modified_by" UUID NOT NULL,
    "last_modified_date" TIMESTAMP NOT NULL,
    "version" INTEGER NOT NULL,
    "deleted" BOOLEAN NOT NULL,
    CONSTRAINT bs_notification_pkey PRIMARY KEY ("id")
    );

CREATE TABLE IF NOT EXISTS bs_notification_config (
    "id" UUID NOT NULL PRIMARY KEY,
    "label" VARCHAR(50) NOT NULL,
    "description" TEXT NOT NULL,
    "type" VARCHAR(25),
    "trigger" VARCHAR(150),
    "target" VARCHAR(150),
    "broadcastchannel" VARCHAR(255) NOT NULL,
    "created_by" UUID NOT NULL,
    "created_date" TIMESTAMP NOT NULL,
    "last_modified_by" UUID NOT NULL,
    "last_modified_date" TIMESTAMP NOT NULL,
    "version" INTEGER NOT NULL,
    "deleted" BOOLEAN NOT NULL
    );

CREATE TABLE IF NOT EXISTS bs_notification_group (
    "group_id" UUID NOT NULL PRIMARY KEY,
    "ref_id" UUID,
    "notification_config_id" UUID,
    "created_by" UUID NOT NULL,
    "created_date" TIMESTAMP NOT NULL,
    "last_modified_by" UUID NOT NULL,
    "last_modified_date" TIMESTAMP NOT NULL,
    "version" INTEGER NOT NULL,
    "deleted" BOOLEAN NOT NULL,
    CONSTRAINT fk_bs_notification_group_notification_config_id FOREIGN KEY ("notification_config_id")
    REFERENCES bs_notification_config ("id")
    );

CREATE TABLE IF NOT EXISTS bs_notification_role (
    "role_id" UUID NOT NULL PRIMARY KEY,
    "ref_id" UUID,
    "notification_config_id" UUID,
    "created_by" UUID NOT NULL,
    "created_date" TIMESTAMP NOT NULL,
    "last_modified_by" UUID NOT NULL,
    "last_modified_date" TIMESTAMP NOT NULL,
    "version" INTEGER NOT NULL,
    "deleted" BOOLEAN NOT NULL,
    CONSTRAINT fk_bs_notification_role_notification_config_id FOREIGN KEY ("notification_config_id")
    REFERENCES bs_notification_config ("id")
    );

CREATE TABLE IF NOT EXISTS bs_event (
    "id" UUID NOT NULL PRIMARY KEY,
    "assign" VARCHAR(50),
    "display" VARCHAR(50),
    "event_type" VARCHAR(50),
    "message" VARCHAR(50),
    "notification_type" VARCHAR(25),
    "subject" VARCHAR(50),
    "recipient_id" UUID,
    "created_by" UUID NOT NULL,
    "created_date" TIMESTAMP NOT NULL,
    "last_modified_by" UUID NOT NULL,
    "last_modified_date" TIMESTAMP NOT NULL,
    "version" INTEGER NOT NULL,
    "deleted" BOOLEAN NOT NULL
    );

    CREATE TABLE IF NOT EXISTS bs_notification_user (
    "user_id" UUID NOT NULL PRIMARY KEY,
    "ref_id" UUID,
    "notification_config_id" UUID,
    "created_by" UUID NOT NULL,
    "created_date" TIMESTAMP NOT NULL,
    "last_modified_by" UUID NOT NULL,
    "last_modified_date" TIMESTAMP NOT NULL,
    "version" INTEGER NOT NULL,
    "deleted" BOOLEAN NOT NULL,
    CONSTRAINT fk_bs_notification_user_notification_config_id FOREIGN KEY ("notification_config_id")
    REFERENCES bs_notification_config ("id")
    );
