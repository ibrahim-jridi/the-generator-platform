CREATE TABLE IF NOT EXISTS "bs_user"
(
    "id"                 UUID         NOT NULL,
    "username"           VARCHAR(25)  NOT NULL,
    "email_verified"     BOOLEAN NULL DEFAULT NULL,
    "keycloak_id"        UUID         NOT NULL,
    "email"              VARCHAR(150) NOT NULL,
    "is_active"          BOOLEAN NULL DEFAULT NULL,
    "gender"             VARCHAR(255) NULL DEFAULT NULL,
    "first_name"         VARCHAR(25) NULL DEFAULT NULL,
    "last_name"          VARCHAR(25) NULL DEFAULT NULL,
    "address"            VARCHAR(150) NULL DEFAULT NULL,
    "age"                VARCHAR(3) NULL DEFAULT NULL,
    "birth_date"         DATE NULL DEFAULT NULL,
    "cin"                VARCHAR(8) NULL DEFAULT NULL,
    "country" VARCHAR(100) NULL DEFAULT NULL,
    "e_barid"            VARCHAR(25) NULL DEFAULT NULL,
    "profile_completed"  BOOLEAN NULL DEFAULT NULL,
    "nationality"        VARCHAR(25) NULL DEFAULT NULL,
    "phone_number"       VARCHAR(8) NULL DEFAULT NULL,
    "created_by"         UUID         NOT NULL,
    "created_date"       TIMESTAMP    NOT NULL,
    "last_modified_by"   UUID         NOT NULL,
    "last_modified_date" TIMESTAMP    NOT NULL,
    "version"            INTEGER      NOT NULL,
    "deleted"            BOOLEAN      NOT NULL,
    PRIMARY KEY ("id"),
    CONSTRAINT "bs_user_gender_check" CHECK ((((gender)::text = ANY ((ARRAY['FEMALE':: character varying, 'MALE':: character varying])::text[])
) ))
);

CREATE UNIQUE INDEX keycloak_id_ind on bs_user (keycloak_id);
CREATE UNIQUE INDEX username_ind on bs_user (username);

CREATE TABLE IF NOT EXISTS "bs_role"
(
    "id"                 UUID      NOT NULL,
    "label"              VARCHAR(30) NULL DEFAULT NULL,
    "description"        VARCHAR(150) NULL DEFAULT NULL,
    "is_active"          BOOLEAN NULL DEFAULT NULL,
    "created_by"         UUID      NOT NULL,
    "created_date"       TIMESTAMP NOT NULL,
    "last_modified_by"   UUID      NOT NULL,
    "last_modified_date" TIMESTAMP NOT NULL,
    "version"            INTEGER NOT NULL,
    "deleted"            BOOLEAN NOT NULL,
    PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS bs_rel_user_role
(
    user_id
    uuid
    NOT
    NULL,
    role_id
    uuid
    NOT
    NULL,
    CONSTRAINT
    bs_rel_user_role_pkey
    PRIMARY
    KEY
(
    user_id,
    role_id
),
    CONSTRAINT fk_bs_user_id FOREIGN KEY
(
    user_id
)
    REFERENCES bs_user
(
    id
) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT fk_bs_role_id FOREIGN KEY
(
    role_id
)
    REFERENCES bs_role
(
    id
) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    );

CREATE TABLE IF NOT EXISTS "bs_group"
(
    "id"                 UUID NOT NULL,
    "description"        VARCHAR(150) NULL DEFAULT NULL,
    "is_active"          BOOLEAN NULL DEFAULT NULL,
    "label"              VARCHAR(25) NULL DEFAULT NULL,
    "parent_id"          UUID NULL DEFAULT NULL,
    "created_by"         UUID NOT NULL,
    "created_date"       TIMESTAMP NOT NULL,
    "last_modified_by"   UUID  NOT NULL,
    "last_modified_date" TIMESTAMP NOT NULL,
    "version"            INTEGER NOT NULL,
    "deleted"            BOOLEAN NOT NULL,
    PRIMARY KEY ("id"),
    CONSTRAINT "fk_parent_id" FOREIGN KEY ("parent_id") REFERENCES "bs_group" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS bs_rel_user_group
(
    user_id
    uuid
    NOT
    NULL,
    group_id
    uuid
    NOT
    NULL,
    CONSTRAINT
    bs_rel_user_group_pkey
    PRIMARY
    KEY
(
    user_id,
    group_id
),
    CONSTRAINT fk_bs_user_id FOREIGN KEY
(
    user_id
)
    REFERENCES bs_user
(
    id
) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT fk_bs_group_id FOREIGN KEY
(
    group_id
)
    REFERENCES bs_group
(
    id
) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    );

CREATE TABLE IF NOT EXISTS "bs_authority"
(
    "id"                 UUID NOT NULL,
    "description"        VARCHAR(150) NULL DEFAULT NULL,
    "is_active"          BOOLEAN NULL DEFAULT NULL,
    "label"              VARCHAR(25) NULL DEFAULT NULL :: character varying,
    "created_by"         UUID NOT NULL,
    "created_date"       TIMESTAMP NOT NULL,
    "last_modified_by"   UUID NOT NULL,
    "last_modified_date" TIMESTAMP NOT NULL,
    "version"            INTEGER NOT NULL,
    "first_insert"       BOOLEAN NOT NULL,
    "deleted"            BOOLEAN NOT NULL,
    PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS bs_rel_role_authority
(
    role_id
    uuid
    NOT
    NULL,
    authority_id
    uuid
    NOT
    NULL,
    CONSTRAINT
    bs_rel_role_authority_pkey
    PRIMARY
    KEY
(
    role_id,
    authority_id
),
    CONSTRAINT fk_bs_role_id FOREIGN KEY
(
    role_id
)
    REFERENCES bs_role
(
    id
) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT fk_bs_authority_id FOREIGN KEY
(
    authority_id
)
    REFERENCES bs_authority
(
    id
) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    );
