INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name, parent, resource_type, url, method, sequence_number)
VALUES
('7E0960C6B1CC44959DCFB5E36D63F3E2', 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', 'workflow.service.model', '流程模型', 'BBD7A5193ECC493DB202A7F0DECF386C', 'SUBSYSTEM', '/workflow/service/model/*/json', 'GET', 2);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name, parent, resource_type, url, method, sequence_number)
VALUES
('7E0960C6B1CC44959DCFB5E36D63F3E3', 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', 'workflow', '流程', 'BBD7A5193ECC493DB202A7F0DECF386C', 'SUBSYSTEM', '/workflow/**', 'GET', 3);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name, parent, resource_type, url, method, sequence_number)
VALUES
('20151231135516', 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', 'workflow.designer', '流程设计器', 'BBD7A5193ECC493DB202A7F0DECF386C', 'MENU', '/workflow/designer', 'GET', 1);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name, parent, resource_type, url, method, sequence_number)
VALUES
('20151229164416', 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', 'workflow.designer.models', '流程模型列表', 'BBD7A5193ECC493DB202A7F0DECF386C', 'API', '/workflow/service/repository/models', 'GET', 1);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name, parent, resource_type, url, method, sequence_number)
VALUES
('20151229164555', 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', 'workflow.designer.model', '创建流程模型', 'BBD7A5193ECC493DB202A7F0DECF386C', 'API', '/workflow/service/repository/models/**', 'POST', 2);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name, parent, resource_type, url, method, sequence_number)
VALUES
('20151229164625', 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', 'workflow.designer.model', '查询流程模型', 'BBD7A5193ECC493DB202A7F0DECF386C', 'API', '/workflow/service/repository/models/**', 'GET', 3);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name, parent, resource_type, url, method, sequence_number)
VALUES
('20151229164700', 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', 'workflow.designer.model', '更新流程模型', 'BBD7A5193ECC493DB202A7F0DECF386C', 'API', '/workflow/service/repository/models/**', 'PUT', 4);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name, parent, resource_type, url, method, sequence_number)
VALUES
('20151229164730', 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', 'workflow.designer.model', '删除流程模型', 'BBD7A5193ECC493DB202A7F0DECF386C', 'API', '/workflow/service/repository/models/**', 'DELETE', 5);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name, parent, resource_type, url, method, sequence_number)
VALUES
('20160120164030', 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', 'workflow.deployments', '已部流程', 'BBD7A5193ECC493DB202A7F0DECF386C', 'MENU', '/workflow/deployments', 'GET', 1);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name, parent, resource_type, url, method, sequence_number)
VALUES
('20160120164230', 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', 'workflow.deployments.list', '部署列表', 'BBD7A5193ECC493DB202A7F0DECF386C', 'API', '/workflow/service/repository/deployments', 'GET', 1);

COMMIT;