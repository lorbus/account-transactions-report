--CREATE SCHEMA schema_account_transactions

-- Create table app_role
CREATE TABLE IF NOT EXISTS app_role (
  id            BIGINT NOT NULL,
  role_name     VARCHAR(30) NOT NULL,
  description   VARCHAR(255) NOT NULL
);

ALTER TABLE app_role
  ADD CONSTRAINT app_role_pk PRIMARY KEY (id);

ALTER TABLE app_role
  ADD CONSTRAINT app_role_uk UNIQUE (role_name);

-- Create table app_user
CREATE TABLE IF NOT EXISTS app_user (
  id            BIGINT          NOT NULL,
  first_name    VARCHAR(255)    NOT NULL,
  last_name     VARCHAR(255)    NOT NULL,
  username      VARCHAR(36)     NOT NULL,
  password      VARCHAR(255)    NOT NULL,
  enabled       INT             NOT NULL
);

ALTER TABLE app_user
  ADD CONSTRAINT app_user_pk PRIMARY KEY (id);
 
ALTER TABLE app_user
  ADD CONSTRAINT app_user_uk UNIQUE (username);

-- Create table user_role
CREATE TABLE IF NOT EXISTS user_role (
  id      SERIAL NOT NULL,
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL
);

ALTER TABLE user_role
  ADD CONSTRAINT user_role_pk PRIMARY KEY (id);
 
ALTER TABLE user_role
  ADD CONSTRAINT user_role_uk UNIQUE (user_id, role_id);

ALTER TABLE user_role
  ADD CONSTRAINT user_role_fk1 FOREIGN KEY (user_id)
  references app_user (id);

ALTER TABLE user_role
  ADD CONSTRAINT user_role_fk2 FOREIGN KEY (role_id)
  references app_role (id);

-- Create table currency
CREATE TABLE IF NOT EXISTS currency (
    id          VARCHAR(3)  PRIMARY KEY,
    name        TEXT,
    updated_on  TIMESTAMP   DEFAULT now(),
	updated_by  VARCHAR     DEFAULT 'sysadmin'
);

-- Create table account
CREATE TABLE IF NOT EXISTS account (
    id          SERIAL          PRIMARY KEY,
    iban        VARCHAR(26)     NOT NULL,
    user_id     BIGINT          NOT NULL REFERENCES app_user (id),
    balance     NUMERIC(15,2)   NOT NULL DEFAULT 0,
    currency_id	VARCHAR(3)      NOT NULL REFERENCES currency (id),
    updated_on  TIMESTAMP       DEFAULT now(),
	updated_by  VARCHAR         DEFAULT 'sysadmin'
);

-- Create table transaction_type
CREATE TABLE IF NOT EXISTS transaction_type (
    id          VARCHAR     PRIMARY KEY,
    description TEXT,
    updated_on  TIMESTAMP   DEFAULT now(),
	updated_by  VARCHAR     DEFAULT 'sysadmin'
);

-- Create table transaction
CREATE TABLE IF NOT EXISTS transaction (
    id          SERIAL          PRIMARY KEY,
    type_id     VARCHAR         NOT NULL REFERENCES transaction_type (id),
    amount      NUMERIC(15,2)   NOT NULL,
    account_id  INTEGER         NOT NULL REFERENCES account (id),
    currency_id	VARCHAR(3)      NOT NULL REFERENCES currency (id),
    description TEXT,
    updated_on  TIMESTAMP       DEFAULT now(),
	updated_by  VARCHAR         DEFAULT 'sysadmin'
);