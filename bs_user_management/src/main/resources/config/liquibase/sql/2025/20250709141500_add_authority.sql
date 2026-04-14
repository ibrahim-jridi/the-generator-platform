INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date", "last_modified_by", "last_modified_date", "version", "first_insert", "deleted")
VALUES
    (gen_random_uuid(), 'BS_TRACK_REQUESTS', TRUE, 'BS_TRACK_REQUESTS', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, TRUE, FALSE);


INSERT INTO "bs_rel_role_authority" ("role_id", "authority_id")
SELECT r.id, a.id
FROM bs_role r
JOIN bs_authority a ON a.label = 'BS_TRACK_REQUESTS'
WHERE r.label IN ('BS_ROLE_PP_PHARMACIEN', 'BS_ROLE_PP_MEDECIN', 'BS_ROLE_PP_MEDECIN_DENTISTE','BS_ROLE_PP_MEDECIN_VETERINAIRE','BS_ROLE_PP_DEFAULT')
AND NOT EXISTS (
    SELECT 1
    FROM bs_rel_role_authority bra
    WHERE bra.role_id = r.id AND bra.authority_id = a.id
);
