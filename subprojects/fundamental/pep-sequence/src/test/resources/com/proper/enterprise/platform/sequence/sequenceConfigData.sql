INSERT INTO pep_sequence (id, sequence_code, sequence_name, formula, clear_type) VALUES ('001', 'generatetest1', '测试1', 'HT${date:yyyyMM}${length:3}', 'CLEAR_TYPE;MONTH_CLEAR');
INSERT INTO pep_sequence (id, sequence_code, sequence_name, formula, clear_type) VALUES ('002', 'generatetest2', '测试2', '${length:5}', 'CLEAR_TYPE;NO_CLEAR');

-- 数据字典清零方式
INSERT INTO PEP_SYS_DATA_DIC (ID, DD_CATALOG, DD_CODE, DD_NAME, DD_ORDER, IS_DEFAULT) VALUES ('pep_sequence_year_clear', 'CLEAR_TYPE', 'YEAR_CLEAR', '年清零', 3, 'N');
INSERT INTO PEP_SYS_DATA_DIC (ID, DD_CATALOG, DD_CODE, DD_NAME, DD_ORDER, IS_DEFAULT) VALUES ('pep_sequence_month_clear', 'CLEAR_TYPE', 'MONTH_CLEAR', '月清零', 2, 'N');
INSERT INTO PEP_SYS_DATA_DIC (ID, DD_CATALOG, DD_CODE, DD_NAME, DD_ORDER, IS_DEFAULT) VALUES ('pep_sequence_day_clear', 'CLEAR_TYPE', 'DAY_CLEAR', '日清零', 1, 'N');
INSERT INTO PEP_SYS_DATA_DIC (ID, DD_CATALOG, DD_CODE, DD_NAME, DD_ORDER, IS_DEFAULT) VALUES ('pep_sequence_no_clear', 'CLEAR_TYPE', 'NO_CLEAR', '不清零', 0, 'Y');
