UPDATE "bs_rel_user_role"
SET "role_id" = (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_LABO_LOC' LIMIT 1)
WHERE "user_id" IN (
    SELECT id FROM "bs_user"
    WHERE username IN (
    'pm-ext-1111221', 'pm-ext-1111222', 'pm-ext-1111223', 'pm-ext-1111224',
    'pm-ext-1111225', 'pm-ext-1111226', 'pm-ext-1111227', 'pm-ext-1111228',
    'pm-ext-1111229', 'pm-ext-1111230', 'pm-ext-1111231', 'pm-ext-1111232',
    'pm-ext-1111233', 'pm-ext-1111234', 'pm-ext-1111235', 'pm-ext-1111236',
    'pm-ext-1111237', 'pm-ext-1111238', 'pm-ext-1111239', 'pm-ext-1111240',
    'pm-ext-1111241', 'pm-ext-1111242', 'pm-ext-1111243', 'pm-ext-1111244',
    'pm-ext-1111245', 'pm-ext-1111246', 'pm-ext-1111247', 'pm-ext-1111248',
    'pm-ext-1111249', 'pm-ext-1111250', 'pm-ext-1111251'
    )
    )
  AND "role_id" = (SELECT id FROM "bs_role" WHERE label = 'DEFAULT_ROLE' LIMIT 1)
  AND NOT EXISTS (
    SELECT 1 FROM "bs_rel_user_role" existing
    WHERE existing.user_id = "bs_rel_user_role".user_id
  AND existing.role_id = (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_LABO_LOC' LIMIT 1)
    );

-- Insert BS_ROLE_PM_LABO_LOC  for users who don’t have it yet
INSERT INTO "bs_rel_user_role" ("user_id", "role_id")
SELECT u.id, r.id
FROM "bs_user" u
         CROSS JOIN (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_LABO_LOC' LIMIT 1) r
WHERE u.username IN (
                     'pm-ext-1111221', 'pm-ext-1111222', 'pm-ext-1111223', 'pm-ext-1111224',
                     'pm-ext-1111225', 'pm-ext-1111226', 'pm-ext-1111227', 'pm-ext-1111228',
                     'pm-ext-1111229', 'pm-ext-1111230', 'pm-ext-1111231', 'pm-ext-1111232',
                     'pm-ext-1111233', 'pm-ext-1111234', 'pm-ext-1111235', 'pm-ext-1111236',
                     'pm-ext-1111237', 'pm-ext-1111238', 'pm-ext-1111239', 'pm-ext-1111240',
                     'pm-ext-1111241', 'pm-ext-1111242', 'pm-ext-1111243', 'pm-ext-1111244',
                     'pm-ext-1111245', 'pm-ext-1111246', 'pm-ext-1111247', 'pm-ext-1111248',
                     'pm-ext-1111249', 'pm-ext-1111250', 'pm-ext-1111251'
    )
  AND NOT EXISTS (
    SELECT 1 FROM "bs_rel_user_role" existing
    WHERE existing.user_id = u.id
      AND existing.role_id = r.id
    );



INSERT INTO "bs_rel_user_role" ("user_id", "role_id")
SELECT u.id, r.id
FROM "bs_user" u
         CROSS JOIN (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1) r
WHERE u.username IN (
                     'pm-ext-1111221', 'pm-ext-1111222', 'pm-ext-1111223', 'pm-ext-1111224',
                     'pm-ext-1111225', 'pm-ext-1111226', 'pm-ext-1111227', 'pm-ext-1111228',
                     'pm-ext-1111229', 'pm-ext-1111230', 'pm-ext-1111231', 'pm-ext-1111232',
                     'pm-ext-1111233', 'pm-ext-1111234', 'pm-ext-1111235', 'pm-ext-1111236',
                     'pm-ext-1111237', 'pm-ext-1111238', 'pm-ext-1111239', 'pm-ext-1111240',
                     'pm-ext-1111241', 'pm-ext-1111242', 'pm-ext-1111243', 'pm-ext-1111244',
                     'pm-ext-1111245', 'pm-ext-1111246', 'pm-ext-1111247', 'pm-ext-1111248',
                     'pm-ext-1111249', 'pm-ext-1111250', 'pm-ext-1111251'
    )
  AND NOT EXISTS (
    SELECT 1 FROM "bs_rel_user_role" existing
    WHERE existing.user_id = u.id
      AND existing.role_id = r.id
    );
