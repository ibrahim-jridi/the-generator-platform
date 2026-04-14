INSERT INTO "bs_role"("id", "label", "description", "is_active", "created_by", "created_date",
                      "last_modified_by", "last_modified_date", "version", "deleted")
VALUES
(gen_random_uuid(), 'BS-PM-SERVICE-INSPECTION', 'Service Inspection', TRUE, 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1,  FALSE),
(gen_random_uuid(), 'BS-PM-SERVICE-INDUSTRIE', 'Service Industrie', TRUE, 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, FALSE),
(gen_random_uuid(), 'BS-PM-SERVICE-VETERINAIRE', 'Service vétérinaire', TRUE,'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, FALSE),
(gen_random_uuid(), 'BS-PM-MINISTERE-DE-LA-SANTE', 'Ministère de la santé', TRUE, 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, FALSE),
(gen_random_uuid(), 'BS-PM-MINISTERE-DE-LAGRICULTURE', 'Ministère de l’agriculture', TRUE,'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, FALSE),
(gen_random_uuid(), 'BS-PM-COMMISSION-INDUSTRIE', 'Commission industrie', TRUE,'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, FALSE),
(gen_random_uuid(), 'BS-PM-COMMISSION-VETERINAIRE', 'Commission vétérinaire', TRUE,'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, FALSE),
(gen_random_uuid(), 'BS-PM-BUREAU-DORDRE', 'Bureau d’ordre', TRUE,'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, FALSE),
(gen_random_uuid(), 'BS-PM-CHARGE-DES-AGENCES-DE-PROMOTION', 'Chargé des agences de promotion', TRUE,'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, FALSE),
(gen_random_uuid(), 'BS-PM-CONSEIL-DE-LORDRE', 'Conseil de l’ordre ', TRUE,'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, FALSE),
(gen_random_uuid(), 'BS-PM-RESPONSABLE-DES-ESSAIS-CLINIQUES', 'Responsable des essais cliniques', TRUE,'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, FALSE),
(gen_random_uuid(), 'BS-PM-DIRECTEUR-DE-LINSPECTION', 'Directeur de l’inspection', TRUE,'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, FALSE),
(gen_random_uuid(), 'BS-PM-DIRECTEUR-DE-LANMPS', 'Directeur de l’ANMPS', TRUE,'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, FALSE);





INSERT INTO "bs_rel_role_authority" ("role_id", "authority_id")
VALUES
(  (SELECT id FROM bs_role WHERE label = 'BS-PM-SERVICE-INSPECTION' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_TASKS' LIMIT 1)),
(  (SELECT id FROM bs_role WHERE label = 'BS-PM-SERVICE-INSPECTION' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_USER' LIMIT 1)),
(  (SELECT id FROM bs_role WHERE label= 'BS-PM-SERVICE-INDUSTRIE' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_TASKS' LIMIT 1)),
(  (SELECT id FROM bs_role WHERE label = 'BS-PM-SERVICE-INDUSTRIE' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_USER' LIMIT 1)),
(  (SELECT id FROM bs_role WHERE label = 'BS-PM-SERVICE-VETERINAIRE' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_TASKS' LIMIT 1)),
(  (SELECT id FROM bs_role WHERE label = 'BS-PM-SERVICE-VETERINAIRE' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_USER' LIMIT 1)),
(  (SELECT id FROM bs_role WHERE label = 'BS-PM-MINISTERE-DE-LA-SANTE' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_TASKS' LIMIT 1)),
(  (SELECT id FROM bs_role WHERE label = 'BS-PM-MINISTERE-DE-LA-SANTE' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_USER' LIMIT 1)),
(  (SELECT id FROM bs_role WHERE label = 'BS-PM-MINISTERE-DE-LAGRICULTURE' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_TASKS' LIMIT 1)),
(  (SELECT id FROM bs_role WHERE label = 'BS-PM-MINISTERE-DE-LAGRICULTURE' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_USER' LIMIT 1)),
(  (SELECT id FROM bs_role WHERE label = 'BS-PM-COMMISSION-INDUSTRIE' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_TASKS' LIMIT 1)),
(  (SELECT id FROM bs_role WHERE label = 'BS-PM-COMMISSION-INDUSTRIE' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_USER' LIMIT 1)),
(  (SELECT id FROM bs_role WHERE label = 'BS-PM-COMMISSION-VETERINAIRE' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_TASKS' LIMIT 1)),
(  (SELECT id FROM bs_role WHERE label = 'BS-PM-COMMISSION-VETERINAIRE' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_USER' LIMIT 1)),
(  (SELECT id FROM bs_role WHERE label = 'BS-PM-BUREAU-DORDRE' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_TASKS' LIMIT 1)),
(  (SELECT id FROM bs_role WHERE label = 'BS-PM-BUREAU-DORDRE' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_USER' LIMIT 1)),
(  (SELECT id FROM bs_role WHERE label = 'BS-PM-CHARGE-DES-AGENCES-DE-PROMOTION' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_TASKS' LIMIT 1)),
(  (SELECT id FROM bs_role WHERE label = 'BS-PM-CHARGE-DES-AGENCES-DE-PROMOTION' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_USER' LIMIT 1)),
(  (SELECT id FROM bs_role WHERE label = 'BS-PM-CONSEIL-DE-LORDRE' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_TASKS' LIMIT 1)),
(  (SELECT id FROM bs_role WHERE label = 'BS-PM-CONSEIL-DE-LORDRE' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_USER' LIMIT 1)),
(  (SELECT id FROM bs_role WHERE label = 'BS-PM-RESPONSABLE-DES-ESSAIS-CLINIQUES' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_TASKS' LIMIT 1)),
(  (SELECT id FROM bs_role WHERE label = 'BS-PM-RESPONSABLE-DES-ESSAIS-CLINIQUES' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_USER' LIMIT 1)),
(  (SELECT id FROM bs_role WHERE label = 'BS-PM-DIRECTEUR-DE-LINSPECTION' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_TASKS' LIMIT 1)),
(  (SELECT id FROM bs_role WHERE label = 'BS-PM-DIRECTEUR-DE-LINSPECTION' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_USER' LIMIT 1)),
(  (SELECT id FROM bs_role WHERE label = 'BS-PM-DIRECTEUR-DE-LANMPS' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_TASKS' LIMIT 1)),
(  (SELECT id FROM bs_role WHERE label = 'BS-PM-DIRECTEUR-DE-LANMPS' LIMIT 1),(SELECT id FROM bs_authority WHERE label = 'BS_USER' LIMIT 1));



