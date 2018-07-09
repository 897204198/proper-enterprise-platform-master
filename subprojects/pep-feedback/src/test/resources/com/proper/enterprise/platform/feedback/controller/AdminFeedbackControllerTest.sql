-- 意见反馈状态
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order, is_default) VALUES ('pep_feedback_not_reply', 'FEEDBACK_STATUS', '0',
'未反馈', 0, 'Y');
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order) VALUES ('pep_feedback_replied', 'FEEDBACK_STATUS', '1', '已反馈', 1);
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order) VALUES ('pep_feedback_colsed', 'FEEDBACK_STATUS', '2', '已关闭', 2);
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order) VALUES ('pep_feedback_all', 'FEEDBACK_STATUS', '-1', '全部', -1);