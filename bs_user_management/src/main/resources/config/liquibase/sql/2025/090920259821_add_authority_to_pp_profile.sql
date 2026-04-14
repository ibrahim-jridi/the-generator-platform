INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date", "last_modified_by", "last_modified_date", "version", "first_insert", "deleted")
VALUES
    (gen_random_uuid(), 'BS_VIEW_DECLARATION_INTERESTS', TRUE, 'BS_VIEW_DECLARATION_INTERESTS', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, TRUE, FALSE);

INSERT INTO "bs_rel_role_authority" ("role_id", "authority_id")
VALUES
    ((SELECT id FROM "bs_role" WHERE "label" = 'BS_ROLE_PP_DEFAULT' LIMIT 1), (SELECT id FROM "bs_authority" WHERE "label" = 'BS_CREATE_PROMOT_AGENCY' LIMIT 1));
