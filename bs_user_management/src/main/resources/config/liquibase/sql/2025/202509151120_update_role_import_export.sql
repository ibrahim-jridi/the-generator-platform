INSERT INTO "bs_rel_role_authority" ("role_id", "authority_id")
VALUES ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_EXPORT_IMPORT'),
        (SELECT id FROM bs_authority WHERE label = 'BS_DESIGNATIONS_USERS'));

INSERT INTO "bs_rel_role_authority" ("role_id", "authority_id")
VALUES ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_TRANSITAIRE'),
        (SELECT id FROM bs_authority WHERE label = 'BS_DESIGNATIONS_USERS'));

INSERT INTO "bs_rel_role_authority" ("role_id", "authority_id")
VALUES ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_ASSOCIATION'),
        (SELECT id FROM bs_authority WHERE label = 'BS_DESIGNATIONS_USERS'));

INSERT INTO "bs_rel_role_authority" ("role_id", "authority_id")
VALUES ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_STRUCT_DIPLOM'),
        (SELECT id FROM bs_authority WHERE label = 'BS_DESIGNATIONS_USERS'));

INSERT INTO "bs_rel_role_authority" ("role_id", "authority_id")
VALUES ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_SOCIETE_PHARMA'),
        (SELECT id FROM bs_authority WHERE label = 'BS_DESIGNATIONS_USERS'));

INSERT INTO "bs_rel_role_authority" ("role_id", "authority_id")
VALUES ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_SOCIETE_CONSULTING'),
        (SELECT id FROM bs_authority WHERE label = 'BS_DESIGNATIONS_USERS'));

INSERT INTO "bs_rel_role_authority" ("role_id", "authority_id")
VALUES ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_DEFAULT'),
        (SELECT id FROM bs_authority WHERE label = 'BS_DESIGNATIONS_USERS'));
