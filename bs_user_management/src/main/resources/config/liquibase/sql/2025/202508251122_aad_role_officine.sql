INSERT INTO "bs_role"("id", "label", "description", "is_active", "created_by", "created_date",
                      "last_modified_by", "last_modified_date", "version", "deleted")
VALUES

    (gen_random_uuid(), 'BS_ROLE_INT_CROPT', 'CROPT', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false'),


    (gen_random_uuid(), 'BS_ROLE_PM_OFFICINE', 'Officine', 'true',
     'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');



INSERT INTO "bs_rel_role_authority" ("role_id", "authority_id")
VALUES ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_INT_CROPT'),
        (SELECT id FROM bs_authority WHERE label = 'BS_TASKS')),
       ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_INT_CROPT'),
        (SELECT id FROM bs_authority WHERE label = 'BS_USER')),
       ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_OFFICINE'),
        (SELECT id FROM bs_authority WHERE label = 'BS_TASKS')),
       ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_OFFICINE'),
        (SELECT id FROM bs_authority WHERE label = 'BS_USER')),
       ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_OFFICINE'),
        (SELECT id FROM bs_authority WHERE label = 'BS_DESIGNATIONS_USERS'));


