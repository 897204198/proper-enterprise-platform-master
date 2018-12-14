INSERT INTO pep_auth_users (id, username, password, superuser, pep_dtype, name, phone, email, enable,avatar) VALUES ('user1', 'testuser1', 'e10adc3949ba59abbe56e057f20f883e', 'Y', 'UserEntity', 'c', '12345678901', 'test1@test.com', 'Y','avatar');
INSERT INTO pep_auth_users (id, username, password, superuser, pep_dtype, name, phone, email, enable) VALUES ('user2', 'testuser2', 'e10adc3949ba59abbe56e057f20f883e', 'N',
'UserEntity', 'b', '12345678902', 'test2@test.com', 'Y');
INSERT INTO pep_auth_users (id, username, password, superuser, pep_dtype, name, phone, email, enable) VALUES ('user3', 'testuser3', 'e10adc3949ba59abbe56e057f20f883e', 'N', 'UserEntity', 'a', '12345678903', 'test3@test.com', 'Y');

INSERT INTO pep_auth_roles (id, name, description) VALUES ('role4', 'testrole4', 'des');
INSERT INTO pep_auth_roles (id, name, description,PARENT_ID) VALUES ('role1', 'testrole1', 'des','role4');
INSERT INTO pep_auth_roles (id, name) VALUES ('role2', 'testrole2');
INSERT INTO pep_auth_users_roles (user_id, role_id) VALUES ('user2', 'role2');
INSERT INTO pep_auth_users_roles (user_id, role_id) VALUES ('user3', 'role2');
INSERT INTO pep_auth_users_roles (user_id, role_id) VALUES ('user1', 'role1');

INSERT INTO pep_auth_usergroups (id, name, description, seq) VALUES ('group1', 'testgroup1', 'testgroup1', 1);
INSERT INTO pep_auth_usergroups (id, name, description, seq,enable) VALUES ('group2', 'testgroup2', 'testgroup2', 2,'N');

INSERT INTO PEP_AUTH_USERS_GROUPS (USER_ID,USER_GROUP_ID ) VALUES ('user1','group1' );
INSERT INTO PEP_AUTH_USERS_GROUPS (USER_ID,USER_GROUP_ID ) VALUES ('user1','group2' );
INSERT INTO PEP_AUTH_USERS_GROUPS (USER_ID,USER_GROUP_ID ) VALUES ('user3','group2' );
