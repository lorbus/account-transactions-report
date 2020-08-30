CREATE INDEX ON user_role (role_id);
CREATE INDEX ON user_role (user_id);
CREATE INDEX ON account (user_id);
CREATE INDEX ON transaction (type_id);
CREATE INDEX ON transaction (account_id);
CREATE INDEX ON transaction (currency_id);