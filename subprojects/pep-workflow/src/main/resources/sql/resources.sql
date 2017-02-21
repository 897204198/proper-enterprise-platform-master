INSERT INTO pep_auth_resources (id, name, parent_res_id, resource_type, url, method, sequence_number) VALUES ('BBD7A5193ECC493DB202A7F0DECF386C', '默认根资源', null, 'MODULE', '/', 'GET', 0);
INSERT INTO pep_auth_resources (id, name, parent_res_id, resource_type, url, method, sequence_number) VALUES ('7E0960C6B1CC44959DCFB5E36D63F3E2', '流程模型', 'BBD7A5193ECC493DB202A7F0DECF386C', 'MODULE', '/workflow/service/model/*/json', 'GET', 2);
INSERT INTO pep_auth_resources (id, name, parent_res_id, resource_type, url, method, sequence_number) VALUES ('7E0960C6B1CC44959DCFB5E36D63F3E3', '流程', 'BBD7A5193ECC493DB202A7F0DECF386C', 'MODULE', '/workflow/**', 'GET', 3);
INSERT INTO pep_auth_resources (id, name, parent_res_id, resource_type, url, method, sequence_number) VALUES ('20151231135516', '流程设计器', 'BBD7A5193ECC493DB202A7F0DECF386C', 'MENU', '/workflow/designer', 'GET', 1);
INSERT INTO pep_auth_resources (id, name, parent_res_id, resource_type, url, method, sequence_number) VALUES ('20151229164416', '流程模型列表', 'BBD7A5193ECC493DB202A7F0DECF386C', 'API', '/workflow/service/repository/models', 'GET', 1);
INSERT INTO pep_auth_resources (id, name, parent_res_id, resource_type, url, method, sequence_number) VALUES ('20151229164555', '创建流程模型', 'BBD7A5193ECC493DB202A7F0DECF386C', 'API', '/workflow/service/repository/models/**', 'POST', 2);
INSERT INTO pep_auth_resources (id, name, parent_res_id, resource_type, url, method, sequence_number) VALUES ('20151229164625', '查询流程模型', 'BBD7A5193ECC493DB202A7F0DECF386C', 'API', '/workflow/service/repository/models/**', 'GET', 3);
INSERT INTO pep_auth_resources (id, name, parent_res_id, resource_type, url, method, sequence_number) VALUES ('20151229164700', '更新流程模型', 'BBD7A5193ECC493DB202A7F0DECF386C', 'API', '/workflow/service/repository/models/**', 'PUT', 4);
INSERT INTO pep_auth_resources (id, name, parent_res_id, resource_type, url, method, sequence_number) VALUES ('20151229164730', '删除流程模型', 'BBD7A5193ECC493DB202A7F0DECF386C', 'API', '/workflow/service/repository/models/**', 'DELETE', 5);
INSERT INTO pep_auth_resources (id, name, parent_res_id, resource_type, url, method, sequence_number) VALUES ('20160120164030', '流程实例', 'BBD7A5193ECC493DB202A7F0DECF386C', 'MENU', '/workflow/instances', 'GET', 1);
INSERT INTO pep_auth_resources (id, name, parent_res_id, resource_type, url, method, sequence_number) VALUES ('20160120164230', '流程实例列表', 'BBD7A5193ECC493DB202A7F0DECF386C', 'API', '/workflow/service/runtime/process-instances', 'GET', 1);
