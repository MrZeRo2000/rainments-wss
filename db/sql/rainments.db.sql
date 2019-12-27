BEGIN TRANSACTION;
DROP TABLE IF EXISTS "products";
CREATE TABLE IF NOT EXISTS "products" (
	"product_id"	INTEGER NOT NULL,
	"product_name"	TEXT NOT NULL,
	"product_unit_name"	TEXT,
	PRIMARY KEY("product_id")
);
DROP TABLE IF EXISTS "payments";
CREATE TABLE IF NOT EXISTS "payments" (
	"payment_id"	INTEGER NOT NULL,
	"payment_date"	INTEGER NOT NULL,
	"period_id"	INTEGER,
	"location_id"	INTEGER NOT NULL,
	"payment_provider_id"	INTEGER NOT NULL,
	"product_id"	INTEGER,
	"product_counter"	TEXT,
	"payment_amount"	INTEGER NOT NULL,
	"commission_amount"	INTEGER DEFAULT 0,
	PRIMARY KEY("payment_id")
);
DROP TABLE IF EXISTS "payment_providers";
CREATE TABLE IF NOT EXISTS "payment_providers" (
	"payment_provider_id"	INTEGER NOT NULL,
	"payment_provider_name"	TEXT NOT NULL,
	"payment_provider_url"	TEXT,
	PRIMARY KEY("payment_provider_id")
);
DROP TABLE IF EXISTS "periods";
CREATE TABLE IF NOT EXISTS "periods" (
	"period_id"	INTEGER NOT NULL,
	"period_date"	INTEGER NOT NULL,
	"period_year"	INTEGER NOT NULL,
	"period_month"	INTEGER NOT NULL,
	PRIMARY KEY("period_id")
);
DROP TABLE IF EXISTS "locations";
CREATE TABLE IF NOT EXISTS "locations" (
	"location_id"	INTEGER NOT NULL,
	"location_name"	TEXT NOT NULL,
	PRIMARY KEY("location_id")
);
DROP INDEX IF EXISTS "ix_payments_location";
CREATE INDEX IF NOT EXISTS "ix_payments_location" ON "payments" (
	"location_id"
);
DROP INDEX IF EXISTS "ix_payments_period";
CREATE INDEX IF NOT EXISTS "ix_payments_period" ON "payments" (
	"period_id"
);
COMMIT;
