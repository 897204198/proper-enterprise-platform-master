-- 数据字典model部署状态
INSERT INTO PEP_SYS_DATA_DIC (ID, DD_CATALOG, DD_CODE, DD_NAME, DD_ORDER, IS_DEFAULT) VALUES ('pep_workflow_model_status_deployed', 'ModelStatus', 'DEPLOYED', '已部署', 0, 'N');
INSERT INTO PEP_SYS_DATA_DIC (ID, DD_CATALOG, DD_CODE, DD_NAME, DD_ORDER, IS_DEFAULT) VALUES ('pep_workflow_model_status_undeployed', 'ModelStatus', 'UN_DEPLOYED', '未部署', 1, 'Y');


-- 数据字典流程状态
INSERT INTO PEP_SYS_DATA_DIC (ID, DD_CATALOG, DD_CODE, DD_NAME, DD_ORDER, IS_DEFAULT) VALUES ('pep_workflow_status_doing', 'PEPProcInstStateEnum', 'DOING', '处理中', 0, 'Y');
INSERT INTO PEP_SYS_DATA_DIC (ID, DD_CATALOG, DD_CODE, DD_NAME, DD_ORDER, IS_DEFAULT) VALUES ('pep_workflow_status_done', 'PEPProcInstStateEnum', 'DONE', '已完成', 1, 'N');


--WEB_ADDRESS
INSERT INTO PEP_SYS_DATA_DIC (ID, DD_CATALOG, DD_CODE, DD_NAME, DD_ORDER, IS_DEFAULT) VALUES ('basePath', 'AppConfigEnum', 'WEB_ADDRESS', 'http://test', 1, 'N');

INSERT INTO `pep_template`(`ID`, `CREATE_TIME`, `CREATE_USER_ID`, `ENABLE`, `LAST_MODIFY_TIME`, `LAST_MODIFY_USER_ID`, `CODE`, `NAME`, `CATALOG`, `DESCRIPTION`, `MUTI`, `DETAILS`)
VALUES ('b926ab6c-8916-4d03-afab-3b23e20b1f4c', '2018-10-11 17:25:09.304', 'pep-sysadmin', 'Y', '2018-10-11 17:29:48.204', 'pep-sysadmin', 'testSendMailCode', '测试邮件发送模板', 'BPM', '', 'Y', '[{"title":"{initiatorName}的测试邮件","template":"【{initiatorName}】于【{leaveDate_text}】至【{finishDate_text}】请假【{vacationHours}】小时，请假类型为【{vacationType_text}】，请悉知。(测试邮件，请勿回复)","type":"email"}]');
