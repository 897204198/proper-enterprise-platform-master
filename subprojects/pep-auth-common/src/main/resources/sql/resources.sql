INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name, parent, moc, url, method, sequence_number)
VALUES
('BBD7A5193ECC493DB202A7F0DECF386C', 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', 'root', '默认根资源', null, 'SYSTEM', '/', 'GET', 0);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name, parent, moc, url, method, sequence_number)
VALUES
('7E0960C6B1CC44959DCFB5E36D63F3E1', 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', 'auth.resources', '资源', 'BBD7A5193ECC493DB202A7F0DECF386C', 'SUBSYSTEM', '/auth/resources', 'GET', 1);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name, parent, moc, url, method, sequence_number)
VALUES
('7E0960C6B1CC44959DCFB5E36D63F3E2', 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', 'workflow.service.model', '流程模型', 'BBD7A5193ECC493DB202A7F0DECF386C', 'SUBSYSTEM', '/workflow/service/model/*/json', 'GET', 2);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name, parent, moc, url, method, sequence_number)
VALUES
('7E0960C6B1CC44959DCFB5E36D63F3E3', 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', 'workflow', '流程', 'BBD7A5193ECC493DB202A7F0DECF386C', 'SUBSYSTEM', '/workflow/**', 'GET', 3);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name, parent, moc, url, method, sequence_number)
VALUES
('20151229164416', 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', 'workflow.models', '流程模型列表', 'BBD7A5193ECC493DB202A7F0DECF386C', 'API', '/workflow/service/repository/models', 'GET', 1);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name, parent, moc, url, method, sequence_number)
VALUES
('20151229164555', 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', 'workflow.model', '创建流程模型', 'BBD7A5193ECC493DB202A7F0DECF386C', 'API', '/workflow/service/repository/models/**', 'POST', 2);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name, parent, moc, url, method, sequence_number)
VALUES
('20151229164625', 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', 'workflow.model', '查询流程模型', 'BBD7A5193ECC493DB202A7F0DECF386C', 'API', '/workflow/service/repository/models/**', 'GET', 3);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name, parent, moc, url, method, sequence_number)
VALUES
('20151229164700', 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', 'workflow.model', '更新流程模型', 'BBD7A5193ECC493DB202A7F0DECF386C', 'API', '/workflow/service/repository/models/**', 'PUT', 4);

INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name, parent, moc, url, method, sequence_number)
VALUES
('20151229164730', 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', 'workflow.model', '删除流程模型', 'BBD7A5193ECC493DB202A7F0DECF386C', 'API', '/workflow/service/repository/models/**', 'DELETE', 5);

COMMIT;