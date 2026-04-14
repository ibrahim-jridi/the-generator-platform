UPDATE bs_role
SET label = REPLACE(label, 'BS-PM', 'BS-ROLE')
WHERE label IN (
                'BS-PM-SERVICE-INSPECTION',
                'BS-PM-SERVICE-INDUSTRIE',
                'BS-PM-SERVICE-VETERINAIRE',
                'BS-PM-MINISTERE-DE-LA-SANTE',
                'BS-PM-MINISTERE-DE-LAGRICULTURE',
                'BS-PM-COMMISSION-INDUSTRIE',
                'BS-PM-COMMISSION-VETERINAIRE',
                'BS-PM-BUREAU-DORDRE',
                'BS-PM-CHARGE-DES-AGENCES-DE-PROMOTION',
                'BS-PM-CONSEIL-DE-LORDRE',
                'BS-PM-RESPONSABLE-DES-ESSAIS-CLINIQUES',
                'BS-PM-DIRECTEUR-DE-LINSPECTION',
                'BS-PM-DIRECTEUR-DE-LANMPS'
    );


