INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date", "last_modified_by", "last_modified_date", "version", "first_insert", "deleted")
VALUES
    (gen_random_uuid(), 'BS_ADD_DESIGNATION', TRUE, 'BS_ADD_DESIGNATION', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, TRUE, FALSE);

INSERT INTO "bs_rel_role_authority" ("role_id", "authority_id")
VALUES
    ((SELECT id FROM "bs_role" WHERE "label" = 'BS_ROLE_PM_AGENCE_PROMOTION' LIMIT 1), (SELECT id FROM "bs_authority" WHERE "label" = 'BS_ADD_DESIGNATION' LIMIT 1)),
    ((SELECT id FROM "bs_role" WHERE "label" = 'BS_ROLE_PM_FILIALE_PCT' LIMIT 1), (SELECT id FROM "bs_authority" WHERE "label" = 'BS_ADD_DESIGNATION' LIMIT 1)),
    ((SELECT id FROM "bs_role" WHERE "label" = 'BS_ROLE_PM_AGENCE_PCT' LIMIT 1), (SELECT id FROM "bs_authority" WHERE "label" = 'BS_ADD_DESIGNATION' LIMIT 1)),
    ((SELECT id FROM "bs_role" WHERE "label" = 'BS_ROLE_PM_PCT' LIMIT 1), (SELECT id FROM "bs_authority" WHERE "label" = 'BS_ADD_DESIGNATION' LIMIT 1)),
    ((SELECT id FROM "bs_role" WHERE "label" = 'BS_ROLE_PM_CRO' LIMIT 1), (SELECT id FROM "bs_authority" WHERE "label" = 'BS_ADD_DESIGNATION' LIMIT 1)),
((SELECT id FROM "bs_role" WHERE "label" = 'BS_ROLE_PM_SOCIETE_CONSULTING' LIMIT 1), (SELECT id FROM "bs_authority" WHERE "label" = 'BS_ADD_DESIGNATION' LIMIT 1)),
((SELECT id FROM "bs_role" WHERE "label" = 'BS_ROLE_PM_EXPORT_IMPORT' LIMIT 1), (SELECT id FROM "bs_authority" WHERE "label" = 'BS_ADD_DESIGNATION' LIMIT 1)),
((SELECT id FROM "bs_role" WHERE "label" = 'BS_ROLE_PM_TRANSITAIRE' LIMIT 1), (SELECT id FROM "bs_authority" WHERE "label" = 'BS_ADD_DESIGNATION' LIMIT 1)),
((SELECT id FROM "bs_role" WHERE "label" = 'BS_ROLE_PM_ASSOCIATION' LIMIT 1), (SELECT id FROM "bs_authority" WHERE "label" = 'BS_ADD_DESIGNATION' LIMIT 1)),
((SELECT id FROM "bs_role" WHERE "label" = 'BS_ROLE_PM_STRUCT_DIPLOM' LIMIT 1), (SELECT id FROM "bs_authority" WHERE "label" = 'BS_ADD_DESIGNATION' LIMIT 1)),
((SELECT id FROM "bs_role" WHERE "label" = 'BS_ROLE_PM_SOCIETE_PHARMA' LIMIT 1), (SELECT id FROM "bs_authority" WHERE "label" = 'BS_ADD_DESIGNATION' LIMIT 1)),
((SELECT id FROM "bs_role" WHERE "label" = 'BS_ROLE_PM_DEFAULT' LIMIT 1), (SELECT id FROM "bs_authority" WHERE "label" = 'BS_ADD_DESIGNATION' LIMIT 1)),
((SELECT id FROM "bs_role" WHERE "label" = 'BS_ROLE_PM_GROSSISTE' LIMIT 1), (SELECT id FROM "bs_authority" WHERE "label" = 'BS_ADD_DESIGNATION' LIMIT 1));

