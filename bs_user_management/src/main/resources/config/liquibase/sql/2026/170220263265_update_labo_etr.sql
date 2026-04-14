UPDATE "bs_user"
SET denomination = 'AL-TAQADDOM PHARMAEUTICAL INDUSTRIES',
    social_reason='AL-TAQADDOM PHARMAEUTICAL INDUSTRIES',
    email='alTaqaddomPharma@yopmail.com'
WHERE username ='pm-ext-1111900';

UPDATE "bs_user"
SET denomination = 'APM',
    social_reason='APM',
    email='apm@yopmail.com'
WHERE username ='pm-ext-1112018';

--ADD PM USERS

INSERT INTO "bs_user" ("id", "username", "email_verified", "keycloak_id", "email",
                       "is_active", "address", "country", "nationality", "phone_number", "created_by", "created_date",
                       "last_modified_by", "last_modified_date", "version", "deleted", "tax_registration",
                       "legal_status",
                       "activity_domain", "user_type", "social_reason", "file_status", "file_patent", "registry_status",
                       "denomination")
VALUES
    (gen_random_uuid(), 'pm-ext-1112414', true, gen_random_uuid(), 'beigeneUsa@yopmail.com', true,
    'beigeneUsa address', 'Tunisie', 'TUNISIAN', '71678901', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
    1, 'false', '0123206Z', 'SARL', 'parapharmaceutiques',
    'COMPANY', 'BEIGENE USA,INC', NULL, NULL, 'ACTIVE', 'BEIGENE USA,INC'),

    (gen_random_uuid(), 'pm-ext-1112415', true, gen_random_uuid(), 'bharatAndVaccines@yopmail.com', true,
     'bharatAndVaccines address', 'Tunisie', 'TUNISIAN', '71678902', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
     1, 'false', '0123206M', 'SARL', 'parapharmaceutiques',
     'COMPANY', 'BHARAT SERUMS AND VACCINES LIMITED', NULL, NULL, 'ACTIVE', 'BHARAT SERUMS AND VACCINES LIMITED'),

    (gen_random_uuid(), 'pm-ext-1112416', true, gen_random_uuid(), 'gileadSciences@yopmail.com', true,
     'gileadSciences address', 'Tunisie', 'TUNISIAN', '71678903', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
     1, 'false', '0197206N', 'SARL', 'parapharmaceutiques',
     'COMPANY', 'GILEAD SCIENCES IRELAND UC', NULL, NULL, 'ACTIVE', 'GILEAD SCIENCES IRELAND UC'),

    (gen_random_uuid(), 'pm-ext-1112417', true, gen_random_uuid(), 'intasPharma@yopmail.com', true,
     'intasPharma address', 'Tunisie', 'TUNISIAN', '71678904', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
     1, 'false', '0187206N', 'SARL', 'parapharmaceutiques',
     'COMPANY', 'INTAS PHARMACEUTICALS LTD.', NULL, NULL, 'ACTIVE', 'INTAS PHARMACEUTICALS LTD.'),

    (gen_random_uuid(), 'pm-ext-1112418', true, gen_random_uuid(), 'janssenCilag@yopmail.com', true,
     'janssenCilag address', 'Tunisie', 'TUNISIAN', '71678905', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
     1, 'false', '9187206P', 'SARL', 'parapharmaceutiques',
     'COMPANY', 'JANSSEN CILAG', NULL, NULL, 'ACTIVE', 'JANSSEN CILAG'),

    (gen_random_uuid(), 'pm-ext-1112419', true, gen_random_uuid(), 'groupBv@yopmail.com', true,
     'groupBv address', 'Tunisie', 'TUNISIAN', '71678906', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
     1, 'false', '1807206O', 'SARL', 'parapharmaceutiques',
     'COMPANY', 'NORDIC GROUP BV', NULL, NULL, 'ACTIVE', 'NORDIC GROUP BV'),

    (gen_random_uuid(), 'pm-ext-1112420', true, gen_random_uuid(), 'otsukaPharmaBv@yopmail.com', true,
     'otsukaPharmaBv address', 'Tunisie', 'TUNISIAN', '71678907', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
     1, 'false', '1987206O', 'SARL', 'parapharmaceutiques',
     'COMPANY', 'OTSUKA PHARMACEUTICAL NETHERLANDS BV', NULL, NULL, 'ACTIVE', 'OTSUKA PHARMACEUTICAL NETHERLANDS BV'),

    (gen_random_uuid(), 'pm-ext-1112421', true, gen_random_uuid(), 'pharmacarePremiumLimited@yopmail.com', true,
     'pharmacarePremiumLimited address', 'Tunisie', 'TUNISIAN', '71678908', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
     1, 'false', '6587206H', 'SARL', 'parapharmaceutiques',
     'COMPANY', 'PHARMACARE PREMIUM LIMITED', NULL, NULL, 'ACTIVE', 'PHARMACARE PREMIUM LIMITED'),

    (gen_random_uuid(), 'pm-ext-1112422', true, gen_random_uuid(), 'scInformedSRL@yopmail.com', true,
     'scInformedSRL address', 'Tunisie', 'TUNISIAN', '71678909', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
     1, 'false', '7687206H', 'SARL', 'parapharmaceutiques',
     'COMPANY', 'S.C.INFOMED FLUIDS S.R.L.', NULL, NULL, 'ACTIVE', 'S.C.INFOMED FLUIDS S.R.L.');



--AddRoleToPmUsers

INSERT INTO "bs_rel_user_role"("user_id", "role_id")
VALUES
((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112414' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112415' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112416' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112417' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112418' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112419' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112420' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112421' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112422' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),




((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112414' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_LABO_ETRAN' LIMIT 1)),
((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112415' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_LABO_ETRAN' LIMIT 1)),
((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112416' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_LABO_ETRAN' LIMIT 1)),
((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112417' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_LABO_ETRAN' LIMIT 1)),
((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112418' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_LABO_ETRAN' LIMIT 1)),
((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112419' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_LABO_ETRAN' LIMIT 1)),
((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112420' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_LABO_ETRAN' LIMIT 1)),
((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112421' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_LABO_ETRAN' LIMIT 1)),
((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112422' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_LABO_ETRAN' LIMIT 1)),


((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112414' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112415' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112416' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112417' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112418' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112419' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112420' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112421' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112422' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1));

