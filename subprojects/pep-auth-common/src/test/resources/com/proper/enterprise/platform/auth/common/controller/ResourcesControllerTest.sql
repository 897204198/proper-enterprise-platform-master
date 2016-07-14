INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, parent_res_id, resource_type, url, method, sequence_number)
VALUES
('test-res1', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', '安全设置1', null, 'APP', '/platform/auth/security', 'POST', 0);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, parent_res_id, resource_type, url, method, sequence_number)
VALUES
('test-res2', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', '安全设置2', null, 'MENU', '/platform/auth/security', 'GET', 1);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, parent_res_id, resource_type, url, method, sequence_number)
VALUES
('test-res3', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', '安全设置3', null, 'API', '/platform/auth/security', 'PUT', 2);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, parent_res_id, resource_type, url, method, sequence_number)
VALUES
('test-res4', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', '安全设置4', null, 'MENU', '/platform/auth/security', 'DELETE', 3);

INSERT INTO pep_auth_users
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, username, password, superuser, pep_dtype)
VALUES
('test-user1', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', 'admin', 'e10adc3949ba59abbe56e057f20f883e', 'T', 'UserEntity');

INSERT INTO pep_auth_users
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, username, password, superuser, pep_dtype)
VALUES
('test-user2', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', 'user', 'e10adc3949ba59abbe56e057f20f883e', 'F', 'UserEntity');

INSERT INTO pep_auth_roles
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, name)
VALUES
('test-role1', 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', 'testrole1');

INSERT INTO pep_auth_users_roles
(user_id, role_id)
VALUES
('test-user2', 'test-role1');

INSERT INTO pep_auth_roles_resources
(role_id, resource_id)
VALUES
('test-role1', 'test-res3');

INSERT INTO pep_auth_roles_resources
(role_id, resource_id)
VALUES
('test-role1', 'test-res4');
