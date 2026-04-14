
-- Delete duplicated roles
DELETE FROM "bs_rel_role_authority" WHERE role_id = (SELECT id FROM bs_role WHERE label = 'BS_PM_CRO' );
DELETE FROM "bs_rel_role_authority" WHERE role_id = (SELECT id FROM bs_role WHERE label = 'BS_PM_AGENCE_PROMOT' );


DELETE FROM bs_role WHERE label = 'BS_PM_CRO';
DELETE FROM bs_role WHERE label = 'BS_PM_AGENCE_PROMOT';

