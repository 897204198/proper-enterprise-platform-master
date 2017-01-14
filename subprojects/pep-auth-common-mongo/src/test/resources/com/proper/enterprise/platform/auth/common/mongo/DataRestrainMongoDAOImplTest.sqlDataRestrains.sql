INSERT INTO pep_auth_resources (id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, url, method, valid) VALUES ('pep-auth', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', '安全设置', '/platform/auth/security', 'GET', 'Y');
INSERT INTO pep_auth_datarestrains (id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, table_name, sql_str, valid) VALUES ('ds1', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', 'ds1', 'drmongodao', '{$where: "this.a > this.b"}', 'Y');
INSERT INTO pep_auth_datarestrains (id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, table_name, sql_str, valid) VALUES ('ds2', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', 'ds2', 'drmongodao', '{a: {"$ne": 3}}', 'Y');
INSERT INTO pep_auth_datarestrains (id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, table_name, filter_name, valid) VALUES ('ds3', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', 'ds3', 'drmongodao', 'filter may not has sql', 'Y');
INSERT INTO pep_auth_datarestrains (id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, table_name, sql_str, valid) VALUES ('ds4', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', 'ds4', 'drmongodao', '#{@mockUserService.get("notExist")}', 'Y');
INSERT INTO pep_auth_resources_datarestrains (resource_id, datarestrain_id) VALUES ('pep-auth', 'ds1');
INSERT INTO pep_auth_resources_datarestrains (resource_id, datarestrain_id) VALUES ('pep-auth', 'ds2');
INSERT INTO pep_auth_resources_datarestrains (resource_id, datarestrain_id) VALUES ('pep-auth', 'ds3');
INSERT INTO pep_auth_resources_datarestrains (resource_id, datarestrain_id) VALUES ('pep-auth', 'ds4');
