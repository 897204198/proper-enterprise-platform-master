INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, parent_res_id, resource_type, url, method, sequence_number)
VALUES
('pep-auth', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', '安全设置', null, 'APP', '/platform/auth/security', 'GET', 0);

INSERT INTO pep_auth_datarestrains
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, table_name, sql_str)
VALUES
('ds1', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', 'ds1', 'drmongodao', '{$where: "this.a > this.b"}');

INSERT INTO pep_auth_datarestrains
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, table_name, sql_str)
VALUES
('ds2', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', 'ds2', 'drmongodao', '{a: {"$ne": 3}}');

INSERT INTO pep_auth_datarestrains
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, table_name, filter_name)
VALUES
('ds3', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', 'ds3', 'drmongodao', 'filter may not has sql');

INSERT INTO pep_auth_datarestrains
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, table_name, sql_str)
VALUES
('ds4', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', 'ds4', 'drmongodao', '#{@mockUserService.get("notExist")}');

INSERT INTO pep_auth_resources_datarestrains
(resource_id, datarestrain_id)
VALUES
('pep-auth', 'ds1');

INSERT INTO pep_auth_resources_datarestrains
(resource_id, datarestrain_id)
VALUES
('pep-auth', 'ds2');

INSERT INTO pep_auth_resources_datarestrains
(resource_id, datarestrain_id)
VALUES
('pep-auth', 'ds3');

INSERT INTO pep_auth_resources_datarestrains
(resource_id, datarestrain_id)
VALUES
('pep-auth', 'ds4');
