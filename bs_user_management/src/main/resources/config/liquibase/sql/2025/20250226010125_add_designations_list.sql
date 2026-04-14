CREATE TABLE IF NOT EXISTS bs_designations_list (
                                      id UUID NOT NULL PRIMARY KEY,
                                      pm_user_id UUID NOT NULL,
                                      designated_user_id UUID NOT NULL,
                                      role_id UUID NOT NULL,
                                      created_by UUID NOT NULL,
                                      created_date TIMESTAMP NOT NULL,
                                      last_modified_by UUID NOT NULL,
                                      last_modified_date TIMESTAMP NOT NULL,
                                      version INTEGER NOT NULL,
                                      deleted BOOLEAN NOT NULL,
                                      CONSTRAINT fk_BS_ROLE_user_id FOREIGN KEY (pm_user_id) REFERENCES bs_user(id),
                                      CONSTRAINT fk_bs_designated_user_id FOREIGN KEY (designated_user_id) REFERENCES bs_user(id),
                                      CONSTRAINT fk_bs_role_id FOREIGN KEY (role_id) REFERENCES bs_role(id)
);
