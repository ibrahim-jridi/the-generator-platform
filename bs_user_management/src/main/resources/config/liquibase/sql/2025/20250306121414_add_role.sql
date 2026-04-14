INSERT INTO "bs_role"("id", "label", "description", "is_active", "created_by", "created_date",
                      "last_modified_by", "last_modified_date", "version", "deleted")
VALUES

    (gen_random_uuid(), 'BS_ROLE_PHARMA_RESP_PROD', 'Pharmacien Responsable de la Production', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false'),

    (gen_random_uuid(), 'BS_ROLE_RESP_CTRL_QA', 'Pharmacien Responsable du Contrôle de la Qualité', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false'),

    (gen_random_uuid(), 'BS_ROLE_RESP_PHARMACOVIG', 'Responsable de la Pharmacovigilance', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false'),

    (gen_random_uuid(), 'BS_ROLE_RESP_REG_AFF', 'Responsable des affaires réglementaires', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');


