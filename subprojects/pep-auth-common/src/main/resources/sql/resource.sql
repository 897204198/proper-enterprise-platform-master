INSERT INTO pep_auth_resource
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name, parent, moc, url, sequence_number)
VALUES
('BBD7A5193ECC493DB202A7F0DECF386C', 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', 'somp', '默认根资源', null, 'system', null, 0);

INSERT INTO pep_auth_resource
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name, parent, moc, url, sequence_number)
VALUES
('7E0960C6B1CC44959DCFB5E36D63F3E1', 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', 'somp.home', '首页', 'BBD7A5193ECC493DB202A7F0DECF386C', 'subsystem', '/index', 1);

COMMIT;