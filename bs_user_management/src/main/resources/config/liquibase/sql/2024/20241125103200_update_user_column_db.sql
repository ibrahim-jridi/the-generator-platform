ALTER TABLE "bs_user" RENAME COLUMN "cin" TO "national_id";

ALTER TABLE "bs_user" ALTER COLUMN "national_id" TYPE VARCHAR;
ALTER TABLE "bs_user" ALTER COLUMN "phone_number" TYPE VARCHAR;

