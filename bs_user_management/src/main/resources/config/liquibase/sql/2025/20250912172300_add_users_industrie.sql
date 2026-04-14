
-- Add PP-Users

INSERT INTO "bs_user" ("id", "username", "email_verified", "keycloak_id", "email",
                       "is_active", "gender",
                       "first_name", "last_name", "address", "age", "birth_date", "national_id",
                       "country", "e_barid",
                       "profile_completed", "nationality", "phone_number", "created_by",

                       "created_date",
                       "last_modified_by", "last_modified_date", "version", "deleted", "user_type")
VALUES (gen_random_uuid(), 'ext-1111285', true ,gen_random_uuid(),'sonia.benfarhat@sanofi.com', true, 'FEMALE',
        'sonia', 'benfarhat',' 10 addressee ، 1003', '99','1988-04-01', '02995660', 'Tunisie', NULL, NULL, 'TUNISIAN',
        '58511112','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1,
        false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111286', true, gen_random_uuid(),'imeneEssid@yopmail.com', true, 'FEMALE',
        'Imene', 'Essid',' 10 addressee ، 1003', '99','1988-04-01', '06137565', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000001','c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111287', true,gen_random_uuid(),'meriamDridi@yopmail.com', true, 'FEMALE',
        'Meriam', 'Dridi',' 10 addressee ، 1003', '99','1988-04-01', '04830430', 'Tunisie', NULL, NULL, 'TUNISIAN',
        '41000002','c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111288', true,gen_random_uuid(),'basmaMassoudi@yopmail.com',true, 'FEMALE',
        'Basma', 'Massoudi ',' 10 addressee ، 1003', '99','1988-04-01', '06573312', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000003','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111289', true,gen_random_uuid(),'marwaSouei@yopmail.com',true, 'FEMALE',
        'Marwa', 'Souei',' 10 addressee ، 1003', '99','1988-04-01', '08718535', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000004','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111290', true,gen_random_uuid(),'mohamed.gara@tunivet.com',true, 'MALE',
        'Mohamed', 'Gara',' 10 addressee ، 1003', '99','1988-04-01', '00547924', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000005','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111291', true,gen_random_uuid(),'amelBenSlimene@yopmail.com',true, 'FEMALE',
        'Amel', 'Ben slimene',' 10 addressee ، 1003', '99','1988-04-01', '08718535', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000006','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111292', true,gen_random_uuid(),'jihene.abdellatif@rns.tn',true, 'FEMALE',
        'Jihene', 'Abdellatif',' 10 addressee ، 1003', '99','1988-04-01', '08888888', 'Tunisie', NULL, NULL,
        'TUNISIAN', '98751916','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111293', true,gen_random_uuid(),'ines.maati@sanofi.com',true, 'FEMALE',
        'Ines', 'Maati',' 10 addressee ، 1003', '99','1988-04-01', '06435219', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000007','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111294', true,gen_random_uuid(),'marwaSouei@yopmail.com',true, 'FEMALE',
        'Monia', 'Daoues',' 10 addressee ، 1003', '99','1988-04-01', '06435219', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000008','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
      (gen_random_uuid(), 'ext-1111295', true,gen_random_uuid(),'meriamDridi@yopmail.com',true, 'FEMALE',
        'Sinda  ', 'Kaouech',' 10 addressee ، 1003', '99','1988-04-01', '08732507', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000009','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111296', true,gen_random_uuid(),'dorraIdir@yopmail.com',true, 'FEMALE',
        'Dorra  ', 'Idir',' 10 addressee ، 1003', '99','1988-04-01', '08317859', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000009','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111297', true,gen_random_uuid(),'Prd@pharmaderm.net',true, 'MALE',
        'Taher  ', 'El ghali',' 10 addressee ، 1003', '99','1988-04-01', '01537298', 'Tunisie', NULL, NULL,
        'TUNISIAN', '52104512','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111298', true,gen_random_uuid(),'selimaElghali@yopmail.com',true, 'FEMALE',
        'Selima  ', 'El ghali',' 10 addressee ، 1003', '99','1988-04-01', '07351587', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000010','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111299', true,gen_random_uuid(),'selimIddir@yopmail.com',true, 'MALE',
        'Selim  ', 'Iddir ',' 10 addressee ، 1003', '99','1988-04-01', '09011151', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000011','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111300', true,gen_random_uuid(),'imenSouelmia@yopmail.com',true, 'FEMALE',
        'Imen  ', 'Souelmia',' 10 addressee ، 1003', '99','1988-04-01', '08059757', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000012','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111301', true,gen_random_uuid(),'ferid.abdellatif@pfizer.com',true, 'MALE',
        'Abdellatif', 'Ferid',' 10 addressee ، 1003', '99','1988-04-01', '04764407', 'Tunisie', NULL, NULL,
        'TUNISIAN', '98790205','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111302', true,gen_random_uuid(),'takwaFehri@yopmail.com',true, 'FEMALE',
        'Takwa', 'Fehri',' 10 addressee ، 1003', '99','1988-04-01', '09008952', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000013','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111303', true,gen_random_uuid(),'walidDahdouh@yopmail.com',true, 'MALE',
        'Walid', 'Dahdouh',' 10 addressee ، 1003', '99','1988-04-01', '07430140', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000013','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111304', true,gen_random_uuid(),'jalilaJedid@yopmail.com',true, 'FEMALE',
        'Jalila', 'Jedidi',' 10 addressee ، 1003', '99','1988-04-01', '06399763', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000014','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111305', true,gen_random_uuid(),'Mohamedamine.boujbel@medis.com.tn',true, 'MALE',
        'Mohamed amine', 'Boujbel',' 10 addressee ، 1003', '99','1988-04-01', '09601649', 'Tunisie', NULL, NULL,
        'TUNISIAN', '22553330','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111306', true,gen_random_uuid(),'ferielMrabet@yopmail.com',true, 'FEMALE',
        'Feriel', 'Mrabet',' 10 addressee ، 1003', '99','1988-04-01', '06384853', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000015','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111307', true,gen_random_uuid(),'yasmineKammoun@yopmail.com',true, 'FEMALE',
        'Yasmine', 'Kammoun',' 10 addressee ، 1003', '99','1988-04-01', '08746280', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000016','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111308', true,gen_random_uuid(),'mohamedBouchoucha@yopmail.com',true, 'MALE',
        'Mohamed', 'Bouchoucha',' 10 addressee ، 1003', '99','1988-04-01', '07334004', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000017','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111309', true,gen_random_uuid(),'mahmoudAlswisi@yopmail.com',true, 'MALE',
        'Mahmoud', 'Alswisi',' 10 addressee ، 1003', '99','1988-04-01', 'T1293629', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000018','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111310', true,gen_random_uuid(),'karim.alaya@labomedis.com',true, 'MALE',
        'Karim', 'Alaya',' 10 addressee ، 1003', '99','1988-04-01', '03754109', 'Tunisie', NULL, NULL,
        'TUNISIAN', '98777290','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111311', true,gen_random_uuid(),'rahmaElbey@yopmail.com',true, 'MALE',
        'Rahma', 'El bey',' 10 addressee ، 1003', '99','1988-04-01', '06444252', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000019','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111312', true,gen_random_uuid(),'zeinebLadhib@yopmail.com',true, 'FEMALE',
        'Zeineb', 'Ladhib',' 10 addressee ، 1003', '99','1988-04-01', '06465409', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000020','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111313', true,gen_random_uuid(),'dhekra.j@tahapharma.com',true, 'FEMALE',
        'Dhekra', 'Ben Jaber',' 10 addressee ، 1003', '99','1988-04-01', '09761515', 'Tunisie', NULL, NULL,
        'TUNISIAN', '99315411','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111314', true,gen_random_uuid(),'wiemKhalil@yopmail.com',true, 'FEMALE',
        'Wiem', 'Khalil',' 10 addressee ، 1003', '99','1988-04-01', '13237459', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000021','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111315', true,gen_random_uuid(),'imenKoubaa@yopmail.com',true, 'FEMALE',
        'Imen', 'Koubaa',' 10 addressee ، 1003', '99','1988-04-01', '08835665', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000022','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111316', true,gen_random_uuid(),'nizar.laabidi@pasteur.rns.tn',true, 'MALE',
        'Nizar', 'Laabidi',' 10 addressee ، 1003', '99','1988-04-01', '06385160', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000023','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111317', true,gen_random_uuid(),'sanaMasmoudi@yopmail.com',true, 'FEMALE',
        'Sana', 'Masmoudi',' 10 addressee ، 1003', '99','1988-04-01', '05362549', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000024','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111318', true,gen_random_uuid(),'prt@galienpharma.tn',true, 'MALE',
        'Bilel', 'Khedir',' 10 addressee ، 1003', '99','1988-04-01', '10000018', 'Tunisie', NULL, NULL,
        'TUNISIAN', '50728051','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111319', true,gen_random_uuid(),'tlili.med.samy@gmail.com',true, 'MALE',
        'Mohamed sami', 'Tlili',' 10 addressee ، 1003', '99','1988-04-01', '07546163', 'Tunisie', NULL, NULL,
        'TUNISIAN', '99977045','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111320', true,gen_random_uuid(),'marwenKali@yopmail.com',true, 'MALE',
        'Marwen', 'Kali',' 10 addressee ، 1003', '99','1988-04-01', '09491101', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000025','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111321', true,gen_random_uuid(),'ayaMiled@yopmail.com',true, 'FEMALE',
        'Aya', 'Miled',' 10 addressee ، 1003', '99','1988-04-01', '10814043', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000026','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111322', true,gen_random_uuid(),'asma.ouani@advanspharma.com',true, 'FEMALE',
        'Asma', 'Aouani',' 10 addressee ، 1003', '99','1988-04-01', '06418076', 'Tunisie', NULL, NULL,
        'TUNISIAN', '95185655','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111323', true,gen_random_uuid(),'safaNouri@yopmail.com',true, 'FEMALE',
        'Safa', 'Nouri',' 10 addressee ، 1003', '99','1988-04-01', '14253957', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000027','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111324', true,gen_random_uuid(),'mariemRakrouki@yopmail.com',true, 'FEMALE',
        'Mariem', 'Rakrouki',' 10 addressee ، 1003', '99','1988-04-01', '07958228', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000028','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111325', true,gen_random_uuid(),'madihaAbid@yopmail.com',true, 'FEMALE',
        'Madiha', 'Abid',' 10 addressee ، 1003', '99','1988-04-01', '05486139', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000029','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111326', true,gen_random_uuid(),'k.ennouichi@adwya.com.tn',true, 'MALE',
        'Kéfi elkhansa', 'Ennouichi',' 10 addressee ، 1003', '99','1988-04-01', '00793126', 'Tunisie', NULL, NULL,
        'TUNISIAN', '28980633','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111327', true,gen_random_uuid(),'achrefChbichib@yopmail.com',true, 'MALE',
        'Achref', 'Chbichib',' 10 addressee ، 1003', '99','1988-04-01', '07203132', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000030','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111328', true,gen_random_uuid(),'zahraYosri@yopmail.com',true, 'FEMALE',
        'Zahra', 'Yosri',' 10 addressee ، 1003', '99','1988-04-01', '06958110', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000031','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL'),
       (gen_random_uuid(), 'ext-1111329', true,gen_random_uuid(),'feresMessaoud@yopmail.com',true, 'FEMALE',
        'Fares', 'Messaoud',' 10 addressee ، 1003', '99','1988-04-01', '09413746', 'Tunisie', NULL, NULL,
        'TUNISIAN', '41000032','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, false, 'PHYSICAL');


-- Add PM-Users

INSERT INTO "bs_user" ("id", "username", "email_verified", "keycloak_id", "email",
                       "is_active",
                       "social_reason", "address", "age", "tax_registration",
                       "country", "e_barid",
                       "profile_completed", "nationality", "phone_number", "created_by",
                       "created_date",
                       "last_modified_by", "last_modified_date", "version", "deleted", "user_type")
VALUES (gen_random_uuid(), 'pm-ext-1112385', true ,gen_random_uuid(),'winthropPharmaTunisie@yopmail.com', true,
        'WINTHROP PHARMA TUNISIE',' ', '', '0296179R', 'Tunisie', NULL, true, 'TUNISIAN',
        '','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1,
        false, 'COMPANY'),
       (gen_random_uuid(), 'pm-ext-1112386', true ,gen_random_uuid(),'TUNIVET@yopmail.com', true,
        'TUNIVET',' ', '', '1032105R', 'Tunisie', NULL, true, 'TUNISIAN',
        '','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1,
        false, 'COMPANY'),
       (gen_random_uuid(), 'pm-ext-1112387', true ,gen_random_uuid(),'SIPHAT@yopmail.com', true,
        'SIPHAT',' ', '', '0029999J', 'Tunisie', NULL, true, 'TUNISIAN',
        '','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1,
        false, 'COMPANY'),
       (gen_random_uuid(), 'pm-ext-1112388', true ,gen_random_uuid(),'SANOFIAVENTISPHARMATUNISIE@yopmail.com', true,
        'SANOFI AVENTIS PHARMA TUNISIE',' ', '', '0719175P', 'Tunisie', NULL, true, 'TUNISIAN',
        '','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1,
        false, 'COMPANY'),
       (gen_random_uuid(), 'pm-ext-1112389', true ,gen_random_uuid(),'PHARMADERM@yopmail.com', true,
        'PHARMADERM',' ', '', '0024408A', 'Tunisie', NULL, true, 'TUNISIAN',
        '','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1,
        false, 'COMPANY'),
       (gen_random_uuid(), 'pm-ext-1112390', true ,gen_random_uuid(),'PFIZER@yopmail.com', true,
        'PFIZER',' ', '', '0632916C', 'Tunisie', NULL, true, 'TUNISIAN',
        '','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1,
        false, 'COMPANY'),
       (gen_random_uuid(), 'pm-ext-1112391', true ,gen_random_uuid(),'NEAPOLIS@yopmail.com', true,
        'NEAPOLIS',' ', '', '1076074R', 'Tunisie', NULL, true, 'TUNISIAN',
        '','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1,
        false, 'COMPANY'),
       (gen_random_uuid(), 'pm-ext-1112392', true ,gen_random_uuid(),'MEDIS@yopmail.com', true,
        'MEDIS',' ', '', '0539219G', 'Tunisie', NULL, true, 'TUNISIAN',
        '','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1,
        false, 'COMPANY'),
       (gen_random_uuid(), 'pm-ext-1112393', true ,gen_random_uuid(),'LINOPHARMA@yopmail.com', true,
        'LINO PHARMA',' ', '', '1465602K', 'Tunisie', NULL, true, 'TUNISIAN',
        '','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1,
        false, 'COMPANY'),
       (gen_random_uuid(), 'pm-ext-1112394', true ,gen_random_uuid(),'INSTITUTPASTEUR@yopmail.com', true,
        'INSTITUT PASTEUR',' ', '', '0030498H', 'Tunisie', NULL, true, 'TUNISIAN',
        '','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1,
        false, 'COMPANY'),
       (gen_random_uuid(), 'pm-ext-1112395', true ,gen_random_uuid(),'GALIENPHARMACEUTICALS@yopmail.com', true,
        'GALIEN PHARMACEUTICALS',' ', '', '1185853V', 'Tunisie', NULL, true, 'TUNISIAN',
        '','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1,
        false, 'COMPANY'),
       (gen_random_uuid(), 'pm-ext-1112396', true ,gen_random_uuid(),'BERGLIFESCIENCES@yopmail.com', true,
        'BERG LIFE SCIENCES',' ', '', '0790786R', 'Tunisie', NULL, true, 'TUNISIAN',
        '','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1,
        false, 'COMPANY'),
       (gen_random_uuid(), 'pm-ext-1112397', true ,gen_random_uuid(),'ADVANSPHARMA@yopmail.com', true,
        'ADVANS PHARMA',' ', '', '1384205A', 'Tunisie', NULL, true, 'TUNISIAN',
        '','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1,
        false, 'COMPANY'),
       (gen_random_uuid(), 'pm-ext-1112398', true ,gen_random_uuid(),'ADWYA@yopmail.com', true,
        'ADWYA',' ', '', '0014346Y', 'Tunisie', NULL, true, 'TUNISIAN',
        '','c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1,
        false, 'COMPANY');

    --ADD-ECTD_ROLE_TO_PP_USERS
INSERT INTO "bs_rel_user_role"("user_id", "role_id")
VALUES
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111285' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111286' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111287' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111288' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111289' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111290' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111291' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111292' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111293' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111294' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111295' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111296' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111297' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111298' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111299' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111300' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111301' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111302' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111303' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111304' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111305' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111306' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111307' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111308' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111309' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111310' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111311' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111312' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111313' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111314' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111315' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111316' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111317' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111318' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111319' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111320' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111321' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111322' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111323' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111324' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111325' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111326' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111327' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111328' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111329' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),


     --ADD-DEFAULT_ROLE_TO_PP_USERS

    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111285' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111286' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111287' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111288' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111289' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111290' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111291' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111292' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111293' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111294' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111295' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111296' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111297' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111298' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111299' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111300' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111301' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111302' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111303' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111304' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111305' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111306' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111307' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111308' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111309' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111310' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111311' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111312' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111313' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111314' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111315' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111316' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111317' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111318' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111319' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111320' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111321' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111322' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111323' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111324' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111325' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111326' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111327' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111328' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111329' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_DEFAULT_ROLE' LIMIT 1)),

     --ADD-BS_ROLE_PM_INDUSTRIEL_TO_PM_USERS



    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112385' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_INDUSTRIEL' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112386' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_INDUSTRIEL' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112387' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_INDUSTRIEL' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112388' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_INDUSTRIEL' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112389' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_INDUSTRIEL' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112390' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_INDUSTRIEL' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112391' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_INDUSTRIEL' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112392' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_INDUSTRIEL' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112393' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_INDUSTRIEL' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112394' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_INDUSTRIEL' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112395' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_INDUSTRIEL' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112396' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_INDUSTRIEL' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112397' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_INDUSTRIEL' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112398' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_INDUSTRIEL' LIMIT 1)),

     --ADD-BS_ROLE_PM_LABO_LOC_TO_PM_USERS


   ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112385' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_LABO_LOC' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112386' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_LABO_LOC' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112387' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_LABO_LOC' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112388' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_LABO_LOC' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112389' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_LABO_LOC' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112390' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_LABO_LOC' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112391' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_LABO_LOC' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112392' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_LABO_LOC' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112393' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_LABO_LOC' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112394' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_LABO_LOC' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112395' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_LABO_LOC' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112396' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_LABO_LOC' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112397' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_LABO_LOC' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112398' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_PM_LABO_LOC' LIMIT 1)),

     --ADD-BS_ROLE_ECTD_TO_PM_USERS


    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112385' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112386' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112387' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112388' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112389' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112390' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112391' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112392' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112393' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112394' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112395' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112396' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112397' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'pm-ext-1112398' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1));


