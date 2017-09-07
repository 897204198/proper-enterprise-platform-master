INSERT INTO pep_auth_resources (id, name, url, method) VALUES ('7E0960C6B1CC44959DCFB5E36D63F3E2', '流程模型', '/workflow/service/model/*/json', 'GET');
INSERT INTO pep_auth_resources (id, name, url, method) VALUES ('7E0960C6B1CC44959DCFB5E36D63F3E3', '流程', '/workflow/**', 'GET');
INSERT INTO pep_auth_resources (id, name, url, method) VALUES ('20151229164416', '流程模型列表', '/workflow/service/repository/models', 'GET');
INSERT INTO pep_auth_resources (id, name, url, method) VALUES ('20151229164555', '创建流程模型', '/workflow/service/repository/models/**', 'POST');
INSERT INTO pep_auth_resources (id, name, url, method) VALUES ('20151229164625', '查询流程模型', '/workflow/service/repository/models/**', 'GET');
INSERT INTO pep_auth_resources (id, name, url, method) VALUES ('20151229164700', '更新流程模型', '/workflow/service/repository/models/**', 'PUT');
INSERT INTO pep_auth_resources (id, name, url, method) VALUES ('20151229164730', '删除流程模型', '/workflow/service/repository/models/**', 'DELETE');
INSERT INTO pep_auth_resources (id, name, url, method) VALUES ('20160120164230', '流程实例列表', '/workflow/service/runtime/process-instances', 'GET');

COMMIT;
