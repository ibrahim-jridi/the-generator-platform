INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date",
                            "last_modified_by",
                            "last_modified_date", "version", "first_insert", "deleted")
VALUES ('2f133e65-b636-4b70-9aac-9d2cc16eea92', 'BS_ADMIN', 'true', 'BS_ADMIN',
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',
        NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'true', 'false');

INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date",
                            "last_modified_by",
                            "last_modified_date", "version", "first_insert", "deleted")
VALUES (gen_random_uuid(), 'BS_USER', 'true', 'BS_USER', 'c2102141-dc4e-4c13-b52e-35e665af9d7e',
        NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'true', 'false');

INSERT INTO "bs_role" ("id", "label", "description", "is_active", "created_by", "created_date",
                       "last_modified_by",
                       "last_modified_date", "version", "deleted")
VALUES ('658a7856-1286-49c8-bf3b-45abfc94687f', 'ROLE_ADMIN', 'ROLE_ADMIN', 'true',
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',
        NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');

INSERT INTO "bs_rel_role_authority" ("role_id", "authority_id")
VALUES ('658a7856-1286-49c8-bf3b-45abfc94687f', '2f133e65-b636-4b70-9aac-9d2cc16eea92');


INSERT INTO "bs_user" ("id", "username", "email_verified", "keycloak_id", "email",
                       "is_active", "gender",
                       "first_name", "last_name", "address", "age", "birth_date", "cin",
                       "country", "e_barid",
                       "profile_completed", "nationality", "phone_number", "created_by",
                       "created_date",
                       "last_modified_by", "last_modified_date", "version", "deleted")
VALUES ('a7eca4d8-61fa-41ee-a55a-9d690bdc3e4c', 'BS_ROOT', 'true',
        '24f064be-1b84-4bc3-a143-53d2ff674fa2',
        'root@yopmail.com', 'true', 'MALE', 'root', 'root',
        ' tunis، 1003', '99',
        '2018-04-01', '', 'Tunisie', NULL, NULL, NULL, '71130131',
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',
        NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');

INSERT INTO "bs_user" ("id", "username", "email_verified", "keycloak_id", "email",
                       "is_active", "gender",
                       "first_name", "last_name", "address", "age", "birth_date", "cin",
                       "country", "e_barid",
                       "profile_completed", "nationality", "phone_number", "created_by",
                       "created_date",
                       "last_modified_by", "last_modified_date", "version", "deleted")
VALUES ('c2102141-dc4e-4c13-b52e-35e665af9d7d', 'BS_ADMIN', 'true',
        'c2102141-dc4e-4c13-b52e-35e665af9d7d',
        'admin@yopmail.com', 'true', 'MALE', 'admin', 'admin',
        ' tunis، 1003', '99',
        '2018-04-01', '', 'Tunisie', NULL, NULL, NULL, '71130131',
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',
        NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'false');

INSERT INTO "bs_rel_user_role" ("user_id", "role_id")
VALUES ('a7eca4d8-61fa-41ee-a55a-9d690bdc3e4c', '658a7856-1286-49c8-bf3b-45abfc94687f');

INSERT INTO "bs_rel_user_role" ("user_id", "role_id")
VALUES ('c2102141-dc4e-4c13-b52e-35e665af9d7d', '658a7856-1286-49c8-bf3b-45abfc94687f');

INSERT INTO "bs_group" ("id", "description", "is_active", "label", "parent_id", "created_by",
                        "created_date",
                        "last_modified_by", "last_modified_date", "version", "deleted")
VALUES ('b7eca4d8-61fa-41ee-a55a-9d690bdc3e4d', 'globalrouGp', 'true', 'globalGroup', NULL,
        'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e',
        NOW(), 1, 'false');
INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date",
                            "last_modified_by",
                            "last_modified_date", "version", "first_insert", "deleted")
VALUES ('d8cb758b-0e21-4608-868e-69ce8e8d8d32', 'BS_USERSMANG', 'true', 'BS_USERSMANG',
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',
        NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'true', 'false');

INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date",
                            "last_modified_by",
                            "last_modified_date", "version", "first_insert", "deleted")
VALUES ('24807c64-3e71-42e1-9efd-7054c857ea89', 'BS_ROLES', 'true', 'BS_ROLES',
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',
        NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'true', 'false');
INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date",
                            "last_modified_by",
                            "last_modified_date", "version", "first_insert", "deleted")
