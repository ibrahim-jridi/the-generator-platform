CREATE TABLE IF NOT EXISTS jhi_date_time_wrapper
(
    id               BIGINT NOT NULL,
    instant          TIMESTAMP WITHOUT TIME ZONE,
    local_date_time  TIMESTAMP WITHOUT TIME ZONE,
    offset_date_time TIMESTAMP WITHOUT TIME ZONE,
    zoned_date_time  TIMESTAMP WITHOUT TIME ZONE,
    local_time       time WITHOUT TIME ZONE,
    offset_time      time WITHOUT TIME ZONE,
    local_date       date,
    CONSTRAINT pk_jhi_date_time_wrapper PRIMARY KEY (id)
    );
DROP SEQUENCE IF EXISTS sequence_generator;
CREATE SEQUENCE IF NOT EXISTS sequence_generator INCREMENT BY 50 START WITH 100 ;
alter table jhi_date_time_wrapper ALTER COLUMN id SET DEFAULT nextval('sequence_generator');
