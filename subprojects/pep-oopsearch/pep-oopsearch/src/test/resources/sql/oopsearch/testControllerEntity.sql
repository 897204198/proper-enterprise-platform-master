INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('test-001', 'testcontroller', 'TEST_CONTROLLER_ENTITY', 'my_name', 'string', '我的名称', 'myName', '/testOopController/{myName}/istest/{id}');
INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('test-004', 'testcontroller', 'TEST_CONTROLLER_ENTITY', 'create_time', 'date', '人员创建时间', 'createTime', '/testOopController/{myName}/istest/{id}');
COMMIT;
