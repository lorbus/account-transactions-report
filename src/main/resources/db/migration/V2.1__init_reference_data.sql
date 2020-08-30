-- ROLES
INSERT INTO app_role (id, role_name, description)
    VALUES (1, 'USER', 'Standard User - has no admin rights') ON CONFLICT (id) DO NOTHING;
INSERT INTO app_role (id, role_name, description)
    VALUES (2, 'ADMIN', 'Admin User - has admin rights') ON CONFLICT (id) DO NOTHING;

-- USER - not-encrypted password
INSERT INTO app_user (id, first_name, last_name, username, password, enabled)
    VALUES (1, 'Admin', 'Admin', 'admin',
    'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIlVTRVIiLCJBRE1JTiJdLCJpYXQiOjE1OTg3OTMzNzIsImV4cCI6MTU5ODc5Njk3Mn0.l927FQgNQO4YXR5v06SlpXRrSOammVSzz20WQex8-UY', 1) ON CONFLICT (id) DO NOTHING;
INSERT INTO app_user (id, first_name, last_name, username, password, enabled)
    VALUES (2, 'User', 'User', 'user',
    'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOlsiVVNFUiJdLCJpYXQiOjE1OTg3OTM0MDMsImV4cCI6MTU5ODc5NzAwM30.MdcqNIG0jOHkpt_KMYfnzReBLvWjhpqzUROlQuRWsTE', 1) ON CONFLICT (id) DO NOTHING;

INSERT INTO user_role(user_id, role_id) VALUES(1, 1);
INSERT INTO user_role(user_id, role_id) VALUES(1, 2);
INSERT INTO user_role(user_id, role_id) VALUES(2, 1);

INSERT INTO currency (id) VALUES ('GBP') ON CONFLICT (id) DO NOTHING;
INSERT INTO currency (id) VALUES ('CHF') ON CONFLICT (id) DO NOTHING;
INSERT INTO currency (id) VALUES ('EUR') ON CONFLICT (id) DO NOTHING;

INSERT INTO transaction_type (id, description) 
    VALUES ('D', 'Debit transaction') ON CONFLICT (id) DO NOTHING;
INSERT INTO transaction_type (id, description)
    VALUES ('C', 'Credit transaction') ON CONFLICT (id) DO NOTHING;