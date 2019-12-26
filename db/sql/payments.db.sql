BEGIN TRANSACTION;
DROP TABLE IF EXISTS "Payments";
CREATE TABLE IF NOT EXISTS "Payments" (
	"PaymentId"	INTEGER,
	"PaymentDate"	INTEGER NOT NULL,
	"PaymentPeriodId"	INTEGER,
	"LocationId"	INTEGER NOT NULL,
	"PaymentProviderId"	INTEGER NOT NULL,
	"ProductId"	INTEGER,
	"ProductCounter"	TEXT,
	"PaymentAmount"	INTEGER NOT NULL,
	"CommissionAmount"	INTEGER DEFAULT 0,
	PRIMARY KEY("PaymentId")
);
DROP TABLE IF EXISTS "PaymentProviders";
CREATE TABLE IF NOT EXISTS "PaymentProviders" (
	"PaymentProviderId"	INTEGER,
	"PaymentProviderName"	TEXT NOT NULL,
	"PaymentProviderURL"	TEXT,
	PRIMARY KEY("PaymentProviderId")
);
DROP TABLE IF EXISTS "Products";
CREATE TABLE IF NOT EXISTS "Products" (
	"ProductId"	INTEGER,
	"ProductName"	TEXT NOT NULL,
	"ProductUnitName"	TEXT,
	PRIMARY KEY("ProductId")
);
DROP TABLE IF EXISTS "PaymentPeriods";
CREATE TABLE IF NOT EXISTS "PaymentPeriods" (
	"PaymentPeriodId"	INTEGER,
	"PaymentPeriodDate"	INTEGER NOT NULL,
	"PaymentPeriodYear"	INTEGER NOT NULL,
	"PaymentPeriodMonth"	INTEGER NOT NULL,
	PRIMARY KEY("PaymentPeriodId")
);
DROP TABLE IF EXISTS "Locations";
CREATE TABLE IF NOT EXISTS "Locations" (
	"LocationId"	INTEGER,
	"LocationName"	TEXT NOT NULL,
	PRIMARY KEY("LocationId")
);
DROP INDEX IF EXISTS "ix_payments_period";
CREATE INDEX IF NOT EXISTS "ix_payments_period" ON "Payments" (
	"PaymentPeriodId"
);
DROP INDEX IF EXISTS "ix_payments_locatopm";
CREATE INDEX IF NOT EXISTS "ix_payments_locatopm" ON "Payments" (
	"LocationId"
);
COMMIT;
