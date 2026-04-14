ALTER TABLE "bs_user"
    ADD COLUMN IF NOT EXISTS file_status VARCHAR(255),
    ADD COLUMN IF NOT EXISTS file_patent VARCHAR(255),
    ADD COLUMN IF NOT EXISTS registry_status VARCHAR(15),
    ADD COLUMN IF NOT EXISTS denomination VARCHAR(255),
    ADD CONSTRAINT user__registry_status_check CHECK (registry_status::text = ANY (ARRAY['ACTIVE'::VARCHAR, 'IN_STOP'::VARCHAR, 'SUSPENDED'::VARCHAR]::text[]));
