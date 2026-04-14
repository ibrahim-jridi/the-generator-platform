INSERT INTO "bs_user" ("id", "username", "email_verified", "keycloak_id", "email",
                       "is_active", "gender",
                       "first_name", "last_name", "address", "age", "birth_date", "national_id",
                       "country", "e_barid",
                       "profile_completed", "nationality", "phone_number", "created_by",
                       "created_date",
                       "last_modified_by", "last_modified_date", "version", "deleted", "user_type")
VALUES (gen_random_uuid(), 'ext-1111226', true ,gen_random_uuid(),'hayetAbidi@yopmail.com', true, 'FEMALE',
        'hayet', 'abidi',' ', '',NULL, '08561356', 'Tunisie', NULL, false, 'TUNISIAN',
        '','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1,
        false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111227', true ,gen_random_uuid(),'ilhemBechraouimeziane@yopmail.com',
        true, 'FEMALE', 'Ilhem', 'Bechraoui meziane',' ', '', NULL, '04799456', 'Tunisie', NULL, false, 'TUNISIAN',
        '', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111228', true ,gen_random_uuid(),'hajerBenromdhane@yopmail.com',
        true, 'FEMALE', 'Hajer', 'Ben romdhane',' ', '', NULL, '07358527', 'Tunisie', NULL, false, 'TUNISIAN',
        '', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111229', true ,gen_random_uuid(),'douiraMoadh@yopmail.com',
        true, 'MALE', 'DOUIRA', 'Moadh',' ', '', NULL, '07565026', 'Tunisie', NULL, false, 'TUNISIAN',
        '', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111230', true ,gen_random_uuid(),'drYosraslimane@yopmail.com',
        true, 'FEMALE', 'Yosra', 'slimane',' ', '', NULL, '09601511', 'Tunisie', NULL, false, 'TUNISIAN',
        '', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),


       (gen_random_uuid(), 'ext-1111231', true ,gen_random_uuid(),'akroutKhemaies@yopmail.com',
        true, 'MALE', 'AKROUT', 'Khemaies',' ', '', NULL, '05400251', 'Tunisie', NULL, false, 'TUNISIAN',
        '', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111232', true ,gen_random_uuid(),'mohamedRamzilebbi@yopmail.com',
        true, 'MALE', 'Mohamed Ramzi', 'lebbi',' ', '', NULL, '00772723', 'Tunisie', NULL, false, 'TUNISIAN',
        '', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111233', true ,gen_random_uuid(),'leilaJebnoun@yopmail.com',
        true, 'FEMALE', 'Leila', 'Jebnoun',' ', '', NULL, '07518179', 'Tunisie', NULL, false, 'TUNISIAN',
        '', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),


       (gen_random_uuid(), 'ext-1111234', true ,gen_random_uuid(),'nizarGhorbel@yopmail.com',
        true, 'MALE', 'Nizar', 'Ghorbel',' ', '', NULL, '05393237', 'Tunisie', NULL, false, 'TUNISIAN',
        '', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111235', true ,gen_random_uuid(),'madameHelakheirallah@yopmail.com',
        true, 'FEMALE', 'Hela', 'kheirallah',' ', '', NULL, '05311523', 'Tunisie', NULL, false, 'TUNISIAN',
        '', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111236', true ,gen_random_uuid(),'teyssirBenhouria@yopmail.com',
        true, 'FEMALE', 'Teyssir', 'Ben houria',' ', '', NULL, '08724278', 'Tunisie', NULL, false, 'TUNISIAN',
        '', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111237', true ,gen_random_uuid(),'takouaSnoussi@yopmail.com',
        true, 'FEMALE', 'TAKOUA', 'Snoussi',' ', '', NULL, '04737821', 'Tunisie', NULL, false, 'TUNISIAN',
        '', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111238', true ,gen_random_uuid(),'mohamedBassemourir@yopmail.com',
        true, 'MALE', 'Mohamed Bassem', 'ourir',' ', '', NULL, '09000293', 'Tunisie', NULL, false, 'TUNISIAN',
        '', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111239', true ,gen_random_uuid(),'wafaMezghannirekik@yopmail.com',
        true, 'FEMALE', 'Wafa', 'Mezghanni rekik',' ', '', NULL, '08417149', 'Tunisie', NULL, false, 'TUNISIAN',
        '', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),


       (gen_random_uuid(), 'ext-1111240', true ,gen_random_uuid(),'chokriBenbrahim@yopmail.com',
        true, 'MALE', 'Chokri', 'Ben brahim',' ', '', NULL, '06250530', 'Tunisie', NULL, false, 'TUNISIAN',
        '', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111241', true ,gen_random_uuid(),'saber.kallel@ipsen.com',
        true, 'MALE', 'Saber', 'Kallel',' ', '', NULL, '08409915', 'Tunisie', NULL, false, 'TUNISIAN',
        '', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111242', true ,gen_random_uuid(),'Khalil.allouche@pfizer.com',
        true, 'MALE', 'Mohamed Khalil', 'allouche',' ', '', NULL, '08305695', 'Tunisie', NULL, false, 'TUNISIAN',
        '98790250', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111243', true ,gen_random_uuid(),'marwa.souei@sanofi.com',
        true, 'FEMALE', 'Marwa', 'Souei',' ', '', NULL, '08718535', 'Tunisie', NULL, false, 'TUNISIAN',
        '50024828', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111244', true ,gen_random_uuid(),'ceo.pharma@3spharma.org',
        true, 'MALE', 'Sami', 'Boukattaya',' ', '', NULL, '01286133', 'Tunisie', NULL, false, 'TUNISIAN',
        '58581061', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111245', true ,gen_random_uuid(),'benzaiedasma@gmail.com',
        true, 'FEMALE', 'Asma', 'Ben zaied',' ', '', NULL, '06490267', 'Tunisie', NULL, false, 'TUNISIAN',
        '58342373', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111246', true ,gen_random_uuid(),'reglementaire.promopharma@gmail.com',
        true, 'FEMALE', 'Ibtissem', 'Jelassi',' ', '', NULL, '07154086', 'Tunisie', NULL, false, 'TUNISIAN',
        '29416321', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111247', true ,gen_random_uuid(),'newine.bensalem@abbvie.com',
        true, 'FEMALE', 'Newine', 'Ben salem',' ', '', NULL, '00748343', 'Tunisie', NULL, false, 'TUNISIAN',
        '29134374', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111248', true ,gen_random_uuid(),'khaled.laouiti@pierre-fabre.com',
        true, 'MALE', 'KHALED', 'Laouiti',' ', '', NULL, '05443713', 'Tunisie', NULL, false, 'TUNISIAN',
        '71161406', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111249', true ,gen_random_uuid(),'khaoula.jebabli@atudipp.com.tn',
        true, 'FEMALE', 'KHAOULA', 'Jebabli',' ', '', NULL, '08072723', 'Tunisie', NULL, false, 'TUNISIAN',
        '98759232', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111250', true ,gen_random_uuid(),'cyrine.ferchiou@dahabpharma.com.tn',
        true, 'FEMALE', 'CYRINE', 'Ferchiou',' ', '', NULL, '08739701', 'Tunisie', NULL, false, 'TUNISIAN',
        '98759208', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111251', true ,gen_random_uuid(),'imene.ben_abdallah@roche.com',
        true, 'FEMALE', 'Imene', 'Ben abdallah',' ', '', NULL, '07380270', 'Tunisie', NULL, false, 'TUNISIAN',
        '93084301', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111252', true ,gen_random_uuid(),'gps.pharma@yahoo.com',
        true, 'MALE', 'Néji', 'Bennasr',' ', '', NULL, '00245237', 'Tunisie', NULL, false, 'TUNISIAN',
        '52338995', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111253', true ,gen_random_uuid(),'nesrine.bensaad@astrazeneca.com',
        true, 'FEMALE', 'Nesrine', 'Ben saad',' ', '', NULL, '08488545', 'Tunisie', NULL, false, 'TUNISIAN',
        '29137387', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111254', true ,gen_random_uuid(),'riad.aded@mab-tu.com',
        true, 'MALE', 'Riad', 'Aded',' ', '', NULL, '07462949', 'Tunisie', NULL, false, 'TUNISIAN',
        '98314614', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111255', true ,gen_random_uuid(),'hosni.bouborma@orange.tn',
        true, 'MALE', 'HOSNI', 'Bouborma',' ', '', NULL, '05186563', 'Tunisie', NULL, false, 'TUNISIAN',
        '26328318', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111256', true ,gen_random_uuid(),'wassim.feki@advanspharma.com',
        true, 'MALE', 'Wassim', 'Feki',' ', '', NULL, '08632690', 'Tunisie', NULL, false, 'TUNISIAN',
        '28803434', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111257', true ,gen_random_uuid(),'mehdi.bouhlel@mcpharma.com.tn',
        true, 'MALE', 'Mehdi', 'Bouhlel',' ', '', NULL, '09606488', 'Tunisie', NULL, false, 'TUNISIAN',
        '98765305', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111258', true ,gen_random_uuid(),'mea@pharmis.com',
        true, 'FEMALE', 'Fatma', 'Trabelsi',' ', '', NULL, '07452874', 'Tunisie', NULL, false, 'TUNISIAN',
        '26676179', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111259', true ,gen_random_uuid(),'semia.belhadj-hassen@servier.com',
        true, 'FEMALE', 'Semia', 'Znazen ep belhaj hassen',' ', '', NULL, '02040419', 'Tunisie', NULL, false, 'TUNISIAN',
        '23328732', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111260', true ,gen_random_uuid(),'tarek.elbiche@gmail.com',
        true, 'MALE', 'Tarek', 'El biche',' ', '', NULL, '03344068', 'Tunisie', NULL, false, 'TUNISIAN',
        '58 326 118', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111261', true ,gen_random_uuid(),'sihem.omrani@medicis.tn',
        true, 'FEMALE', 'sihem', 'Omrani',' ', '', NULL, '04668105', 'Tunisie', NULL, false, 'TUNISIAN',
        '', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111262', true ,gen_random_uuid(),'nabil.drira@ceva.com',
        true, 'MALE', 'nabil', 'Drira',' ', '', NULL, '05281856', 'Tunisie', NULL, false, 'TUNISIAN',
        '', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),


       (gen_random_uuid(), 'ext-1111263', true ,gen_random_uuid(),'chiheb_achour@yahoo.fr',
        true, 'MALE', 'Chiheb', 'Achour',' ', '', NULL, '00783699', 'Tunisie', NULL, false, 'TUNISIAN',
        '', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111264', true ,gen_random_uuid(),'ict.promotion@planet.tn',
        true, 'FEMALE', 'Dorra', 'Hizaoui',' ', '', NULL, '00117815', 'Tunisie', NULL, false, 'TUNISIAN',
        '', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111265', true ,gen_random_uuid(),'syvamed@syvamed.com',
        true, 'MALE', 'ABDESSATTAR', 'Mabrouk',' ', '', NULL, '03976079', 'Tunisie', NULL, false, 'TUNISIAN',
        '', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111266', true ,gen_random_uuid(),'fadhelou@yahoo.fr',
        true, 'MALE', 'Fadhel', 'Ouamara',' ', '', NULL, '00208706', 'Tunisie', NULL, false, 'TUNISIAN',
        '', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111267', true ,gen_random_uuid(),'asma.h@biotechpharmamd.com',
        true, 'FEMALE', 'Asma', 'HADDAD',' ', '', NULL, '06416617', 'Tunisie', NULL, false, 'TUNISIAN',
        '', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111268', true ,gen_random_uuid(),'hiba@mediterx.com',
        true, 'FEMALE', 'hiba', 'Ben alaya',' ', '', NULL, '09858050', 'Tunisie', NULL, false, 'TUNISIAN',
        '', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111269', true ,gen_random_uuid(),'ict.promotion@planet.tn',
        true, 'MALE', 'Hamed', 'LITAIEM',' ', '', NULL, '04791876', 'Tunisie', NULL, false, 'TUNISIAN',
        '26961807', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111270', true ,gen_random_uuid(),'medinisalma@gmail.com',
        true, 'FEMALE', 'Salma ', 'Medini',' ', '', NULL, '07402278', 'Tunisie', NULL, false, 'TUNISIAN',
        '52371544', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111271', true ,gen_random_uuid(),'liliaFki@yopmail.com',
        true, 'FEMALE', 'Lilia', 'fki',' ', '', NULL, '08410476', 'Tunisie', NULL, false, 'TUNISIAN',
        '', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111272', true ,gen_random_uuid(),'achraafBenAbdallah@yopmail.com',
        true, 'MALE', 'Achraaf', 'Ben Abdallah',' ', '', NULL, '06418431', 'Tunisie', NULL, false, 'TUNISIAN',
        '', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        1, false, 'PHYSICAL'),

        (gen_random_uuid(), 'ext-1111273', true, gen_random_uuid(), 'eyaHajahmed@yopmail.com', true,
        'FEMALE', 'Eya', 'Haj Ahmed', ' ', '', NULL, '09838865', 'Tunisie', NULL, false, 'TUNISIAN',
        '', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e',
        NOW(), 1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111274', true, gen_random_uuid(), 'omarChoura@yopmail.com', true,
        'MALE', 'Omar', 'Choura', ' ', '', NULL, '08337995', 'Tunisie', NULL, false, 'TUNISIAN', '',
        'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e',
        NOW(), 1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111275', true, gen_random_uuid(), 'stounsi@its.jnj.com', true,
        'FEMALE', 'Salma', 'Tounsi', ' ', '', NULL, '05459379', 'Tunisie', NULL, false, 'TUNISIAN',
        '54491565', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111276', true, gen_random_uuid(), 'mongi.maamar@biomaghreb.com',
        true, 'MALE', 'Mongi', 'Maamar', ' ', '', NULL, '01460011', 'Tunisie', NULL, false,
        'TUNISIAN', '58515222', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111277', true, gen_random_uuid(),
        'raouf.ben.othmen@external.merckgroup.com', true, 'MALE', 'Ben Othman', 'Mohamed Raouf',
        ' ', '', NULL, '01617360', 'Tunisie', NULL, false, 'TUNISIAN', '98707122',
        'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e',
        NOW(), 1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111278', true, gen_random_uuid(), 'zeineb.hachicha@abbott.com',
        true, 'FEMALE', 'Hachicha', 'Zeineb', ' ', '', NULL, '07194373', 'Tunisie', NULL, false,
        'TUNISIAN', '98435545', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),

        (gen_random_uuid(), 'ext-1111279', true, gen_random_uuid(),
        'zina.kobbi@arema-international.com', true, 'FEMALE', 'Zina', 'Kobbi', ' ', '', NULL,
        '06343229', 'Tunisie', NULL, false, 'TUNISIAN', '22327117',
        'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e',
        NOW(), 1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111280', true, gen_random_uuid(), 'f.kallel@gmail.com', true,
        'MALE', 'Farouk', 'Kallel', ' ', '', NULL, '543013', 'Tunisie', NULL, false, 'TUNISIAN',
        '98304058', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111281', true, gen_random_uuid(), 'macherif@hikma.com', true,
        'MALE', 'Cherif', 'Malek', ' ', '', NULL, '09628979', 'Tunisie', NULL, false, 'TUNISIAN',
        '29479730', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111282', true, gen_random_uuid(), 'naila.dridi@julphar.net', true,
        'FEMALE', 'Neila', 'Dridi', ' ', '', NULL, '08303831', 'Tunisie', NULL, false, 'TUNISIAN',
        '94784609', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111283', true, gen_random_uuid(), 'test@yopmail.com', true,
        'FEMALE', 'test', 'test', ' ', '', NULL, 'P123456', 'afghan', NULL, false, 'AFGHAN',
        '25926999', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),

       (gen_random_uuid(), 'ext-1111284', true, gen_random_uuid(), 'liliafki@pharmare.tn', true,
        'FEMALE', 'Lilia', 'Feki', ' ', '', NULL, '08410467', 'Tunisie', NULL, false, 'TUNISIAN',
        '99745051', 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL');










