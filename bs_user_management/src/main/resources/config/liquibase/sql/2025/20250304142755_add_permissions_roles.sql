INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date",
                            "last_modified_by", "last_modified_date", "version", "first_insert", "deleted")
VALUES
    ('a2b2c3d4-e5f6-4789-abcd-123456789001', 'Création de compte pour un CRO', 'true', 'BS_CREATE_CRO_ACCOUNT',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 0, 'true', 'false');

INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date",
                            "last_modified_by", "last_modified_date", "version", "first_insert", "deleted")
VALUES

    ('a2b2c3d4-e5f6-4789-abcd-123456789002', 'Création d’un compte pour agence promo et d’info médicale et scientif', 'true', 'BS_CREATE_PROMOT_AGENCY',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 0, 'true', 'false');

INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date",
                            "last_modified_by", "last_modified_date", "version", "first_insert", "deleted")
VALUES

    ('a2b2c3d4-e5f6-4789-abcd-123456789003', 'Création d’une société d’import/export et sociétés de services', 'true', 'BS_CREATE_IMPORT_EXPORT',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 0, 'true', 'false');

INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date",
                            "last_modified_by", "last_modified_date", "version", "first_insert", "deleted")
VALUES

    ('a2b2c3d4-e5f6-4789-abcd-123456789004', 'Inscription à la liste d’attente', 'true', 'BS_WAITLIST_REGISTRATION',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 0, 'true', 'false');

INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date",
                            "last_modified_by", "last_modified_date", "version", "first_insert", "deleted")
VALUES

    ('a2b2c3d4-e5f6-4789-abcd-123456789005', 'Renouvellement d’inscription à la liste d’attente', 'true', 'BS_WAITLIST_RENEWAL',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 0, 'true', 'false');

INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date",
                            "last_modified_by", "last_modified_date", "version", "first_insert", "deleted")
VALUES

    ('a2b2c3d4-e5f6-4789-abcd-123456789006', 'Renouvellement d’inscri à la liste d’attente-Changement de catégorie', 'true', 'BS_WAIT_RENEW_CATEGOR_CHANG',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 0, 'true', 'false');

INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date",
                            "last_modified_by", "last_modified_date", "version", "first_insert", "deleted")
VALUES

    ('a1b2c3d4-e5f6-4789-abcd-123456789007','Renouvellement d’inscri à la liste d attente-Changement de gouv', 'true', 'BS_WAIT_RENEW_REGION_CHANGE',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 0, 'true', 'false');

INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date",
                            "last_modified_by", "last_modified_date", "version", "first_insert", "deleted")
VALUES

    ('a2b2c3d4-e5f6-4789-abcd-123456789008', 'Désinscription de la liste d’attente', 'true', 'BS_WAITLIST_UNSUBSCRIBE',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 0, 'true', 'false');




INSERT INTO "bs_role" ("id", "label", "description", "is_active", "created_by", "created_date",
                       "last_modified_by", "last_modified_date", "version", "deleted")
