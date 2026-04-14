INSERT INTO "bs_rel_role_authority" ("role_id", "authority_id")
SELECT r.id, a.id
FROM bs_role r
JOIN bs_authority a ON a.label = 'BS_UPDATE_KEYCLOAK_USER'
WHERE r.label IN ('BS_ROLE_PM_CRO', 'BS_ROLE_PM_AGENCE_PROMOTION', 'BS_ROLE_PM_INDUSTRIEL')
AND NOT EXISTS (
    SELECT 1
    FROM bs_rel_role_authority bra
    WHERE bra.role_id = r.id AND bra.authority_id = a.id
);
