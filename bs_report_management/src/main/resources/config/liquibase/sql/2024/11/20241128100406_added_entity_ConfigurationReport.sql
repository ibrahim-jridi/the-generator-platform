CREATE TABLE IF NOT EXISTS bs_configuration_report (
    id uuid NOT NULL,
    name VARCHAR,
    address VARCHAR,
    postal_code VARCHAR,
    phone VARCHAR,
    fax VARCHAR,
    email VARCHAR,
    logo VARCHAR,
    footer VARCHAR,
    code VARCHAR,
    created_by uuid,
    created_date TIMESTAMP,
    deleted BOOLEAN,
    last_modified_by uuid,
    version INTEGER,
    last_modified_date TIMESTAMP
);




