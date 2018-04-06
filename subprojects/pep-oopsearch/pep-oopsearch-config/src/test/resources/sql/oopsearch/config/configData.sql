INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('001', 'demouser', 'demo_user', 'user_id', 'string', '用户id', 'demouser_user_id', '/demouser');
INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('002', 'demouser', 'demo_user', 'user_name', 'string', '用户名', 'demouser_user_name', '/demouser');
INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('003', 'demouser', 'demo_user', 'age', 'int', '年龄', 'demouser_age', '/demouser');
INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('004', 'demouser', 'demo_user', 'create_time', 'date', '人员创建时间' , 'demouser_create_time', '/demouser');
COMMIT;