VALUES ('9ac5c280-ab34-4d70-93d7-6dc827fd15e8', 'BS_GROUPS', 'true', 'BS_GROUPS',
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',
        NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'true', 'false');
INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date",
                            "last_modified_by",
                            "last_modified_date", "version", "first_insert", "deleted")
VALUES ('d97e9707-aa66-44c9-9d48-9e1a4fdbf32f', 'BS_PROCESS', 'true', 'BS_PROCESS',
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',
        NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'true', 'false');
INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date",
                            "last_modified_by",
                            "last_modified_date", "version", "first_insert", "deleted")
VALUES ('31061b8c-6f2f-413d-8e0d-4b7bcbbb72ef', 'BS_TRANSACTIONS', 'true', 'BS_TRANSACTIONS',
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',
        NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'true', 'false');
INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date",
                            "last_modified_by",
                            "last_modified_date", "version", "first_insert", "deleted")
VALUES ('43e580e8-7ffc-49d0-99a7-764a9117c8aa', 'BS_TASKS_BY_GROUP', 'true', 'BS_TASKS_BY_GROUP',
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',
        NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'true', 'false');
INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date",
                            "last_modified_by",
                            "last_modified_date", "version", "first_insert", "deleted")
VALUES ('72594183-309f-4626-ba32-ba6c5dfa5046', 'BS_TASKS', 'true', 'BS_TASKS',
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',
        NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'true', 'false');
INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date",
                            "last_modified_by",
                            "last_modified_date", "version", "first_insert", "deleted")
VALUES ('9830b2e1-262f-4cf7-a8d1-1a5e885feed9', 'BS_NOTIFICATIONS', 'true', 'BS_NOTIFICATIONS',
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',
        NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'true', 'false');
INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date",
                            "last_modified_by",
                            "last_modified_date", "version", "first_insert", "deleted")
VALUES ('3b2963f8-fcba-4c46-bbb7-93b11f585a31', 'BS_VALUES', 'true', 'BS_VALUES',
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',
        NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'true', 'false');
INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date",
                            "last_modified_by",
                            "last_modified_date", "version", "first_insert", "deleted")
VALUES ('bd867a24-00a3-49e5-9280-329544707a25', 'BS_FORMS', 'true', 'BS_FORMS',
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',
        NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'true', 'false');
INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date",
                            "last_modified_by",
                            "last_modified_date", "version", "first_insert", "deleted")
VALUES ('58ceeaf9-93d4-4f22-ba04-7feafcbe5f58', 'BS_REPORTS', 'true', 'BS_REPORTS',
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',
        NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'true', 'false');
INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date",
                            "last_modified_by",
                            "last_modified_date", "version", "first_insert", "deleted")
VALUES ('8a911484-06b4-4a71-b9ba-2ae66d7a2547', 'BS_FILES', 'true', 'BS_FILES',
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',
        NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'true', 'false');
INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date",
                            "last_modified_by",
                            "last_modified_date", "version", "first_insert", "deleted")
VALUES ('07942db3-140b-457a-8dc8-7bb1b06e8e3e', 'BS_MY_FILES', 'true', 'BS_MY_FILES',
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',
        NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'true', 'false');
INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date",
                            "last_modified_by",
                            "last_modified_date", "version", "first_insert", "deleted")
VALUES ('594f6450-1c32-40f6-90d2-f198522abb83', 'BS_NATURAL_PERSON', 'true', 'BS_NATURAL_PERSON',
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',
        NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'true', 'false');
INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date",
                            "last_modified_by",
                            "last_modified_date", "version", "first_insert", "deleted")
VALUES ('b07037b0-57df-4c97-b954-201c1e137e8c', 'BS_LEGAL_PERSON', 'true', 'BS_LEGAL_PERSON',
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',
        NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'true', 'false');
INSERT INTO "bs_authority" ("id", "description", "is_active", "label", "created_by", "created_date",
                            "last_modified_by",
                            "last_modified_date", "version", "first_insert", "deleted")
VALUES ('f10a5bf1-8180-4b6a-8480-ae0f2b918ee7', 'BS_FILE_CONFIGURATION', 'true', 'BS_FILE_CONFIGURATION',
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',
        NOW(), 'c2102141-dc4e-4c13-b52e-35e665af9d7e', NOW(), 1, 'true', 'false');
