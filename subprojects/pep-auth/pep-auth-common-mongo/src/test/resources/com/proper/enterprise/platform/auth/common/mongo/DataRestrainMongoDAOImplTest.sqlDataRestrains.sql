INSERT INTO pep_auth_resources (id, name, url, method) VALUES ('pep-auth', '安全设置', '/platform/auth/security', 'GET');
INSERT INTO pep_auth_datarestrains (id, name, table_name, sql_str) VALUES ('ds1', 'ds1', 'drmongodao', '{$where: "this.a > this.b"}');
INSERT INTO pep_auth_datarestrains (id, name, table_name, sql_str) VALUES ('ds2', 'ds2', 'drmongodao', '{a: {"$ne": 3}}');
INSERT INTO pep_auth_datarestrains (id, name, table_name, filter_name) VALUES ('ds3', 'ds3', 'drmongodao', 'filter may not has sql');
INSERT INTO pep_auth_datarestrains (id, name, table_name, sql_str) VALUES ('ds4', 'ds4', 'drmongodao', '#{@mockUserService.get("notExist")}');
INSERT INTO pep_auth_resources_datarestrains (resource_id, datarestrain_id) VALUES ('pep-auth', 'ds1');
INSERT INTO pep_auth_resources_datarestrains (resource_id, datarestrain_id) VALUES ('pep-auth', 'ds2');
INSERT INTO pep_auth_resources_datarestrains (resource_id, datarestrain_id) VALUES ('pep-auth', 'ds3');
INSERT INTO pep_auth_resources_datarestrains (resource_id, datarestrain_id) VALUES ('pep-auth', 'ds4');
