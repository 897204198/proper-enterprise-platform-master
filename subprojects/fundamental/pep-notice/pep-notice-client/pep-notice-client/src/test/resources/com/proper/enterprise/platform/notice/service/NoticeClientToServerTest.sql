INSERT INTO pep_auth_users (id, username, password, superuser, pep_dtype, name, phone, email, enable) VALUES ('test1', 'wanghp', 'pwd', 'Y', 'UserEntity', '王浩鵬', '18610294487', 'wanghaopeng@propersoft.cn', 'Y');
INSERT INTO pep_auth_users (id, username, password, superuser, pep_dtype, name, phone, email, enable) VALUES ('test2', 'test2', 'pwd', 'Y', 'UserEntity', 'c', '12345678902', 'test1@test2.com', 'Y');
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order) VALUES ('notice_server_url', 'NOTICE_SERVER', 'URL', 'http://172.168.3.221:8081/pep', 0);
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order) VALUES ('notice_server_token', 'NOTICE_SERVER', 'TOKEN', 'testAppToken2', 1);
