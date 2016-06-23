INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, parent_res_id, resource_type, url, method, sequence_number)
VALUES
('BBD7A5193ECC493DB202A7F0DECF386C', 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', '默认根资源', null, 'MODULE', '/', 'GET', 0);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, parent_res_id, resource_type, url, method, sequence_number)
VALUES
('14342b22-222d-42b2-900e-e4b6a45593c3', 'SYS', '2015-08-18 09:38:00', 'SYS', '2015-08-18 09:38:00', '平台权限模块', null, 'MODULE', '/platform/auth', 'GET', 0);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, parent_res_id, resource_type, url, method, sequence_number)
VALUES
('e92d4ac4-9439-4fa9-90d0-e65a05a76f33', 'SYS', '2015-08-18 09:38:00', 'SYS', '2015-08-18 09:38:00', '安全设置', '14342b22-222d-42b2-900e-e4b6a45593c3', 'APP', '/platform/auth/security', 'GET', 0);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, parent_res_id, resource_type, url, method, sequence_number)
VALUES
('c2aa6f9d-33ed-4229-b1bf-cf41f9223d40', 'SYS', '2015-08-18 09:38:00', 'SYS', '2015-08-18 09:38:00', '资源', 'e92d4ac4-9439-4fa9-90d0-e65a05a76f33', 'MENU', '/platform/auth/security/resources', 'GET', 0);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, parent_res_id, resource_type, url, method, sequence_number)
VALUES
('33a43d82-1276-401b-ac55-f88e1fd6ebcf', 'SYS', '2015-08-18 09:38:00', 'SYS', '2015-08-18 09:38:00', '角色', 'e92d4ac4-9439-4fa9-90d0-e65a05a76f33', 'MENU', '/platform/auth/security/roles', 'GET', 1);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, parent_res_id, resource_type, url, method, sequence_number)
VALUES
('224d5ba7-212f-4924-9bf6-6a1fe16bed58', 'SYS', '2015-08-18 09:38:00', 'SYS', '2015-08-18 09:38:00', '用户', 'e92d4ac4-9439-4fa9-90d0-e65a05a76f33', 'MENU', '/platform/auth/security/users', 'GET', 2);

COMMIT;
