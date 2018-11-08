-- 用户
INSERT INTO pep_auth_users (id, username, password, superuser, pep_dtype, name, phone, email, enable) VALUES ('admin', 'testuser1', 'pwd', 'Y', 'UserEntity', 'c', '12345678901', 'test1@test.com', 'Y');
INSERT INTO pep_auth_users (id, username, password, superuser, pep_dtype, name, phone, email, enable) VALUES ('user2', 'testuser2', 'pwd', 'F','UserEntity', 'b', '12345678902', 'test2@test.com', 'Y');
INSERT INTO pep_auth_users (id, username, password, superuser, pep_dtype, name, phone, email, enable) VALUES ('user3', 'testuser3', 'e10adc3949ba59abbe56e057f20f883e', 'N', 'UserEntity', 'a', '12345678903', 'test3@test.com', 'Y');
-- 角色
INSERT INTO pep_auth_roles (id, name, description) VALUES ('role1', 'testrole1', 'des');
INSERT INTO pep_auth_roles (id, name) VALUES ('role2', 'testrole2');
INSERT INTO pep_auth_roles (id, name, description) VALUES ('role3', 'testrole3', 'des3');
INSERT INTO pep_auth_roles (id, name, description) VALUES ('role4', 'testrole4', 'des4');
INSERT INTO pep_auth_roles (id, name, description) VALUES ('role5', 'testrole5', 'des5');
INSERT INTO pep_auth_roles (id, name, description) VALUES ('role6', 'testrole6', 'des6');
INSERT INTO pep_auth_roles (id, name, description,enable) VALUES ('role7', 'testrole7', 'des7','N');
INSERT INTO pep_auth_roles (id, name, description,enable) VALUES ('role8', 'testrole8', 'des8','Y');
-- 用户角色关联
INSERT INTO pep_auth_users_roles (user_id, role_id) VALUES ('user2', 'role2');
INSERT INTO pep_auth_users_roles (user_id, role_id) VALUES ('user3', 'role2');
INSERT INTO pep_auth_users_roles (user_id, role_id) VALUES ('admin', 'role1');
-- 用户组
INSERT INTO pep_auth_usergroups (id, name, description, seq) VALUES ('group1', 'testgroup1', 'testgroup1', 1);
INSERT INTO pep_auth_usergroups (id, name, description, seq) VALUES ('group2', 'testgroup2', 'testgroup2', 2);
INSERT INTO pep_auth_usergroups (id, name, description, seq) VALUES ('group3', 'testgroup3', 'testgroup3', 3);
-- 用户用户组关联
INSERT INTO PEP_AUTH_USERS_GROUPS (USER_ID,USER_GROUP_ID ) VALUES ('admin','group1' );
INSERT INTO PEP_AUTH_USERS_GROUPS (USER_ID,USER_GROUP_ID ) VALUES ('user2','group2' );
INSERT INTO PEP_AUTH_USERS_GROUPS (USER_ID,USER_GROUP_ID ) VALUES ('user3','group2' );
-- 菜单
INSERT INTO PEP_AUTH_MENUS (id, name, route, SEQUENCE_NUMBER) VALUES ('999', 'menu1', '/test3', 2);
INSERT INTO PEP_AUTH_MENUS (id, name, route, SEQUENCE_NUMBER,PARENT_ID) VALUES ('998', 'menu2', '/test4', 2,'999');
INSERT INTO PEP_AUTH_MENUS (id, name, route, SEQUENCE_NUMBER,ENABLE,PARENT_ID) VALUES ('997', 'menu3', '/test5', 2,'N','999');
-- 资源
INSERT INTO PEP_AUTH_RESOURCES (id, name, URL, METHOD) VALUES ('996', 'resources1', '/resource1', 'GET');
INSERT INTO PEP_AUTH_RESOURCES (id, name, URL, METHOD) VALUES ('995', 'resources2', '/resource2', 'GET');
INSERT INTO PEP_AUTH_RESOURCES (id, name, URL, METHOD) VALUES ('994', 'resources3', '/auth/resources/*/roles', 'GET');
INSERT INTO PEP_AUTH_RESOURCES (id, name, URL, METHOD,enable) VALUES ('993', 'resources4', '/auth/resources/', 'GET','n');
INSERT INTO PEP_AUTH_RESOURCES (id, name, URL, METHOD) VALUES ('992', 'resources5', '/auth/resources/**/roles', 'GET');
INSERT INTO PEP_AUTH_RESOURCES (id, name, URL, METHOD) VALUES ('991', 'resources6', '/auth/resources/aaa/*', 'GET');
-- 角色与资源
INSERT INTO pep_auth_roles_resources (ROLE_ID, RESOURCE_ID) VALUES ('role3', '996');
-- 用户组与角色
INSERT INTO pep_auth_groups_roles (GROUP_ID, ROLE_ID) VALUES ('group1', 'role3');
INSERT INTO pep_auth_groups_roles (GROUP_ID, ROLE_ID) VALUES ('group2', 'role4');
-- 角色与菜单
INSERT INTO pep_auth_roles_menus (ROLE_ID, MENU_ID) VALUES ('role8', '999');
INSERT INTO pep_auth_roles_menus (ROLE_ID, MENU_ID) VALUES ('role2', '999');
-- 菜单资源
INSERT INTO pep_auth_menus_resources (MENU_ID, RES_ID) VALUES ('999', '992');


