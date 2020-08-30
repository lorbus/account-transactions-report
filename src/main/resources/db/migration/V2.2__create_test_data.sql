-- ACCOUNT & TRX for user1
INSERT INTO account (iban, currency_id, user_id)
    VALUES ('CH93-0000-0000-0000-0000-1', 'EUR', 1);
INSERT INTO transaction (type_id, amount, account_id, currency_id, description)
    VALUES ('C', 10, 1, 'EUR', 'Online credit EUR');

INSERT INTO account (iban, currency_id, user_id)
    VALUES ('CH93-0000-0000-0000-0000-2', 'EUR', 2);
INSERT INTO transaction (type_id, amount, account_id, currency_id, description)
    VALUES ('D', -10, 2, 'EUR', 'Online payment EUR');

INSERT INTO account (iban, currency_id, user_id)
    VALUES ('CH93-0000-0000-0000-0000-3', 'GBP', 1);
INSERT INTO transaction (type_id, amount, account_id, currency_id, description)
    VALUES ('C', 10000, 3, 'GBP', 'Online credit GBP');

INSERT INTO account (iban, currency_id, user_id)
    VALUES ('CH93-0000-0000-0000-0000-3', 'GBP', 1);
INSERT INTO transaction (type_id, amount, account_id, currency_id, description)
    VALUES ('D', -100, 3, 'GBP', 'Online payment GBP');

INSERT INTO account (iban, currency_id, user_id)
    VALUES ('CH93-0000-0000-0000-0000-4', 'CHF', 2);
INSERT INTO transaction (type_id, amount, account_id, currency_id, description)
    VALUES ('C', 1000, 4, 'CHF', 'Online credit CHF');

INSERT INTO account (iban, currency_id, user_id)
    VALUES ('CH93-0000-0000-0000-0000-4', 'CHF', 2);
INSERT INTO transaction (type_id, amount, account_id, currency_id, description)
    VALUES ('C', 500, 4, 'CHF', 'Online payment CHF');