INSERT INTO pep_auth_resources (id, name, url, method, resource_type) VALUES ('7E0960C6B1CC44959DCFB5E36D63F3E2', '流程模型', '/workflow/service/model/*/json', 'GET', 'RESOURCE_TYPE;0');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type) VALUES ('7E0960C6B1CC44959DCFB5E36D63F3E3', '流程', '/workflow/**', 'GET', 'RESOURCE_TYPE;0');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type) VALUES ('20151229164416', '流程模型列表', '/workflow/service/repository/models', 'GET', 'RESOURCE_TYPE;0');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type) VALUES ('20151229164555', '创建流程模型', '/workflow/service/repository/models/**', 'POST', 'RESOURCE_TYPE;0');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type) VALUES ('20151229164625', '查询流程模型', '/workflow/service/repository/models/**', 'GET', 'RESOURCE_TYPE;0');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type) VALUES ('20151229164700', '更新流程模型', '/workflow/service/repository/models/**', 'PUT', 'RESOURCE_TYPE;0');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type) VALUES ('20151229164730', '删除流程模型', '/workflow/service/repository/models/**', 'DELETE', 'RESOURCE_TYPE;0');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type) VALUES ('20160120164230', '流程实例列表', '/workflow/service/runtime/process-instances', 'GET', 'RESOURCE_TYPE;0');

COMMIT;
