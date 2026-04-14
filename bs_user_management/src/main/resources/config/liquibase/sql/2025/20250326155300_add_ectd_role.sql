INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date",
                            "last_modified_by", "last_modified_date", "version", "first_insert", "deleted")
VALUES
    ('a2b2c3a2-e5f3-4789-bacd-123456689010', 'redirection ECTD', 'true', 'BS_REDIRECTION_ECTD',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 0, 'true', 'false');

INSERT INTO "bs_role" ("id", "label", "description", "is_active", "created_by", "created_date",
                       "last_modified_by", "last_modified_date", "version", "deleted")
VALUES

    ('11122111-bbfa-49c5-bb2b-45abfc95599f', 'BS_ECTD', 'redirection ECTD', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');


INSERT INTO "bs_rel_role_authority" ("role_id", "authority_id")
VALUES

    ('11122111-bbfa-49c5-bb2b-45abfc95599f', (SELECT id FROM bs_authority WHERE label = 'BS_REDIRECTION_ECTD'));
