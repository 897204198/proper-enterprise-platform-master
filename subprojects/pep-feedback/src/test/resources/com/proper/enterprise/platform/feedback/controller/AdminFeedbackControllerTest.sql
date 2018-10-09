-- 意见反馈状态
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order, is_default) VALUES ('pep_feedback_not_reply', 'FEEDBACK_STATUS', '0',
'未反馈', 0, 'Y');
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order) VALUES ('pep_feedback_replied', 'FEEDBACK_STATUS', '1', '已反馈', 1);
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order) VALUES ('pep_feedback_colsed', 'FEEDBACK_STATUS', '2', '已关闭', 2);
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order) VALUES ('pep_feedback_all', 'FEEDBACK_STATUS', '-1', '全部', -1);
-- 用户
INSERT INTO pep_auth_users (id, username, password, superuser, pep_dtype, name, phone, email, enable) VALUES ('admin', 'testuser1', 'pwd', 'Y', 'UserEntity', 'c', '12345678901', 'test1@test.com', 'Y');
INSERT INTO pep_auth_users (id, username, password, superuser, pep_dtype, name, phone, email, enable) VALUES ('user2', 'testuser2', 'pwd', 'F','UserEntity', 'b', '12345678902', 'test2@test.com', 'Y');
INSERT INTO pep_auth_users (id, username, password, superuser, pep_dtype, name, phone, email, enable) VALUES ('user3', 'testuser3', 'e10adc3949ba59abbe56e057f20f883e', 'N', 'UserEntity', 'a', '12345678903', 'test3@test.com', 'Y');
