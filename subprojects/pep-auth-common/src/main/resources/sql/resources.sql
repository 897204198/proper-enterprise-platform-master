INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, parent_res_id, resource_type, url, method, sequence_number)
VALUES
('pep-auth', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', '安全设置', null, 'APP', '/platform/auth/security', 'GET', 0);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, parent_res_id, resource_type, url, method, sequence_number)
VALUES
('pep-auth-resources', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', '资源', 'pep-auth', 'MENU', '/platform/auth/security/resources', 'GET', 0);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, parent_res_id, resource_type, url, method, sequence_number)
VALUES
('pep-auth-roles', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', '角色', 'pep-auth', 'MENU', '/platform/auth/security/roles', 'GET', 1);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, parent_res_id, resource_type, url, method, sequence_number)
VALUES
('pep-auth-users', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', '用户', 'pep-auth', 'MENU', '/platform/auth/security/users', 'GET', 2);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, parent_res_id, resource_type, url, method, sequence_number)
VALUES
('pep-auth-users-c', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', '创建用户', 'pep-auth-users', 'API', '/auth/users', 'POST', 0);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, parent_res_id, resource_type, url, method, sequence_number)
VALUES
('pep-auth-users-r', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', '检索用户', 'pep-auth-users', 'API', '/auth/users*', 'GET', 1);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, parent_res_id, resource_type, url, method, sequence_number)
VALUES
('pep-auth-users-g', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', '获得用户', 'pep-auth-users', 'API', '/auth/users/*', 'GET', 2);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, parent_res_id, resource_type, url, method, sequence_number)
VALUES
('pep-auth-users-u', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', '更新用户', 'pep-auth-users', 'API', '/auth/users/*', 'PUT', 3);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, parent_res_id, resource_type, url, method, sequence_number)
VALUES
('pep-auth-users-d', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', '删除用户', 'pep-auth-users', 'API', '/auth/users/*', 'DELETE', 4);

COMMIT;
