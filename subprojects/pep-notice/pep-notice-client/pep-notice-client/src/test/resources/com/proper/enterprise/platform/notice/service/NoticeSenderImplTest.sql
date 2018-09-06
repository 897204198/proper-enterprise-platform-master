INSERT INTO `pep_template`(`ID`, `CREATE_TIME`, `CREATE_USER_ID`, `LAST_MODIFY_TIME`, `LAST_MODIFY_USER_ID`, `ENABLE`, `CODE`, `NAME`,`TITLE`, `TEMPLATE`, `DESCRIPTION`,  `CATALOG`,  `TYPE`) VALUES ('idEndNotice', '2018-08-08 00:00:00.000', 'PEP', '2018-08-08 00:00:00.000', 'PEP', 'Y', 'EndCode', 'EndCode', '流程完结', '您的【{processDefinitionName}】已完成，详情请在【发起历史】中查看。',  '1',  'NOTICE_CATALOG;BPM',  'NOTICE_TYPE;EMAIL');
INSERT INTO `pep_template`(`ID`, `CREATE_TIME`, `CREATE_USER_ID`, `LAST_MODIFY_TIME`, `LAST_MODIFY_USER_ID`, `ENABLE`, `CODE`, `NAME`,`TITLE`, `TEMPLATE`, `DESCRIPTION`,  `CATALOG`,  `TYPE`) VALUES ('idEndNotice2', '2018-08-08 00:00:00.000', 'PEP', '2018-08-08 00:00:00.000', 'PEP', 'Y', 'EndCode', 'EndCode', '您有一条已办消息', '您的{processDefinitionName}已完成',  '1',  'NOTICE_CATALOG;BPM',  'NOTICE_TYPE;PUSH');
INSERT INTO `pep_template`(`ID`, `CREATE_TIME`, `CREATE_USER_ID`, `LAST_MODIFY_TIME`, `LAST_MODIFY_USER_ID`, `ENABLE`, `CODE`, `NAME`,`TITLE`, `TEMPLATE`, `DESCRIPTION`,  `CATALOG`,  `TYPE`) VALUES ('idEndNotice3', '2018-08-08 00:00:00.000', 'PEP', '2018-08-08 00:00:00.000', 'PEP', 'Y', 'EndCode', 'EndCode', 'test', 'test',  '1',  'NOTICE_CATALOG;BPM',  'NOTICE_TYPE;TEST');
INSERT INTO `pep_template`(`ID`, `CREATE_TIME`, `CREATE_USER_ID`, `LAST_MODIFY_TIME`, `LAST_MODIFY_USER_ID`, `ENABLE`, `CODE`, `NAME`, `TITLE`, `TEMPLATE`, `DESCRIPTION`,  `CATALOG`,  `TYPE`) VALUES ('idEntryTaskAssignee', '2018-08-13 00:00:00.000', 'PEP', '2018-08-13 00:00:00.000', 'PEP', 'Y', 'EntryTaskAssignee', 'EntryTaskAssignee', '流程待办', '【{initiatorName}】发起的【{name}】的【{processDefinitionName}】已经到达您的代办，请您处理【<a href={pageurl}>{taskName}</a>】。', '1', 'NOTICE_CATALOG;BPM',  'NOTICE_TYPE;EMAIL');
INSERT INTO `pep_template`(`ID`, `CREATE_TIME`, `CREATE_USER_ID`, `LAST_MODIFY_TIME`, `LAST_MODIFY_USER_ID`, `ENABLE`, `CODE`, `NAME`, `TITLE`, `TEMPLATE`, `DESCRIPTION`,  `CATALOG`,  `TYPE`) VALUES ('idEntryTaskAssignee2', '2018-08-13 00:00:00.000', 'PEP', '2018-08-13 00:00:00.000', 'PEP', 'Y', 'EntryTaskAssignee', 'EntryTaskAssignee', '您有一条新的待办消息', '{name}的{processDefinitionName}', '1', 'NOTICE_CATALOG;BPM',  'NOTICE_TYPE;PUSH');
INSERT INTO `pep_template`(`ID`, `CREATE_TIME`, `CREATE_USER_ID`, `LAST_MODIFY_TIME`, `LAST_MODIFY_USER_ID`, `ENABLE`, `CODE`, `NAME`, `TITLE`, `TEMPLATE`, `DESCRIPTION`,  `CATALOG`,  `TYPE`) VALUES ('idTaskAssignee', '2018-08-08 00:00:00.000', 'PEP', '2018-08-08 00:00:00.000', 'PEP', 'Y', 'TaskAssignee', 'TaskAssignee', '流程待办', '【{initiatorName}】发起的【{processDefinitionName}】已经到达您的代办，请您处理【<a href={pageurl}>{taskName}</a>】。', '1', 'NOTICE_CATALOG;BPM',  'NOTICE_TYPE;EMAIL');
INSERT INTO `pep_template`(`ID`, `CREATE_TIME`, `CREATE_USER_ID`, `LAST_MODIFY_TIME`, `LAST_MODIFY_USER_ID`, `ENABLE`, `CODE`, `NAME`, `TITLE`, `TEMPLATE`, `DESCRIPTION`,  `CATALOG`,  `TYPE`) VALUES ('idTaskAssignee2', '2018-08-08 00:00:00.000', 'PEP', '2018-08-08 00:00:00.000', 'PEP', 'Y', 'TaskAssignee', 'TaskAssignee', '您有一条新的待办消息', '{initiatorName}的{processDefinitionName}', '1', 'NOTICE_CATALOG;BPM',  'NOTICE_TYPE;PUSH');
INSERT INTO `pep_template`(`ID`, `CREATE_TIME`, `CREATE_USER_ID`, `LAST_MODIFY_TIME`, `LAST_MODIFY_USER_ID`, `ENABLE`, `CODE`, `NAME`, `TITLE`, `TEMPLATE`, `DESCRIPTION`,  `CATALOG`,  `TYPE`) VALUES ('idTaskAssigneeEntry', '2018-08-08 00:00:00.000', 'PEP', '2018-08-08 00:00:00.000', 'PEP', 'Y', 'TaskAssigneeEntry', 'TaskAssigneeEntry', '流程待办', '【{initiatorName}】发起的【{processDefinitionName}】已经到达您的代办，请您处理【<a href={pageurl}>{taskName}</a>】。','1', 'NOTICE_CATALOG;BPM',  'NOTICE_TYPE;EMAIL');
INSERT INTO `pep_template`(`ID`, `CREATE_TIME`, `CREATE_USER_ID`, `LAST_MODIFY_TIME`, `LAST_MODIFY_USER_ID`, `ENABLE`, `CODE`, `NAME`, `TITLE`, `TEMPLATE`, `DESCRIPTION`,  `CATALOG`,  `TYPE`) VALUES ('idTaskAssigneeEntry2', '2018-08-08 00:00:00.000', 'PEP', '2018-08-08 00:00:00.000', 'PEP', 'Y', 'TaskAssigneeEntry', 'TaskAssigneeEntry', '您有一条新的待办消息', '{initiatorName}的{processDefinitionName}','1', 'NOTICE_CATALOG;BPM',  'NOTICE_TYPE;PUSH');
INSERT INTO pep_auth_users (id, username, password, superuser, pep_dtype, name, phone, email, enable) VALUES ('test1', 'test1', 'pwd', 'Y', 'UserEntity', 'c', '12345678901', 'test1@test1.com', 'Y');
INSERT INTO pep_auth_users (id, username, password, superuser, pep_dtype, name, phone, email, enable) VALUES ('test2', 'test2', 'pwd', 'Y', 'UserEntity', 'c', '12345678902', 'test1@test2.com', 'Y');
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order) VALUES ('notice_type_push', 'NOTICE_TYPE', 'PUSH', '推送', 0);
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order) VALUES ('notice_type_email', 'NOTICE_TYPE', 'EMAIL', '邮件', 1);
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order) VALUES ('notice_type_sms', 'NOTICE_TYPE', 'SMS', '短信', 2);
