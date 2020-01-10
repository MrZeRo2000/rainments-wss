BEGIN TRANSACTION;
DROP TABLE IF EXISTS "payments";
CREATE TABLE IF NOT EXISTS "payments" (
	"payment_id"	INTEGER NOT NULL,
	"order_id"	INTEGER,
	"payment_date"	INTEGER NOT NULL,
	"payment_period_date"	INTEGER NOT NULL,
	"location_id"	INTEGER NOT NULL,
	"payment_group_id"	INTEGER NOT NULL,
	"product_id"	INTEGER,
	"product_counter"	TEXT,
	"payment_amount"	INTEGER NOT NULL,
	"commission_amount"	INTEGER DEFAULT 0,
	PRIMARY KEY("payment_id")
);
DROP TABLE IF EXISTS "products";
CREATE TABLE IF NOT EXISTS "products" (
	"product_id"	INTEGER NOT NULL,
	"order_id"	INTEGER,
	"product_name"	TEXT NOT NULL,
	"product_unit_name"	TEXT,
	PRIMARY KEY("product_id")
);
DROP TABLE IF EXISTS "payment_groups";
CREATE TABLE IF NOT EXISTS "payment_groups" (
	"payment_group_id"	INTEGER NOT NULL,
	"order_id"	INTEGER,
	"payment_group_name"	TEXT NOT NULL,
	"payment_group_url"	TEXT,
	PRIMARY KEY("payment_group_id")
);
DROP TABLE IF EXISTS "locations";
CREATE TABLE IF NOT EXISTS "locations" (
	"location_id"	INTEGER NOT NULL,
	"order_id"	INTEGER,
	"location_name"	TEXT NOT NULL,
	PRIMARY KEY("location_id")
);
DROP INDEX IF EXISTS "ix_payments_period_date";
CREATE INDEX IF NOT EXISTS "ix_payments_period_date" ON "payments" (
	"payment_period_date"
);
COMMIT;
