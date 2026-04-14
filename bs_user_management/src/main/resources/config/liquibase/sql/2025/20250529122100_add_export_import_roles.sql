INSERT INTO "bs_role"("id", "label", "description", "is_active", "created_by", "created_date",
                      "last_modified_by", "last_modified_date", "version", "deleted")
VALUES

    (gen_random_uuid(), 'BS_ROLE_PM_RESP_TECH', 'le responsable technique de la société crée', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false'),

    (gen_random_uuid(), 'BS_ROLE_PM_GERANT', 'le gérant de la société crée', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false'),

    (gen_random_uuid(), 'BS_ROLE_PM_REP_LEG', 'le représentant légal', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');


