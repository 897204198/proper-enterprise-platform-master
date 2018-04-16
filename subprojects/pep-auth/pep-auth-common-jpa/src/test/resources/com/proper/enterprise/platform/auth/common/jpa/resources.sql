INSERT INTO pep_auth_resources (id, name, url, method, identifier) VALUES ('test-c', '增', '/auth/test', 'POST', 'add');
INSERT INTO pep_auth_resources (id, name, url, method, identifier) VALUES ('test-r', '检索', '/auth/test', 'GET', 'find');
INSERT INTO pep_auth_resources (id, name, url, method, identifier) VALUES ('test-g', '查询', '/auth/test/*', 'GET', 'find');
INSERT INTO pep_auth_resources (id, name, url, method, identifier) VALUES ('test-u', '改', '/auth/test/*', 'PUT', 'edit');
INSERT INTO pep_auth_resources (id, name, url, method, identifier) VALUES ('test-d', '删', '/auth/test/*', 'DELETE', 'delete');
INSERT INTO pep_auth_resources (id, name, url, method, identifier) VALUES ('test', '无菜单', '/test', 'GET', 'get');
INSERT INTO pep_auth_resources (id, name, url, method, identifier) VALUES ('test1', '无角色', '/test1', 'GET', 'get');
INSERT INTO pep_auth_resources (id, name, url, method, identifier) VALUES ('test-menus', '查菜单', '/auth/menus', 'GET', 'get');
INSERT INTO pep_auth_resources (id, name, url, method, enable, valid, identifier) VALUES ('test-enable', '启用停用', '/test-enable', 'GET', 'N', 'Y',
'get');
INSERT INTO pep_auth_resources (id, name, url, method, enable, valid, identifier) VALUES ('test-valid', '逻辑删除', '/test-valid', 'GET', 'Y', 'N',
'get');