-- INSERT-DESIGNATIONS_TO_PM_USERS

INSERT INTO "bs_designations_list"("id", "pm_user_id", "designated_user_id", "role_id", "created_by", "created_date",
                                   "last_modified_by", "last_modified_date", "version", "deleted")
VALUES
    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112385' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111285' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_TECH' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),  1,'false'),
       (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112385' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111286' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_PROD' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),
       (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112385' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111287' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_CTRL_QA' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),
       (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112385' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111288' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),
       (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112385' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111289' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),

       (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112386' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111290' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_TECH' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'), (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112386' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111290' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_PROD' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'), (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112386' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111291' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_CTRL_QA' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'), (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112386' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111291' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_CTRL_QA' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'), (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112386' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111290' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),
       (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112387' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111292' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_TECH' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),
       (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112388' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111293' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_TECH' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),       (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112388' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111294' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_PROD' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),       (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112388' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111287' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_CTRL_QA' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),       (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112388' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111295' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),       (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112388' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111296' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),
       (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112389' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111297' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_TECH' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),       (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112389' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111298' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_PROD' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),       (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112389' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111297' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_CTRL_QA' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),       (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112389' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111299' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),       (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112389' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111300' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),
       (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112390' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111301' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_TECH' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112390' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111302' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_PROD' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),       (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112390' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111303' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_CTRL_QA' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),       (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112390' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111304' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),


        (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112391' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111305' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_TECH' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112391' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111306' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_PROD' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),       (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112391' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111307' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_CTRL_QA' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),  (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112391' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111308' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),(gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112391' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111309' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),


        (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112392' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111310' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_TECH' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112392' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111311' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_PROD' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),       (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112392' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111310' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_CTRL_QA' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),  (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112392' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111308' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),(gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112392' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111312' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),


           (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112393' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111313' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_TECH' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112393' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111313' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_PROD' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),       (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112393' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111314' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_CTRL_QA' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),  (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112393' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111315' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),(gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112393' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111315' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),


         (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112394' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111316' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_TECH' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112394' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111316' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_PROD' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),       (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112394' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111317' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_CTRL_QA' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),  (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112394' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111317' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),(gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112394' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111317' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),


      (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112395' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111318' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_TECH' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),  (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112395' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111318' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),


 (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112396' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111319' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_TECH' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112396' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111319' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_PROD' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),       (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112396' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111320' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_CTRL_QA' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),  (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112396' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111321' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),(gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112396' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111320' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),




 (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112397' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111322' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_TECH' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112397' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111323' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_PROD' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),       (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112397' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111322' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_CTRL_QA' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),  (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112397' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111324' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),(gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112397' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111325' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),



 (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112398' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111326' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_TECH' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112398' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111327' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_PROD' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),       (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112398' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111328' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_CTRL_QA' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),  (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112398' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111326' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false'),
    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112398' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111329' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false');

UPDATE "bs_designations_list" SET role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1)
where pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111251' LIMIT 1)
  and designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111207' LIMIT 1)
  and role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1) ;

UPDATE "bs_designations_list" SET role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1)
where pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111246' LIMIT 1)
  and designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111218' LIMIT 1)
  and role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1) ;

UPDATE "bs_designations_list" SET role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1)
where pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111245' LIMIT 1)
  and designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111217' LIMIT 1)
  and role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1) ;

UPDATE "bs_designations_list" SET role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1)
where pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111243' LIMIT 1)
  and designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111203' LIMIT 1)
  and role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1) ;

UPDATE "bs_designations_list" SET role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1)
where pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111242' LIMIT 1)
  and designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111198' LIMIT 1)
  and role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1) ;

UPDATE "bs_designations_list" SET role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1)
where pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111240' LIMIT 1)
  and designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111183' LIMIT 1)
  and role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1) ;

UPDATE "bs_designations_list" SET role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1)
where pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111239' LIMIT 1)
  and designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111189' LIMIT 1)
  and role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1) ;

