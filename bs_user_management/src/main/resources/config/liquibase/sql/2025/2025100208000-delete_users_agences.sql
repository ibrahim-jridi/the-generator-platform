
-- Delete from bs_rel_user_group
DELETE FROM bs_rel_user_group
WHERE user_id IN (
    SELECT id FROM bs_user
    WHERE username IN (
                       'pm-ext-1111252','pm-ext-1111253','pm-ext-1111254','pm-ext-1111255','pm-ext-1111256',
                       'pm-ext-1111257','pm-ext-1111258','pm-ext-1111259','pm-ext-1111260','pm-ext-1111261',
                       'pm-ext-1111262','pm-ext-1111263','pm-ext-1111264','pm-ext-1111265','pm-ext-1111266',
                       'pm-ext-1111267','pm-ext-1111268','pm-ext-1111269','pm-ext-1111270','pm-ext-1111271',
                       'pm-ext-1111272','pm-ext-1111273','pm-ext-1111274','pm-ext-1111275','pm-ext-1111276',
                       'pm-ext-1111277','pm-ext-1111278','pm-ext-1111279','pm-ext-1111280','pm-ext-1111281',
                       'pm-ext-1111282','pm-ext-1111283','pm-ext-1111284','pm-ext-1111285','pm-ext-1111286',
                       'pm-ext-1111287','pm-ext-1111288','pm-ext-1111289','pm-ext-1111290','pm-ext-1111291',
                       'pm-ext-1111292','pm-ext-1111293','pm-ext-1111294',
                       'ext-1111226','ext-1111227','ext-1111228','ext-1111229','ext-1111230','ext-1111231',
                       'ext-1111232','ext-1111233','ext-1111234','ext-1111235','ext-1111236','ext-1111237',
                       'ext-1111238','ext-1111239','ext-1111240','ext-1111241','ext-1111242','ext-1111243',
                       'ext-1111244','ext-1111245','ext-1111246','ext-1111247','ext-1111248','ext-1111249',
                       'ext-1111250','ext-1111251','ext-1111252','ext-1111253','ext-1111254','ext-1111255',
                       'ext-1111256','ext-1111257','ext-1111258','ext-1111259','ext-1111260','ext-1111261',
                       'ext-1111262','ext-1111263','ext-1111264','ext-1111265','ext-1111266','ext-1111267',
                       'ext-1111268','ext-1111269','ext-1111270','ext-1111271','ext-1111272','ext-1111273',
                       'ext-1111274','ext-1111275','ext-1111276','ext-1111277','ext-1111278','ext-1111279',
                       'ext-1111280','ext-1111281','ext-1111282','ext-1111283','ext-1111284'
        )
);
delete from "bs_rel_user_role" where user_id in (select id
        FROM "bs_user"
        WHERE username IN
              ('ext-1111226','ext-1111227','ext-1111228','ext-1111229','ext-1111230','ext-1111231',
               'ext-1111232','ext-1111233','ext-1111234','ext-1111235','ext-1111236','ext-1111237',
               'ext-1111238','ext-1111239','ext-1111240','ext-1111241','ext-1111242','ext-1111243',
               'ext-1111244','ext-1111245','ext-1111246','ext-1111247','ext-1111248','ext-1111249',
               'ext-1111250','ext-1111251','ext-1111252','ext-1111253','ext-1111254','ext-1111255',
               'ext-1111256','ext-1111257','ext-1111258','ext-1111259','ext-1111260','ext-1111261',
               'ext-1111262','ext-1111263','ext-1111264','ext-1111265','ext-1111266','ext-1111267',
               'ext-1111268','ext-1111269','ext-1111270','ext-1111271','ext-1111272','ext-1111273',
               'ext-1111274','ext-1111275','ext-1111276','ext-1111277','ext-1111278','ext-1111279',
               'ext-1111280','ext-1111281','ext-1111282','ext-1111283','ext-1111284'));

