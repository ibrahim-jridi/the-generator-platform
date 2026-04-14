
ALTER TABLE "bs_authority" ALTER COLUMN "label" TYPE VARCHAR;

INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date", "last_modified_by", "last_modified_date", "version", "first_insert", "deleted")
VALUES
    (gen_random_uuid(), 'BS_VIEW_DECLARATION_DEBUT_ACTIVITE', TRUE, 'BS_VIEW_DECLARATION_DEBUT_ACTIVITE', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, TRUE, FALSE),
    (gen_random_uuid(), 'BS_EDIT_DECLARATION_DEBUT_ACTIVITE', TRUE, 'BS_EDIT_DECLARATION_DEBUT_ACTIVITE', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, TRUE, FALSE),
    (gen_random_uuid(), 'BS_SUBMIT_DECLARATION_DEBUT_ACTIVITE', TRUE, 'BS_SUBMIT_DECLARATION_DEBUT_ACTIVITE', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, TRUE, FALSE),
    (gen_random_uuid(), 'BS_SAVE_AS_DRAFT_DECLARATION_DEBUT_ACTIVITE', TRUE, 'BS_SAVE_AS_DRAFT_DECLARATION_DEBUT_ACTIVITE', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, TRUE, FALSE);

INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date", "last_modified_by", "last_modified_date", "version", "first_insert", "deleted")
VALUES
    (gen_random_uuid(), 'BS_VIEW_ENGAGEMENT_RSE', TRUE, 'BS_VIEW_ENGAGEMENT_RSE', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, TRUE, FALSE),
    (gen_random_uuid(), 'BS_EDIT_ENGAGEMENT_RSE', TRUE, 'BS_EDIT_ENGAGEMENT_RSE', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, TRUE, FALSE),
    (gen_random_uuid(), 'BS_SUBMIT_ENGAGEMENT_RSE', TRUE, 'BS_SUBMIT_ENGAGEMENT_RSE', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, TRUE, FALSE),
    (gen_random_uuid(), 'BS_SAVE_AS_DRAFT_ENGAGEMENT_RSE', TRUE, 'BS_SAVE_AS_DRAFT_ENGAGEMENT_RSE', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, TRUE, FALSE);

INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date", "last_modified_by", "last_modified_date", "version", "first_insert", "deleted")
VALUES
    (gen_random_uuid(), 'BS_VIEW_BONNE_PRATIQUE', TRUE, 'BS_VIEW_BONNE_PRATIQUE', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, TRUE, FALSE),
    (gen_random_uuid(), 'BS_EDIT_BONNE_PRATIQUE', TRUE, 'BS_EDIT_BONNE_PRATIQUE', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, TRUE, FALSE),
    (gen_random_uuid(), 'BS_SUBMIT_BONNE_PRATIQUE', TRUE, 'BS_SUBMIT_BONNE_PRATIQUE', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, TRUE, FALSE),
    (gen_random_uuid(), 'BS_SAVE_AS_DRAFT_BONNE_PRATIQUE', TRUE, 'BS_SAVE_AS_DRAFT_BONNE_PRATIQUE', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, TRUE, FALSE);


INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date", "last_modified_by", "last_modified_date", "version", "first_insert", "deleted")
VALUES
    (gen_random_uuid(), 'BS_VIEW_ENGAGEMENT_DIGITAL', TRUE, 'BS_VIEW_ENGAGEMENT_DIGITAL', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, TRUE, FALSE),
    (gen_random_uuid(), 'BS_EDIT_ENGAGEMENT_DIGITAL', TRUE, 'BS_EDIT_ENGAGEMENT_DIGITAL', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, TRUE, FALSE),
    (gen_random_uuid(), 'BS_SUBMIT_ENGAGEMENT_DIGITAL', TRUE, 'BS_SUBMIT_ENGAGEMENT_DIGITAL', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, TRUE, FALSE),
    (gen_random_uuid(), 'BS_SAVE_AS_DRAFT_ENGAGEMENT_DIGITAL', TRUE, 'BS_SAVE_AS_DRAFT_ENGAGEMENT_DIGITAL', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, TRUE, FALSE);


