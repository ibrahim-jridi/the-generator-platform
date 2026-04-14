CREATE TABLE IF NOT EXISTS bs_report_template (
    id uuid NOT NULL,
    path VARCHAR,
    type VARCHAR,
    created_by uuid,
    created_date TIMESTAMP,
    deleted BOOLEAN,
    last_modified_by uuid,
    version INTEGER,
    last_modified_date TIMESTAMP
);




