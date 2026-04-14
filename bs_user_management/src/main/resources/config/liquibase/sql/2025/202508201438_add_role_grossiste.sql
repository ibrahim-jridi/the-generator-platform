INSERT INTO "bs_role"("id", "label", "description", "is_active", "created_by", "created_date",
                      "last_modified_by", "last_modified_date", "version", "deleted")
VALUES

    (gen_random_uuid(), 'BS_ROLE_INT_CNOPT', 'CNOPT', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false'),

    (gen_random_uuid(), 'BS_ROLE_INT_INSP_CENT', 'Service inspection centrale', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false'),

    (gen_random_uuid(), 'BS_ROLE_INT_INSP_REG', 'Service inspection régionale', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false'),

    (gen_random_uuid(), 'BS_ROLE_PM_GROSSISTE', 'Grossiste', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');



INSERT INTO "bs_rel_role_authority" ("role_id", "authority_id")
VALUES ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_INT_CNOPT'),
        (SELECT id FROM bs_authority WHERE label = 'BS_TASKS')),
       ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_INT_CNOPT'),
        (SELECT id FROM bs_authority WHERE label = 'BS_USER')),
       ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_INT_INSP_CENT'),
        (SELECT id FROM bs_authority WHERE label = 'BS_TASKS')),
       ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_INT_INSP_CENT'),
        (SELECT id FROM bs_authority WHERE label = 'BS_USER')),
       ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_INT_INSP_REG'),
        (SELECT id FROM bs_authority WHERE label = 'BS_TASKS')),
       ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_INT_INSP_REG'),
        (SELECT id FROM bs_authority WHERE label = 'BS_USER')),
       ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_GROSSISTE'),
        (SELECT id FROM bs_authority WHERE label = 'BS_TASKS')),
       ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_GROSSISTE'),
        (SELECT id FROM bs_authority WHERE label = 'BS_USER')),
       ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_GROSSISTE'),
        (SELECT id FROM bs_authority WHERE label = 'BS_DESIGNATIONS_USERS'));


