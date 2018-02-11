INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order, is_default) VALUES ('search_day', 'DATE_TYPE', 'day', '日', 0, 'Y');
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order) VALUES ('search_tian', 'DATE_TYPE', 'day', '天', 1);
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order) VALUES ('search_week', 'DATE_TYPE', 'week', '周', 2);
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order) VALUES ('search_month', 'DATE_TYPE', 'month', '月', 3);
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order) VALUES ('search_quarter', 'DATE_TYPE', 'quarter', '季', 4);
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order) VALUES ('search_jidu', 'DATE_TYPE', 'quarter', '季度', 5);
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order) VALUES ('search_year', 'DATE_TYPE', 'year', '年', 6);

INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order, is_default) VALUES ('search_qian', 'TIME_AXIS', '-2', '前', 0, 'Y');
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order) VALUES ('search_zuo', 'TIME_AXIS', '-1', '昨', 1);
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order) VALUES ('search_shang', 'TIME_AXIS', '-1', '上', 2);
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order) VALUES ('search_qu', 'TIME_AXIS', '-1', '去', 3);
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order) VALUES ('search_jin', 'TIME_AXIS', '0', '今', 4);
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order) VALUES ('search_ben', 'TIME_AXIS', '0', '本', 5);
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order) VALUES ('search_zhe', 'TIME_AXIS', '0', '这', 6);
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order) VALUES ('search_ming', 'TIME_AXIS', '1', '明', 7);
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order) VALUES ('search_xia', 'TIME_AXIS', '1', '下', 8);
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order) VALUES ('search_hou', 'TIME_AXIS', '2', '后', 9);
COMMIT;
