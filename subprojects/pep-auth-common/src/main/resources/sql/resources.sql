INSERT INTO pep_auth_resources (id, name, url, method) VALUES ('pep-auth-users-c', '创建用户', '/auth/users', 'POST');
INSERT INTO pep_auth_resources (id, name, url, method) VALUES ('pep-auth-users-r', '检索用户', '/auth/users*', 'GET');
INSERT INTO pep_auth_resources (id, name, url, method) VALUES ('pep-auth-users-g', '获得用户', '/auth/users/*', 'GET');
INSERT INTO pep_auth_resources (id, name, url, method) VALUES ('pep-auth-users-u', '更新用户', '/auth/users/*', 'PUT');
INSERT INTO pep_auth_resources (id, name, url, method) VALUES ('pep-auth-users-d', '删除用户', '/auth/users/*', 'DELETE');

COMMIT;
