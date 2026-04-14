

INSERT INTO "bs_role" ("id", "label", "description", "is_active", "created_by", "created_date",
                       "last_modified_by",
                       "last_modified_date", "version", "deleted")
VALUES ('242a7856-1286-49c8-bf3b-45abfc94699f', 'DEFAULT_ROLE', 'DEFAULT_ROLE', 'true',
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',
        NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');

INSERT INTO "bs_rel_role_authority" ("role_id", "authority_id")
VALUES ('242a7856-1286-49c8-bf3b-45abfc94699f', (SELECT id FROM bs_authority WHERE label = 'BS_USER'));
