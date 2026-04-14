update "bs_user"
set national_id = '08457131'
where username='ext-1111294';

update "bs_user"
set national_id = '02738725'
where username='ext-1111291';


-- Delete from bs_rel_user_role
DELETE FROM "bs_rel_user_role"
WHERE "user_id" = (
    SELECT "id" FROM "bs_user"
    WHERE username = 'ext-1111315'
);

-- Delete from bs_rel_user_group
DELETE FROM "bs_rel_user_group"
WHERE "user_id" = (
    SELECT "id" FROM "bs_user"
    WHERE username = 'ext-1111315'
);

-- Delete from bs_designations_list
DELETE  FROM "bs_designations_list"
WHERE  "designated_user_id" = (
    SELECT "id" FROM "bs_user"
    WHERE  username  = 'ext-1111315'
);

-- Delete from bs_user
DELETE FROM "bs_user"
WHERE username = 'ext-1111315';