UPDATE "bs_designations_list" SET role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1)
where pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111236' LIMIT 1)
  and designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111178' LIMIT 1)
  and role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1) ;

UPDATE "bs_designations_list" SET role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1)
where pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111238' LIMIT 1)
  and designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111186' LIMIT 1)
  and role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1) ;


INSERT INTO "bs_designations_list"("id", "pm_user_id", "designated_user_id", "role_id", "created_by", "created_date",
                                   "last_modified_by", "last_modified_date", "version", "deleted")

VALUES (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1111238' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111221' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
    1,'false');


UPDATE "bs_designations_list" SET role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1)
where pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111235' LIMIT 1)
  and designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111174' LIMIT 1)
  and role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1) ;


INSERT INTO "bs_designations_list"("id", "pm_user_id", "designated_user_id", "role_id", "created_by", "created_date",
                                   "last_modified_by", "last_modified_date", "version", "deleted")

VALUES (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1111235' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111221' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
    1,'false');


UPDATE "bs_designations_list" SET role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1)
where pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111234' LIMIT 1)
  and designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111170' LIMIT 1)
  and role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1) ;

UPDATE "bs_designations_list" SET role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1)
where pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111233' LIMIT 1)
  and designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111168' LIMIT 1)
  and role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1) ;

UPDATE "bs_designations_list" SET role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1)
where pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111232' LIMIT 1)
  and designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111166' LIMIT 1)
  and role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1) ;

