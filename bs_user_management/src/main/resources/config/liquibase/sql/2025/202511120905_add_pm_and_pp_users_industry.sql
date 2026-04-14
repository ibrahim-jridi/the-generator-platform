--UPDATE TAXREGISTRATION FOR TERIAK (site djebel ouest)
UPDATE "bs_user"
SET tax_registration = '0510687HAE001'
WHERE username ='pm-ext-1111242';


--CREATE PM USERS
INSERT INTO "bs_user" ("id", "username", "email_verified", "keycloak_id", "email",
                       "is_active", "address", "country", "nationality", "phone_number", "created_by", "created_date",
                       "last_modified_by", "last_modified_date", "version", "deleted", "tax_registration",
                       "legal_status",
                       "activity_domain", "user_type", "social_reason", "file_status", "file_patent", "registry_status",
                       "denomination")
VALUES
    (gen_random_uuid(), 'pm-ext-1112399', true, gen_random_uuid(), 'pharmafen@yopmail.com', true,
     'pharmafen address', 'Tunisie', 'TUNISIAN', '20963940', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
     1, 'false', '1695029GAM000', 'SARL', 'parapharmaceutiques',
     'COMPANY', 'PHARMAFEN', NULL, NULL, 'ACTIVE', 'PHARMAFEN'),

    (gen_random_uuid(), 'pm-ext-1112400', true, gen_random_uuid(), 'teriakSiteElFejja@yopmail.com', true,
     'TERIAK (site djebel Fejja) ', 'Tunisie', 'TUNISIAN', '20963940', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
     1, 'false', '0510687HAE002', 'SARL', 'parapharmaceutiques',
     'COMPANY', 'TERIAK (Site  El Fejja)', NULL, NULL, 'ACTIVE', 'TERIAK (Site  El Fejja)');

--AFFECTATION ROLE

INSERT INTO "bs_rel_user_role"("user_id", "role_id")

VALUES
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112399' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_INDUSTRIEL' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112400' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_INDUSTRIEL' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112399' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112400' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1));


-- UPDATE AND CREATE PP USERS
UPDATE "bs_user"
SET phone_number = '+21695891171'
WHERE username ='ext-1111196';

UPDATE "bs_user"
SET email = 'fakher.naghmouchi@teriak.com'
WHERE username ='ext-1111196';

UPDATE "bs_user"
SET phone_number = '+21693346144'
WHERE username ='ext-1111197';


UPDATE "bs_user"
SET email = 'khaled.arab@teriak.com'
WHERE username ='ext-1111197';

UPDATE "bs_user"
SET email = 'yasmine.bensalem@medicis.tn '
WHERE username ='ext-1111198';


UPDATE "bs_user"
SET phone_number = '+21693727639'
WHERE username ='ext-1111198';


INSERT INTO "bs_user" ("id", "username", "email_verified", "keycloak_id", "email",
                       "is_active", "gender",
                       "first_name", "last_name", "address", "age", "birth_date", "national_id",
                       "country", "e_barid",
                       "profile_completed", "nationality", "phone_number", "created_by",
                       "created_date",
                       "last_modified_by", "last_modified_date", "version", "deleted", "user_type")
VALUES
    (gen_random_uuid(), 'ext-1111330', true ,gen_random_uuid(),'marwa.guesmi@pharmafen.tn', true, 'FEMALE',
        'Marwa', 'GUESMI',' 10 addressee ، 1003', '99','1988-04-01', '07898697', 'Tunisie', NULL, NULL, 'TUNISIAN',
        '52407002','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1,
        false, 'PHYSICAL'),
    (gen_random_uuid(), 'ext-1111331', true ,gen_random_uuid(),'nadia.ghaieth@teriak.com', true, 'FEMALE',
     'Nadia', 'Ghaieth',' 10 addressee ، 1003', '99','1988-04-01', '00976937', 'Tunisie', NULL, NULL, 'TUNISIAN',
     '98702391','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1,
     false, 'PHYSICAL'),
    (gen_random_uuid(), 'ext-1111332', true ,gen_random_uuid(),'omar.maghraoui@teriak.com', true, 'MALE',
     'Omar', 'Maghraoui',' 10 addressee ، 1003', '99','1988-04-01', '09489975', 'Tunisie', NULL, NULL, 'TUNISIAN',
     '93179243','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1,
     false, 'PHYSICAL'),
    (gen_random_uuid(), 'ext-1111333', true ,gen_random_uuid(),'youssef.fendri@pharmafen.tn', true, 'MALE',
     'Youssef ', 'FENDRI',' 10 addressee ، 1003', '99','1988-04-01', '11013239', 'Tunisie', NULL, NULL, 'TUNISIAN',
     '55410400','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1,
     false, 'PHYSICAL'),
    (gen_random_uuid(), 'ext-1111334', true ,gen_random_uuid(),'kaouthar.chelly@teriak.com', true, 'FEMALE',
     'Chelly', 'Kaouthar',' 10 addressee ، 1003', '99','1988-04-01', '06479377', 'Tunisie', NULL, NULL, 'TUNISIAN',
     '+21699177740','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1,
     false, 'PHYSICAL'),
    (gen_random_uuid(), 'ext-1111335', true ,gen_random_uuid(),'chaima.boughanmi@teriak.com', true, 'FEMALE',
     'chaima', 'Boughanmi',' 10 addressee ، 1003', '99','1988-04-01', '07213177', 'Tunisie', NULL, NULL, 'TUNISIAN',
     '93179297','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1,
     false, 'PHYSICAL'),
    (gen_random_uuid(), 'ext-1111336', true ,gen_random_uuid(),'safa.hedhili@teriak.com', true, 'FEMALE',
     'Safa', 'Hedhili',' 10 addressee ، 1003', '99','1988-04-01', '00987011', 'Tunisie', NULL, NULL, 'TUNISIAN',
     '92136923','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1,
     false, 'PHYSICAL');

--AFFECT ROLE TO PP USERS
    INSERT INTO "bs_rel_user_role"("user_id", "role_id")

VALUES
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111330' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111331' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111332' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111333' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111334' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111335' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111336' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1));


-- INSERT-DESIGNATIONS_TO_PM_USERS

INSERT INTO "bs_designations_list"("id", "pm_user_id", "designated_user_id", "role_id", "created_by", "created_date",
                                   "last_modified_by", "last_modified_date", "version", "deleted")
VALUES
    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112399' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111330' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_TECH' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),  1,'false'),
    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112400' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111331' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_TECH' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
    1,'false'),

    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112399' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111330' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_PROD' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),  1,'false'),
    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112400' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111332' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_PROD' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
    1,'false'),

    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112399' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111333' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_CTRL_QA' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),  1,'false'),
    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112400' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111336' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_CTRL_QA' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
    1,'false'),

    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112399' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111330' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),  1,'false'),
    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1111242' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111198' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
    1,'false'),
    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112400' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111198' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
    1,'false'),

    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112399' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111333' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),  1,'false'),
    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1111242' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111334' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
    1,'false'),
    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112400' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111335' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
    1,'false');
