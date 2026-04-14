INSERT INTO "bs_role" ("id", "label", "description", "is_active", "created_by", "created_date",
                       "last_modified_by", "last_modified_date", "version", "deleted")
VALUES

    ('86b6727c-45ae-4aae-bb28-01c5073a67a9', 'BS_ROLE_SOCIETE_CONSULTING', 'Société Service Consulting Juridique IMP/EXP', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');

INSERT INTO "bs_rel_role_authority" ("role_id", "authority_id")
VALUES
    ('86b6727c-45ae-4aae-bb28-01c5073a67a9', (SELECT id FROM bs_authority WHERE label = 'BS_TASKS')),
    ('86b6727c-45ae-4aae-bb28-01c5073a67a9', (SELECT id FROM bs_authority WHERE label = 'BS_USER' ));

