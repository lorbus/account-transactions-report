--CREATE SCHEMA

CREATE TABLE IF NOT EXISTS currency (
    id VARCHAR(3) PRIMARY KEY,
    name TEXT,
    updated_on TIMESTAMP DEFAULT now(),
	updated_by VARCHAR DEFAULT 'sysadmin'
);

CREATE TABLE IF NOT EXISTS account (
    id SERIAL PRIMARY KEY,
    iban VARCHAR(26) NOT NULL,
    user_id VARCHAR NOT NULL,
    balance NUMERIC(15,2) DEFAULT 0 NOT NULL,
    currency_id	VARCHAR(3) REFERENCES currency (id) NOT NULL,
    updated_on TIMESTAMP DEFAULT now(),
	updated_by VARCHAR DEFAULT 'sysadmin'
);

CREATE TABLE IF NOT EXISTS transaction_type (
    id VARCHAR PRIMARY KEY,
    description TEXT,
    updated_on TIMESTAMP DEFAULT now(),
	updated_by VARCHAR DEFAULT 'sysadmin'
);

CREATE TABLE IF NOT EXISTS transaction (
    id SERIAL PRIMARY KEY,
    type_id VARCHAR NOT NULL REFERENCES transaction_type (id),
    amount NUMERIC(15,2) NOT NULL,
    account_id INTEGER REFERENCES account(id),
    currency_id	VARCHAR(3) REFERENCES currency (id) NOT NULL,
    description TEXT,
    updated_on TIMESTAMP DEFAULT now(),
	updated_by VARCHAR DEFAULT 'sysadmin'
);