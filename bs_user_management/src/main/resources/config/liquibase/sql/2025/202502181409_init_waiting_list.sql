CREATE TABLE IF NOT EXISTS "bs_waiting_list"
(
    "id"                 UUID         NOT NULL,
    "id_user"            UUID         NOT NULL,
    "rank"               INTEGER      NOT NULL,
    "category"           VARCHAR(50) NOT NULL,
    "governorate"        VARCHAR(50) NOT NULL,
    "delegation"         VARCHAR(50) ,
    "municipality"       VARCHAR(50) ,
    "status"             VARCHAR(25) NOT NULL,
    "date_renewal"       DATE,
    "created_by"         UUID         NOT NULL,
    "created_date"       TIMESTAMP    NOT NULL,
    "last_modified_by"   UUID         NOT NULL,
    "last_modified_date" TIMESTAMP    NOT NULL,
    "version"            INTEGER      NOT NULL,
    "deleted"            BOOLEAN      NOT NULL,
    PRIMARY KEY ("id")
);
