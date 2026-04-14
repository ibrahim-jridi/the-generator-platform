CREATE TABLE IF NOT EXISTS "bs_profile"
(
    "user_id" UUID NOT NULL,
    "key"     VARCHAR(255) NOT NULL,
    "value"   TEXT,
    CONSTRAINT "fk_user_profile" FOREIGN KEY ("user_id") REFERENCES "bs_user"("id") ON DELETE CASCADE,
    CONSTRAINT "unique_profile_key" UNIQUE ("user_id", "key")
);
