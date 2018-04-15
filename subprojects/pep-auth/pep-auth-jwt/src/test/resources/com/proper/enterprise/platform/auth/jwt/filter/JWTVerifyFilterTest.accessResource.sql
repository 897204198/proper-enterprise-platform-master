INSERT INTO pep_auth_resources (id, name, url, method) VALUES ('r1', 'test-res-1', '/jwt/filter/res/nomenu', 'GET');
INSERT INTO pep_auth_resources (id, name, url, method) VALUES ('r2', 'test-res-2', '/jwt/filter/res/menures', 'GET');
INSERT INTO pep_auth_menus (id, name, route, icon, parent_id, sequence_number) VALUES ('m1', 'test-menu', '/test/menu', null, null, '0');
INSERT INTO pep_auth_menus_resources (menu_id, res_id) VALUES ('m1', 'r2');
INSERT INTO pep_auth_users (id, username, password, superuser, pep_dtype) VALUES ('art', 'art', 'pwd', 'N', 'UserEntity');
