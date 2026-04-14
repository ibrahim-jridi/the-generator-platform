INSERT INTO "bs_rel_role_authority" ("role_id", "authority_id")
VALUES ((SELECT id FROM bs_role WHERE label = 'BS_ROLE_PP_PHARMACIEN'),
        (SELECT id FROM bs_authority WHERE label = 'BS_CREATE_WHOLESALER'));
