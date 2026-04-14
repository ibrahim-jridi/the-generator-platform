INSERT INTO "bs_role"("id", "label", "description", "is_active", "created_by", "created_date",
                      "last_modified_by", "last_modified_date", "version", "deleted")
VALUES



    ('5e95000e-1152-3333-a0eb-3ca2da928cfe', 'BS_ROLE_RESP_AGENCE_PROMOT', 'responsable agence promotion', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');







INSERT INTO "bs_rel_role_authority" ("role_id", "authority_id")
VALUES
    ('5e95000e-1152-3333-a0eb-3ca2da928cfe', (SELECT id FROM bs_authority WHERE label = 'BS_TASKS')),
    ('5e95000e-1152-3333-a0eb-3ca2da928cfe', (SELECT id FROM bs_authority WHERE label = 'BS_USER' )),
    ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_TECH'), (SELECT id FROM bs_authority WHERE label = 'BS_TASKS')),
    ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_TECH'), (SELECT id FROM bs_authority WHERE label = 'BS_USER' )),
    ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_GERANT'), (SELECT id FROM bs_authority WHERE label = 'BS_TASKS')),
    ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_GERANT'), (SELECT id FROM bs_authority WHERE label = 'BS_USER' )),
    ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_REP_LEG'), (SELECT id FROM bs_authority WHERE label = 'BS_TASKS')),
    ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_REP_LEG'), (SELECT id FROM bs_authority WHERE label = 'BS_USER' ));
