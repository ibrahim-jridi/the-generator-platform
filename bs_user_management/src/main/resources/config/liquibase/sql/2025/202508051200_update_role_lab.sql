INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date", "last_modified_by", "last_modified_date", "version", "first_insert", "deleted")
VALUES
    (gen_random_uuid(), 'BS_LABORATORY', TRUE, 'BS_LABORATORY', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, TRUE, FALSE);

INSERT INTO "bs_rel_role_authority" ("role_id", "authority_id")
VALUES

    ( (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_LABO_ETRAN'), (SELECT id FROM bs_authority WHERE label = 'BS_DESIGNATIONS_USERS')),
    ( (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_LABO_ETRAN'), (SELECT id FROM bs_authority WHERE label = 'BS_USER')),
    ( (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_LABO_ETRAN'), (SELECT id FROM bs_authority WHERE label = 'BS_TASKS')),
    ( (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_LABO_LOC') ,  (SELECT id FROM bs_authority WHERE label = 'BS_DESIGNATIONS_USERS')),
    ( (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_LABO_LOC'), (SELECT id FROM bs_authority WHERE label = 'BS_USER')),
    ( (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_LABO_LOC'), (SELECT id FROM bs_authority WHERE label = 'BS_TASKS')),
    ( (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_LABO_ETRAN'), (SELECT id FROM bs_authority WHERE label = 'BS_LABORATORY')),
    ( (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_LABO_LOC'), (SELECT id FROM bs_authority WHERE label = 'BS_LABORATORY'));


UPDATE "bs_role"
SET "description" = 'Laboratoire Local',
    "last_modified_date" = NOW()
WHERE "label" LIKE 'BS_ROLE_PM_LABO_LOC';

UPDATE "bs_role"
SET "description" = 'Laboratoire Etranger',
    "last_modified_date" = NOW()
WHERE "label" LIKE 'BS_ROLE_PM_LABO_ETRAN';

