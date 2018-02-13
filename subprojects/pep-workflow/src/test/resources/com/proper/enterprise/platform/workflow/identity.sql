INSERT INTO pep_auth_users (id, username, password, superuser, pep_dtype, name, phone, email, enable) VALUES ('user1', 'testuser1', 'pwd', 'Y', 'UserEntity', 'c', '12345678901', 'test1@test.com', 'Y');
INSERT INTO pep_auth_users (id, username, password, superuser, pep_dtype, name, phone, email, enable) VALUES ('user2', 'testuser2', 'pwd', 'F',
'UserEntity', 'b', '12345678902', 'test2@test.com', 'Y');
INSERT INTO pep_auth_users (id, username, password, superuser, pep_dtype, name, phone, email, enable) VALUES ('user3', 'testuser3', 'e10adc3949ba59abbe56e057f20f883e', 'N', 'UserEntity', 'a', '12345678903', 'test3@test.com', 'Y');

INSERT INTO pep_auth_roles (id, name, description) VALUES ('role1', 'testrole1', 'des');
INSERT INTO pep_auth_roles (id, name) VALUES ('role2', 'testrole2');
INSERT INTO pep_auth_users_roles (user_id, role_id) VALUES ('user2', 'role2');
INSERT INTO pep_auth_users_roles (user_id, role_id) VALUES ('user3', 'role2');
INSERT INTO pep_auth_users_roles (user_id, role_id) VALUES ('user1', 'role1');

INSERT INTO pep_auth_usergroups (id, name, description, seq) VALUES ('group1', 'testgroup1', 'testgroup1', 1);
INSERT INTO pep_auth_usergroups (id, name, description, seq) VALUES ('group2', 'testgroup2', 'testgroup2', 2);

INSERT INTO PEP_AUTH_GROUPS_USERS (USER_GROUP_ID, USER_ID) VALUES ('group1', 'user1');
INSERT INTO PEP_AUTH_GROUPS_USERS (USER_GROUP_ID, USER_ID) VALUES ('group2', 'user2');
INSERT INTO PEP_AUTH_GROUPS_USERS (USER_GROUP_ID, USER_ID) VALUES ('group2', 'user3');
