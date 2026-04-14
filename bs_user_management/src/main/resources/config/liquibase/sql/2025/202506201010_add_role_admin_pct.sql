INSERT INTO "bs_role"("id", "label", "description", "is_active", "created_by", "created_date",
                      "last_modified_by", "last_modified_date", "version", "deleted")
VALUES (gen_random_uuid(), 'BS_ROLE_INT_ADMIN_PCT', 'Admin du compte PCT', 'true',
        'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');

INSERT INTO "bs_rel_role_authority" ("role_id", "authority_id")
VALUES ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_INT_ADMIN_PCT'),
        (SELECT id FROM bs_authority WHERE label = 'BS_TASKS')),
       ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_INT_ADMIN_PCT'),
        (SELECT id FROM bs_authority WHERE label = 'BS_USER')),
       ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_INT_ADMIN_PCT'),
        (SELECT id FROM bs_authority WHERE label = 'BS_CREATE_PCT')),
       ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_INT_ADMIN_PCT'),
        (SELECT id FROM bs_authority WHERE label = 'BS_CREATE_AGENCE_PCT')),
       ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_INT_ADMIN_PCT'),
        (SELECT id FROM bs_authority WHERE label = 'BS_CREATE_SUBSIDIARY_PCT')),
       ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_AGENCE_PCT'),
        (SELECT id FROM bs_authority WHERE label = 'BS_TASKS')),
       ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_AGENCE_PCT'),
        (SELECT id FROM bs_authority WHERE label = 'BS_USER')),
       ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_FILIALE_PCT'),
        (SELECT id FROM bs_authority WHERE label = 'BS_TASKS')),
       ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_FILIALE_PCT'),
        (SELECT id FROM bs_authority WHERE label = 'BS_USER')),
       ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PCT'),
        (SELECT id FROM bs_authority WHERE label = 'BS_TASKS')),
       ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PCT'), (SELECT id FROM bs_authority WHERE label = 'BS_USER'));