INSERT INTO "bs_role" ("id", "label", "description", "is_active", "created_by", "created_date", "last_modified_by", "last_modified_date", "version", "deleted")
VALUES
    (gen_random_uuid(), 'PM_AGENCE_PROMOTION', 'PM_AGENCE_PROMOTION', TRUE, 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, FALSE);

INSERT INTO "bs_rel_role_authority" ("role_id", "authority_id")
VALUES
    ((SELECT id FROM "bs_role" WHERE "label" = 'PM_AGENCE_PROMOTION' LIMIT 1), (SELECT id FROM "bs_authority" WHERE "label" = 'BS_VIEW_DECLARATION_DEBUT_ACTIVITE' LIMIT 1)),
    ((SELECT id FROM "bs_role" WHERE "label" = 'PM_AGENCE_PROMOTION' LIMIT 1), (SELECT id FROM "bs_authority" WHERE "label" = 'BS_EDIT_DECLARATION_DEBUT_ACTIVITE' LIMIT 1)),
    ((SELECT id FROM "bs_role" WHERE "label" = 'PM_AGENCE_PROMOTION' LIMIT 1), (SELECT id FROM "bs_authority" WHERE "label" = 'BS_SUBMIT_DECLARATION_DEBUT_ACTIVITE' LIMIT 1)),
    ((SELECT id FROM "bs_role" WHERE "label" = 'PM_AGENCE_PROMOTION' LIMIT 1), (SELECT id FROM "bs_authority" WHERE "label" = 'BS_SAVE_AS_DRAFT_DECLARATION_DEBUT_ACTIVITE' LIMIT 1)),
    ((SELECT id FROM "bs_role" WHERE "label" = 'PM_AGENCE_PROMOTION' LIMIT 1), (SELECT id FROM "bs_authority" WHERE "label" = 'BS_VIEW_ENGAGEMENT_RSE' LIMIT 1)),
    ((SELECT id FROM "bs_role" WHERE "label" = 'PM_AGENCE_PROMOTION' LIMIT 1), (SELECT id FROM "bs_authority" WHERE "label" = 'BS_EDIT_ENGAGEMENT_RSE' LIMIT 1)),
    ((SELECT id FROM "bs_role" WHERE "label" = 'PM_AGENCE_PROMOTION' LIMIT 1), (SELECT id FROM "bs_authority" WHERE "label" = 'BS_SUBMIT_ENGAGEMENT_RSE' LIMIT 1)),
    ((SELECT id FROM "bs_role" WHERE "label" = 'PM_AGENCE_PROMOTION' LIMIT 1), (SELECT id FROM "bs_authority" WHERE "label" = 'BS_SAVE_AS_DRAFT_ENGAGEMENT_RSE' LIMIT 1)),
    ((SELECT id FROM "bs_role" WHERE "label" = 'PM_AGENCE_PROMOTION' LIMIT 1), (SELECT id FROM "bs_authority" WHERE "label" = 'BS_VIEW_BONNE_PRATIQUE' LIMIT 1)),
    ((SELECT id FROM "bs_role" WHERE "label" = 'PM_AGENCE_PROMOTION' LIMIT 1), (SELECT id FROM "bs_authority" WHERE "label" = 'BS_EDIT_BONNE_PRATIQUE' LIMIT 1)),
    ((SELECT id FROM "bs_role" WHERE "label" = 'PM_AGENCE_PROMOTION' LIMIT 1), (SELECT id FROM "bs_authority" WHERE "label" = 'BS_SUBMIT_BONNE_PRATIQUE' LIMIT 1)),
    ((SELECT id FROM "bs_role" WHERE "label" = 'PM_AGENCE_PROMOTION' LIMIT 1), (SELECT id FROM "bs_authority" WHERE "label" = 'BS_SAVE_AS_DRAFT_BONNE_PRATIQUE' LIMIT 1)),
    ((SELECT id FROM "bs_role" WHERE "label" = 'PM_AGENCE_PROMOTION' LIMIT 1), (SELECT id FROM "bs_authority" WHERE "label" = 'BS_VIEW_ENGAGEMENT_DIGITAL' LIMIT 1)),
    ((SELECT id FROM "bs_role" WHERE "label" = 'PM_AGENCE_PROMOTION' LIMIT 1), (SELECT id FROM "bs_authority" WHERE "label" = 'BS_EDIT_ENGAGEMENT_DIGITAL' LIMIT 1)),
    ((SELECT id FROM "bs_role" WHERE "label" = 'PM_AGENCE_PROMOTION' LIMIT 1), (SELECT id FROM "bs_authority" WHERE "label" = 'BS_SUBMIT_ENGAGEMENT_DIGITAL' LIMIT 1)),
    ((SELECT id FROM "bs_role" WHERE "label" = 'PM_AGENCE_PROMOTION' LIMIT 1), (SELECT id FROM "bs_authority" WHERE "label" = 'BS_SAVE_AS_DRAFT_ENGAGEMENT_DIGITAL' LIMIT 1)),
    ((SELECT id FROM "bs_role" WHERE label = 'PM_AGENCE_PROMOTION' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_TASKS' LIMIT 1)),
    ((SELECT id FROM "bs_role" WHERE label = 'PM_AGENCE_PROMOTION' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_USER' LIMIT 1));


INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date",
                            "last_modified_by",
                            "last_modified_date", "version", "first_insert", "deleted")
VALUES
    ( gen_random_uuid(), 'BS_VIEW_EQUITE_RECRUCTEMENT', TRUE, 'BS_VIEW_EQUITE_RECRUCTEMENT', 'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, TRUE, FALSE),
    ( gen_random_uuid(), 'BS_EDIT_EQUITE_RECRUTEMENT', TRUE, 'BS_EDIT_EQUITE_RECRUTEMENT', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, TRUE, FALSE),
    ( gen_random_uuid(), 'BS_SUBMIT_EQUITE_RECRUTEMENT', TRUE, 'BS_SUBMIT_EQUITE_RECRUTEMENT', 'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, TRUE, FALSE),
    ( gen_random_uuid(), 'BS_SAVE_AS_DRAFT_EQUITE_RECRUTEMENT', TRUE, 'BS_SAVE_AS_DRAFT_EQUITE_RECRUTEMENT', 'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, TRUE, FALSE);



INSERT INTO "bs_role" ("id", "label", "description", "is_active", "created_by", "created_date",
                       "last_modified_by",
                       "last_modified_date", "version", "deleted")
VALUES ( gen_random_uuid(), 'PM_CRO', 'PM_CRO', TRUE, 'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, FALSE);


INSERT INTO "bs_rel_role_authority" ("role_id", "authority_id")
VALUES
    (  (SELECT id FROM bs_role WHERE label = 'PM_CRO' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_VIEW_EQUITE_RECRUCTEMENT' LIMIT 1)),
    (  (SELECT id FROM bs_role WHERE label = 'PM_CRO' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_EDIT_EQUITE_RECRUTEMENT' LIMIT 1)),
    (  (SELECT id FROM bs_role WHERE label = 'PM_CRO' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_SUBMIT_EQUITE_RECRUTEMENT' LIMIT 1)),
    (  (SELECT id FROM bs_role WHERE label = 'PM_CRO' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_SAVE_AS_DRAFT_EQUITE_RECRUTEMENT' LIMIT 1)),
    (  (SELECT id FROM bs_role WHERE label = 'PM_CRO' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_TASKS' LIMIT 1)),
    (  (SELECT id FROM bs_role WHERE label = 'PM_CRO' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_USER' LIMIT 1));
