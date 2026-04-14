
-- Insert into bs_designations_list

INSERT INTO "bs_designations_list"(  "id", "pm_user_id", "designated_user_id", "role_id",
                                     "created_by", "created_date", "last_modified_by", "last_modified_date", "version", "deleted"
)
SELECT
    gen_random_uuid(),
    (SELECT id FROM bs_user WHERE username = 'pm-ext-1112089' LIMIT 1),
    (SELECT id FROM bs_user WHERE username = 'ext-1111221' LIMIT 1),
    r.id,
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',
    NOW(),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',
    NOW(),
    1,
    false
FROM bs_role r
WHERE r.label = 'BS_ROLE_PM_RESP_REG_AFF';


