INSERT INTO "bs_rel_user_role"("user_id", "role_id")

VALUES
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112399' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_LABO_LOC' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112400' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_LABO_LOC' LIMIT 1));

