CREATE TABLE IF NOT EXISTS "bs_activity_type"
(
    id      UUID         NOT NULL PRIMARY KEY,
    code    VARCHAR(150) NOT NULL,
    name    VARCHAR(150) NOT NULL,
    user_id UUID         NOT NULL,
    version          INTEGER      NOT NULL,
    created_by       UUID         NOT NULL,
    created_date      TIMESTAMP    NOT NULL,
    last_modified_by    UUID         NOT NULL,
    last_modified_date TIMESTAMP    NOT NULL,
    deleted           BOOLEAN      NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES bs_user (id)
);