VALUES

    ('11111111-aafa-49c8-bf3b-45abfc94699f', 'BS_PP_PHARMACIEN', 'Pharmacien', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');

INSERT INTO "bs_role" ("id", "label", "description", "is_active", "created_by", "created_date",
                       "last_modified_by", "last_modified_date", "version", "deleted")
VALUES

    ('11111111-bbfb-49c8-bf3b-45abfc94699f', 'BS_PP_MEDECIN', 'Médecin', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');

INSERT INTO "bs_role" ("id", "label", "description", "is_active", "created_by", "created_date",
                       "last_modified_by", "last_modified_date", "version", "deleted")
VALUES

    ('11111111-ccfc-49c8-bf3b-45abfc94699f', 'BS_DEFAULT_PP', 'Utilisateur par défaut PP', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');

INSERT INTO "bs_role" ("id", "label", "description", "is_active", "created_by", "created_date",
                       "last_modified_by", "last_modified_date", "version", "deleted")
VALUES

    ('22222222-aaaf-49c8-bf3b-45abfc94699f', 'BS_PM_CRO', 'CRO', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');

INSERT INTO "bs_role" ("id", "label", "description", "is_active", "created_by", "created_date",
                       "last_modified_by", "last_modified_date", "version", "deleted")
VALUES

    ('22222222-bbbf-49c8-bf3b-45abfc94699f', 'BS_PM_PCT', 'PCT', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');

INSERT INTO "bs_role" ("id", "label", "description", "is_active", "created_by", "created_date",
                       "last_modified_by", "last_modified_date", "version", "deleted")
VALUES

    ('22222222-cccf-49c8-bf3b-45abfc94699f', 'BS_PM_AGENCE_PCT', 'Agence PCT', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');

INSERT INTO "bs_role" ("id", "label", "description", "is_active", "created_by", "created_date",
                       "last_modified_by", "last_modified_date", "version", "deleted")
VALUES

    ('22222222-dddf-49c8-bf3b-45abfc94699f', 'BS_PM_FILIALE_PCT', 'Filiale PCT', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');

INSERT INTO "bs_role" ("id", "label", "description", "is_active", "created_by", "created_date",
                       "last_modified_by", "last_modified_date", "version", "deleted")
VALUES

    ('22222222-eeef-49c8-bf3b-45abfc94699f', 'BS_PM_AGENCE_PROMOT', 'Agence de promotion', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');

INSERT INTO "bs_role" ("id", "label", "description", "is_active", "created_by", "created_date",
                       "last_modified_by", "last_modified_date", "version", "deleted")
VALUES

    ('22222222-fffa-49c8-bf3b-45abfc94699f', 'BS_PM_EXPORT_IMPORT', 'Société Import/Export', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');

INSERT INTO "bs_role" ("id", "label", "description", "is_active", "created_by", "created_date",
                       "last_modified_by", "last_modified_date", "version", "deleted")
VALUES

    ('22222222-fafa-49c8-bf3b-45abfc94699f', 'BS_PM_DEFAULT', 'Utilisateur par défaut PM', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');

INSERT INTO "bs_role" ("id", "label", "description", "is_active", "created_by", "created_date",
                       "last_modified_by", "last_modified_date", "version", "deleted")
VALUES

    ('22222222-fafb-49c8-bf3b-45abfc94699f', 'BS_PM_INDUSTRIEL', 'Industrie', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');

INSERT INTO "bs_role" ("id", "label", "description", "is_active", "created_by", "created_date",
                       "last_modified_by", "last_modified_date", "version", "deleted")
VALUES

    ('22222222-fafc-49c8-bf3b-45abfc94699f', 'BS_PM_DETTENTEUR_AGENCE', 'Détenteur Agence Promotion', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');

INSERT INTO "bs_role"("id", "label", "description", "is_active", "created_by", "created_date",
                      "last_modified_by", "last_modified_date", "version", "deleted")
VALUES

    ('33333333-aaaf-49c8-bf3b-45abfc94699f', 'BS_PM_DETTENTEUR_CRO', 'Détenteur d''une CRO', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');

INSERT INTO "bs_role"("id", "label", "description", "is_active", "created_by", "created_date",
                      "last_modified_by", "last_modified_date", "version", "deleted")
VALUES

    ('33333333-bbbf-49c8-bf3b-45abfc94699f', 'BS_PM_RESPO_CRO', 'Responsable d''une CRO', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');

INSERT INTO "bs_role"("id", "label", "description", "is_active", "created_by", "created_date",
                      "last_modified_by", "last_modified_date", "version", "deleted")
VALUES

    ('33333333-cccf-49c8-bf3b-45abfc94699f', 'BS_PM_RESPO_PROD_INVEST', 'Responsable produits invest.', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');

INSERT INTO "bs_role"("id", "label", "description", "is_active", "created_by", "created_date",
                      "last_modified_by", "last_modified_date", "version", "deleted")
VALUES

    ('33333333-dddf-49c8-bf3b-45abfc94699f', 'BS_PM_DELEGUEE_MEDICAL', 'Délégué médical', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');

INSERT INTO "bs_role"("id", "label", "description", "is_active", "created_by", "created_date",
                      "last_modified_by", "last_modified_date", "version", "deleted")
VALUES

    ('33333333-eeef-49c8-bf3b-45abfc94699f', 'BS_PM_VISITOR_MEDICAL', 'Visiteur médical', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');

INSERT INTO "bs_role"("id", "label", "description", "is_active", "created_by", "created_date",
                      "last_modified_by", "last_modified_date", "version", "deleted")
VALUES

    ('33333333-fffa-49c8-bf3b-45abfc94699f', 'BS_PM_PHARMA_ECHANTILLONS', 'Pharmacien gestion échant.', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');

INSERT INTO "bs_role"("id", "label", "description", "is_active", "created_by", "created_date",
                      "last_modified_by", "last_modified_date", "version", "deleted")
VALUES

    ('33333333-fafa-49c8-bf3b-45abfc94699f', 'BS_PM_PHARMA_RESP_TECH', 'Pharmacien Resp. Technique', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');

INSERT INTO "bs_role"("id", "label", "description", "is_active", "created_by", "created_date",
                      "last_modified_by", "last_modified_date", "version", "deleted")
VALUES

    ('33333333-fafb-49c8-bf3b-45abfc94699f', 'BS_PM_REPRI_LABO', 'Représentant Laboratoire', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');

INSERT INTO "bs_role"("id", "label", "description", "is_active", "created_by", "created_date",
                      "last_modified_by", "last_modified_date", "version", "deleted")
VALUES

    ('33333333-fafc-49c8-bf3b-45abfc94699f', 'BS_PM_PHARMA_STOCK', 'Pharmacien gestion stock', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');

INSERT INTO "bs_rel_role_authority" ("role_id", "authority_id")
VALUES

    ('11111111-aafa-49c8-bf3b-45abfc94699f', (SELECT id FROM bs_authority WHERE label = 'BS_CREATE_CRO_ACCOUNT')),
    ('11111111-aafa-49c8-bf3b-45abfc94699f', (SELECT id FROM bs_authority WHERE label = 'BS_CREATE_PROMOT_AGENCY')),
    ('11111111-aafa-49c8-bf3b-45abfc94699f', (SELECT id FROM bs_authority WHERE label = 'BS_CREATE_IMPORT_EXPORT')),
    ('11111111-aafa-49c8-bf3b-45abfc94699f', (SELECT id FROM bs_authority WHERE label = 'BS_WAITLIST_REGISTRATION')),
    ('11111111-aafa-49c8-bf3b-45abfc94699f', (SELECT id FROM bs_authority WHERE label = 'BS_WAITLIST_RENEWAL')),
    ('11111111-aafa-49c8-bf3b-45abfc94699f', (SELECT id FROM bs_authority WHERE label = 'BS_WAIT_RENEW_CATEGOR_CHANG')),
    ('11111111-aafa-49c8-bf3b-45abfc94699f', (SELECT id FROM bs_authority WHERE label = 'BS_WAIT_RENEW_REGION_CHANGE')),
    ('11111111-aafa-49c8-bf3b-45abfc94699f', (SELECT id FROM bs_authority WHERE label = 'BS_WAITLIST_UNSUBSCRIBE'));

INSERT INTO "bs_rel_role_authority" ("role_id", "authority_id")
VALUES

    ('11111111-bbfb-49c8-bf3b-45abfc94699f', (SELECT id FROM bs_authority WHERE label = 'BS_CREATE_CRO_ACCOUNT')),
    ('11111111-bbfb-49c8-bf3b-45abfc94699f', (SELECT id FROM bs_authority WHERE label = 'BS_CREATE_PROMOT_AGENCY')),
    ('11111111-bbfb-49c8-bf3b-45abfc94699f', (SELECT id FROM bs_authority WHERE label = 'BS_CREATE_IMPORT_EXPORT'));

INSERT INTO "bs_rel_role_authority" ("role_id", "authority_id")
VALUES

    ('11111111-ccfc-49c8-bf3b-45abfc94699f', (SELECT id FROM bs_authority WHERE label = 'BS_CREATE_CRO_ACCOUNT')),
    ('11111111-ccfc-49c8-bf3b-45abfc94699f', (SELECT id FROM bs_authority WHERE label = 'BS_CREATE_IMPORT_EXPORT'));



INSERT INTO "bs_role" ("id", "label", "description", "is_active", "created_by", "created_date",
                       "last_modified_by", "last_modified_date", "version", "deleted")
VALUES
    ('11111111-affa-49c8-bf3b-45abfc94699f', 'BS_PP_MEDECIN_DENTISTE', 'Médecin dentiste', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');
INSERT INTO "bs_role" ("id", "label", "description", "is_active", "created_by", "created_date",
                       "last_modified_by", "last_modified_date", "version", "deleted")
VALUES
    ('11111111-fafa-49c8-bf3b-45abfc94699f', 'BS_PP_MEDECIN_VETERINAIRE', 'Médecin veterinaire', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');

INSERT INTO "bs_rel_role_authority" ("role_id", "authority_id")
VALUES
    ('11111111-fafa-49c8-bf3b-45abfc94699f', (SELECT id FROM bs_authority WHERE label = 'BS_CREATE_CRO_ACCOUNT')),
    ('11111111-fafa-49c8-bf3b-45abfc94699f', (SELECT id FROM bs_authority WHERE label = 'BS_CREATE_PROMOT_AGENCY')),
    ('11111111-fafa-49c8-bf3b-45abfc94699f', (SELECT id FROM bs_authority WHERE label = 'BS_CREATE_IMPORT_EXPORT'));

INSERT INTO "bs_rel_role_authority" ("role_id", "authority_id")
VALUES
    ('11111111-affa-49c8-bf3b-45abfc94699f', (SELECT id FROM bs_authority WHERE label = 'BS_CREATE_CRO_ACCOUNT')),
    ('11111111-affa-49c8-bf3b-45abfc94699f', (SELECT id FROM bs_authority WHERE label = 'BS_CREATE_PROMOT_AGENCY')),
    ('11111111-affa-49c8-bf3b-45abfc94699f', (SELECT id FROM bs_authority WHERE label = 'BS_CREATE_IMPORT_EXPORT'));
