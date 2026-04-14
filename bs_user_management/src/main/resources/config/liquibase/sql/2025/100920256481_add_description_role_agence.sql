UPDATE "bs_role"
SET description = 'Responsable d’agence de promotion',
    last_modified_date = NOW()
WHERE label = 'BS_ROLE_PM_RESP_AGE_PROM';

UPDATE "bs_role"
SET description = 'Gérant d’agence de promotion',
    last_modified_date = NOW()
WHERE label = 'BS_ROLE_PM_GERANT_AGE_PROM';

UPDATE "bs_role"
SET description = 'Agence de promotion',
    last_modified_date = NOW()
WHERE label = 'BS_ROLE_PM_AGENCE_PROMOTION';
