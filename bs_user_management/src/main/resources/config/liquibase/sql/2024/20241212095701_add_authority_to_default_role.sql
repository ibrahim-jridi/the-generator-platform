INSERT INTO "bs_rel_role_authority" ("role_id", "authority_id")
VALUES ('242a7856-1286-49c8-bf3b-45abfc94699f',
        (SELECT id FROM bs_authority WHERE label = 'BS_TASKS'));
