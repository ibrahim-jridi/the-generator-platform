INSERT INTO "bs_rel_role_authority" ("role_id", "authority_id")
VALUES
    (  (SELECT id FROM bs_role WHERE label = 'BS_PM_CRO' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_DESIGNATIONS_USERS' LIMIT 1)),
    (  (SELECT id FROM bs_role WHERE label = 'BS_PM_AGENCE_PROMOT' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_DESIGNATIONS_USERS' LIMIT 1)),
    (  (SELECT id FROM bs_role WHERE label = 'BS_PM_INDUSTRIEL' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_DESIGNATIONS_USERS' LIMIT 1));
