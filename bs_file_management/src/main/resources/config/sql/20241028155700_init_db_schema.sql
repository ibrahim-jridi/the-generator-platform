CREATE TABLE "bs_size_config"
(
    "id"                 UUID             NOT NULL,
    "extension"          VARCHAR(25)      NOT NULL,
    "max_size"           DOUBLE PRECISION NOT NULL,
    "created_by"         UUID             NOT NULL,
    "created_date"       TIMESTAMP        NOT NULL,
    "last_modified_by"   UUID             NOT NULL,
    "last_modified_date" TIMESTAMP        NOT NULL,
    "version"            INTEGER          NOT NULL,
    "deleted"            BOOLEAN          NOT NULL,
    PRIMARY KEY ("id")
)
;
CREATE TABLE "bs_folder"
(
    "id"                 UUID         NOT NULL,
    "folder_minio_id"    UUID         NOT NULL,
    "name"               VARCHAR(255) NOT NULL,
    "path"               VARCHAR(255) NOT NULL,
    "recurrence"         BIGINT NULL DEFAULT NULL,
    "user_id"            UUID NULL DEFAULT NULL,
    "parent_id"          UUID NULL DEFAULT NULL,
    "created_by"         UUID         NOT NULL,
    "created_date"       TIMESTAMP    NOT NULL,
    "last_modified_by"   UUID         NOT NULL,
    "last_modified_date" TIMESTAMP    NOT NULL,
    "version"            INTEGER      NOT NULL,
    "deleted"            BOOLEAN      NOT NULL,


    PRIMARY KEY ("id"),
    CONSTRAINT "fk_bs_folder_id" FOREIGN KEY ("parent_id") REFERENCES "bs_folder" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION
)
;
CREATE TABLE "bs_file"
(
    "id"                 UUID         NOT NULL,
    "file_minio_id"      UUID         NOT NULL,
    "folder_id"          UUID NULL DEFAULT NULL,
    "name"               VARCHAR(255) NOT NULL,
    "path"               VARCHAR(255) NOT NULL,
    "type"               VARCHAR(25)  NOT NULL,
    "size"               VARCHAR(25)  NOT NULL,
    "extension"          VARCHAR(25)  NOT NULL,
    "in_workflow"        BOOLEAN      NOT NULL,
    "created_by"         UUID         NOT NULL,
    "created_date"       TIMESTAMP    NOT NULL,
    "last_modified_by"   UUID         NOT NULL,
    "last_modified_date" TIMESTAMP    NOT NULL,
    "version"            INTEGER      NOT NULL,
    "deleted"            BOOLEAN      NOT NULL,
    PRIMARY KEY ("id"),
    CONSTRAINT "fk_bs_folder_id" FOREIGN KEY ("folder_id") REFERENCES "bs_folder" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION
)
;


