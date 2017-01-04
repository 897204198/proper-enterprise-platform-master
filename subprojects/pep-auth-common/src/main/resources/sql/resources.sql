INSERT INTO pep_auth_resources (id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, url, method) VALUES ('pep-auth-users-c', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', '创建用户', '/auth/users', 'POST');
INSERT INTO pep_auth_resources (id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, url, method) VALUES ('pep-auth-users-r', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', '检索用户', '/auth/users*', 'GET');
INSERT INTO pep_auth_resources (id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, url, method) VALUES ('pep-auth-users-g', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', '获得用户', '/auth/users/*', 'GET');
INSERT INTO pep_auth_resources (id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, url, method) VALUES ('pep-auth-users-u', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', '更新用户', '/auth/users/*', 'PUT');
INSERT INTO pep_auth_resources (id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, url, method) VALUES ('pep-auth-users-d', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', '删除用户', '/auth/users/*', 'DELETE');

COMMIT;