delete from "bs_rel_user_role" where user_id in (select id
        FROM "bs_user"
        WHERE username IN
              ('pm-ext-1111252','pm-ext-1111253','pm-ext-1111254','pm-ext-1111255','pm-ext-1111256',
               'pm-ext-1111257','pm-ext-1111258','pm-ext-1111259','pm-ext-1111260','pm-ext-1111261',
               'pm-ext-1111262','pm-ext-1111263','pm-ext-1111264','pm-ext-1111265','pm-ext-1111266',
               'pm-ext-1111267','pm-ext-1111268','pm-ext-1111269','pm-ext-1111270','pm-ext-1111271',
               'pm-ext-1111272','pm-ext-1111273','pm-ext-1111274','pm-ext-1111275','pm-ext-1111276',
               'pm-ext-1111277','pm-ext-1111278','pm-ext-1111279','pm-ext-1111280','pm-ext-1111281',
               'pm-ext-1111282','pm-ext-1111283','pm-ext-1111284','pm-ext-1111285','pm-ext-1111286',
               'pm-ext-1111287','pm-ext-1111288','pm-ext-1111289','pm-ext-1111290','pm-ext-1111291',
               'pm-ext-1111292','pm-ext-1111293','pm-ext-1111294'));

delete from "bs_designations_list" where pm_user_id in (select id
        FROM "bs_user"
        WHERE username IN
                 ('pm-ext-1111252','pm-ext-1111253','pm-ext-1111254','pm-ext-1111255','pm-ext-1111256',
                'pm-ext-1111257','pm-ext-1111258','pm-ext-1111259','pm-ext-1111260','pm-ext-1111261',
                'pm-ext-1111262','pm-ext-1111263','pm-ext-1111264','pm-ext-1111265','pm-ext-1111266',
                'pm-ext-1111267','pm-ext-1111268','pm-ext-1111269','pm-ext-1111270','pm-ext-1111271',
                'pm-ext-1111272','pm-ext-1111273','pm-ext-1111274','pm-ext-1111275','pm-ext-1111276',
                'pm-ext-1111277','pm-ext-1111278','pm-ext-1111279','pm-ext-1111280','pm-ext-1111281',
                'pm-ext-1111282','pm-ext-1111283','pm-ext-1111284','pm-ext-1111285','pm-ext-1111286',
                'pm-ext-1111287','pm-ext-1111288','pm-ext-1111289','pm-ext-1111290','pm-ext-1111291',
                'pm-ext-1111292','pm-ext-1111293','pm-ext-1111294'));


delete FROM "bs_user" WHERE username IN
             ('pm-ext-1111252','pm-ext-1111253','pm-ext-1111254','pm-ext-1111255','pm-ext-1111256',
            'pm-ext-1111257','pm-ext-1111258','pm-ext-1111259','pm-ext-1111260','pm-ext-1111261',
            'pm-ext-1111262','pm-ext-1111263','pm-ext-1111264','pm-ext-1111265','pm-ext-1111266',
            'pm-ext-1111267','pm-ext-1111268','pm-ext-1111269','pm-ext-1111270','pm-ext-1111271',
            'pm-ext-1111272','pm-ext-1111273','pm-ext-1111274','pm-ext-1111275','pm-ext-1111276',
            'pm-ext-1111277','pm-ext-1111278','pm-ext-1111279','pm-ext-1111280','pm-ext-1111281',
            'pm-ext-1111282','pm-ext-1111283','pm-ext-1111284','pm-ext-1111285','pm-ext-1111286',
            'pm-ext-1111287','pm-ext-1111288','pm-ext-1111289','pm-ext-1111290','pm-ext-1111291',
            'pm-ext-1111292','pm-ext-1111293','pm-ext-1111294');

delete FROM "bs_user" WHERE username IN
                            ('ext-1111226','ext-1111227','ext-1111228','ext-1111229','ext-1111230','ext-1111231',
                             'ext-1111232','ext-1111233','ext-1111234','ext-1111235','ext-1111236','ext-1111237',
                             'ext-1111238','ext-1111239','ext-1111240','ext-1111241','ext-1111242','ext-1111243',
                             'ext-1111244','ext-1111245','ext-1111246','ext-1111247','ext-1111248','ext-1111249',
                             'ext-1111250','ext-1111251','ext-1111252','ext-1111253','ext-1111254','ext-1111255',
                             'ext-1111256','ext-1111257','ext-1111258','ext-1111259','ext-1111260','ext-1111261',
                             'ext-1111262','ext-1111263','ext-1111264','ext-1111265','ext-1111266','ext-1111267',
                             'ext-1111268','ext-1111269','ext-1111270','ext-1111271','ext-1111272','ext-1111273',
                             'ext-1111274','ext-1111275','ext-1111276','ext-1111277','ext-1111278','ext-1111279',
                             'ext-1111280','ext-1111281','ext-1111282','ext-1111283','ext-1111284');





