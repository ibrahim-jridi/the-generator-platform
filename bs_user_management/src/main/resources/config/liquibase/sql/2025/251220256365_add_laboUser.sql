--UPDATE-DENOMINATION

UPDATE "bs_user"
SET denomination = 'AQVIDA GMBH',
    social_reason = 'AQVIDA GMBH'
WHERE username = 'pm-ext-1111315';

UPDATE "bs_user"
SET denomination = 'BIOGEN Idec Ltd',
    social_reason='BIOGEN Idec Ltd'
WHERE username ='pm-ext-1111401';

UPDATE "bs_user"
SET denomination = 'BIOGEN NETHERLANDS B.V.',
    social_reason='BIOGEN NETHERLANDS B.V.'
WHERE username ='pm-ext-1111419';

--INSERT-NEW-USER

INSERT INTO "bs_user" ("id", "username", "email_verified", "keycloak_id", "email",
                       "is_active", "address", "country", "nationality", "phone_number", "created_by", "created_date",
                       "last_modified_by", "last_modified_date", "version", "deleted", "tax_registration",
                       "legal_status",
                       "activity_domain", "user_type", "social_reason", "file_status", "file_patent", "registry_status",
                       "denomination")
VALUES
    (gen_random_uuid(), 'pm-ext-1112412', true, gen_random_uuid(), 'amp@yopmail.com', true,
     'AMP address ', 'Tunisie', 'TUNISIAN', '20963970', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
     1, 'false', '0510680HAX913', 'SARL', 'parapharmaceutiques',
     'COMPANY', 'BeiGene USA,Inc', NULL, NULL, 'ACTIVE', 'BeiGene USA,Inc'),
    (gen_random_uuid(), 'pm-ext-1112413', true, gen_random_uuid(), 'amp@yopmail.com', true,
     'AMP address ', 'Tunisie', 'TUNISIAN', '20963971', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
     1, 'false', '0510680HAX903', 'SARL', 'parapharmaceutiques',
     'COMPANY', 'Bharat Serums and Vaccines Limited', NULL, NULL, 'ACTIVE', 'Bharat Serums and Vaccines Limited');


--AFFECT-ROLE

INSERT INTO "bs_rel_user_role"("user_id", "role_id")
VALUES
((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112412' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),


((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112412' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_LABO_ETRAN' LIMIT 1)),

((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112413' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),


((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112413' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_LABO_ETRAN' LIMIT 1));


DELETE FROM "bs_rel_user_role" ur
    USING "bs_user" u, "bs_role" r
WHERE ur.user_id = u.id
  AND ur.role_id = r.id
  AND u.username = 'pm-ext-1111242'
  AND r.label = 'BS_ROLE_DEFAULT_ROLE';

