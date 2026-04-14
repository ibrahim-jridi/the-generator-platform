UPDATE bs_role
SET role_type =
        CASE
            WHEN label = 'BS_ROLE_ADMIN' THEN 'INTERNE'
            WHEN label = 'BS_ROLE_DEFAULT_ROLE' THEN 'INTERNE'
            WHEN label = 'BS_ROLE_ECTD' THEN 'INTERNE'
            WHEN label = 'BS_ROLE_RESP_AGENCE_PROMOT' THEN 'PERSONNE_MORALE'
            WHEN label LIKE '%PM%' THEN 'PERSONNE_MORALE'
            WHEN label LIKE '%PP%' THEN 'PERSONNE_PHYSIQUE'
            WHEN label LIKE '%INT%' THEN 'INTERNE'
            ELSE role_type
        END;
