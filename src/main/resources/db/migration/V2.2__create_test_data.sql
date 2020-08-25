insert into account (iban, currency_id, user_id) values ('CH93-0000-0000-0000-0000-1', 'EUR', 'user1');
insert into transaction (type_id, amount, account_id, currency_id, description)
values ('C', 10, 1, 'EUR', 'Online credit EUR');

insert into account (iban, currency_id, user_id) values ('CH93-0000-0000-0000-0000-2', 'EUR', 'user2');
insert into transaction (type_id, amount, account_id, currency_id, description)
values ('D', -10, 2, 'EUR', 'Online payment EUR');

insert into account (iban, currency_id, user_id) values ('CH93-0000-0000-0000-0000-3', 'GBP', 'user3');
insert into transaction (type_id, amount, account_id, currency_id, description)
values ('C', 10000, 3, 'GBP', 'Online credit GBP');

insert into account (iban, currency_id, user_id) values ('CH93-0000-0000-0000-0000-3', 'GBP', 'user3');
insert into transaction (type_id, amount, account_id, currency_id, description)
values ('D', -100, 3, 'GBP', 'Online payment GBP');

insert into account (iban, currency_id, user_id) values ('CH93-0000-0000-0000-0000-4', 'CHF', 'user4');
insert into transaction (type_id, amount, account_id, currency_id, description)
values ('C', 1000, 4, 'CHF', 'Online credit CHF');

insert into account (iban, currency_id, user_id) values ('CH93-0000-0000-0000-0000-4', 'CHF', 'user4');
insert into transaction (type_id, amount, account_id, currency_id, description)
values ('C', 500, 4, 'CHF', 'Online payment CHF');