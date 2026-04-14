
INSERT INTO "bs_role" ("id", "label", "description", "is_active", "created_by", "created_date",
                       "last_modified_by", "last_modified_date", "version", "deleted")
VALUES

    ('9c567e9b-4717-4505-b059-0b2e672ee827', 'BS_ROLE_TRANSITAIRE', 'Transitaire IMP/EXP', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');


INSERT INTO "bs_role" ("id", "label", "description", "is_active", "created_by", "created_date",
                       "last_modified_by", "last_modified_date", "version", "deleted")
VALUES

    ('2477998a-54b4-4228-87a4-5545ce53352f', 'BS_ROLE_ASSOCIATION', 'Association IMP/EXP', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');

INSERT INTO "bs_role" ("id", "label", "description", "is_active", "created_by", "created_date",
                       "last_modified_by", "last_modified_date", "version", "deleted")
VALUES

    ('5e95021e-1162-4444-a0eb-3ca2da928cfe', 'BS_ROLE_STRUCT_DIPLOM', 'Structure Dimplomatique IMP/EXP', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');

INSERT INTO "bs_role" ("id", "label", "description", "is_active", "created_by", "created_date",
                       "last_modified_by", "last_modified_date", "version", "deleted")
VALUES

    ('6d9745cb-44c3-4b9b-a8c1-5bdd8f09703f', 'BS_ROLE_SOCIETE_PHARMA', 'Société pharamaceutique IMP/EXP', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');

INSERT INTO "bs_rel_role_authority" ("role_id", "authority_id")
VALUES
    ('9c567e9b-4717-4505-b059-0b2e672ee827', (SELECT id FROM bs_authority WHERE label = 'BS_TASKS')),
    ('9c567e9b-4717-4505-b059-0b2e672ee827', (SELECT id FROM bs_authority WHERE label = 'BS_USER' )),
    ('2477998a-54b4-4228-87a4-5545ce53352f', (SELECT id FROM bs_authority WHERE label = 'BS_TASKS')),
    ('2477998a-54b4-4228-87a4-5545ce53352f', (SELECT id FROM bs_authority WHERE label = 'BS_USER' )),
    ('5e95021e-1162-4444-a0eb-3ca2da928cfe', (SELECT id FROM bs_authority WHERE label = 'BS_TASKS')),
    ('5e95021e-1162-4444-a0eb-3ca2da928cfe', (SELECT id FROM bs_authority WHERE label = 'BS_USER' )),
    ('6d9745cb-44c3-4b9b-a8c1-5bdd8f09703f', (SELECT id FROM bs_authority WHERE label = 'BS_TASKS')),
    ('6d9745cb-44c3-4b9b-a8c1-5bdd8f09703f', (SELECT id FROM bs_authority WHERE label = 'BS_USER' )),
    ('22222222-fffa-49c8-bf3b-45abfc94699f', (SELECT id FROM bs_authority WHERE label = 'BS_TASKS')),
    ('22222222-fffa-49c8-bf3b-45abfc94699f', (SELECT id FROM bs_authority WHERE label = 'BS_USER' )),
    ('22222222-fafa-49c8-bf3b-45abfc94699f', (SELECT id FROM bs_authority WHERE label = 'BS_TASKS')),
    ('22222222-fafa-49c8-bf3b-45abfc94699f', (SELECT id FROM bs_authority WHERE label = 'BS_USER' ));