UPDATE "bs_designations_list" SET role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1)
where pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111231' LIMIT 1)
  and designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111162' LIMIT 1)
  and role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1) ;


UPDATE "bs_designations_list" SET role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1)
where pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111230' LIMIT 1)
  and designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111158' LIMIT 1)
  and role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1) ;


UPDATE "bs_designations_list" SET role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1)
where pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111229' LIMIT 1)
  and designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111138' LIMIT 1)
  and role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1) ;

UPDATE "bs_designations_list"
SET designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111132' LIMIT 1)
WHERE pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111225' LIMIT 1)
  AND designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111224' LIMIT 1)
  AND role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_CTRL_QA' LIMIT 1)
  AND EXISTS (SELECT 1 FROM bs_user WHERE username = 'ext-1111132');

DELETE FROM "bs_designations_list"
where pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111241' LIMIT 1)
and designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111195' LIMIT 1)
and role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1) ;

UPDATE "bs_designations_list" SET role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1)
where pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111241' LIMIT 1)
  and designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111195' LIMIT 1)
  and role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1) ;

DELETE FROM "bs_designations_list"
where pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111236' LIMIT 1)
  and designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111221' LIMIT 1)
 and role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1) ;

UPDATE "bs_designations_list" SET role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1)
where pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111250' LIMIT 1)
  and designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111154' LIMIT 1)
  and role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1) ;


