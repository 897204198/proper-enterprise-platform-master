-- 数据字典model部署状态
INSERT INTO PEP_SYS_DATA_DIC (ID, DD_CATALOG, DD_CODE, DD_NAME, DD_ORDER, IS_DEFAULT) VALUES ('pep_workflow_model_status_deployed', 'ModelStatus', 'DEPLOYED', '已部署', 0, 'N');
INSERT INTO PEP_SYS_DATA_DIC (ID, DD_CATALOG, DD_CODE, DD_NAME, DD_ORDER, IS_DEFAULT) VALUES ('pep_workflow_model_status_undeployed', 'ModelStatus', 'UN_DEPLOYED', '未部署', 1, 'Y');


-- 数据字典流程状态
INSERT INTO PEP_SYS_DATA_DIC (ID, DD_CATALOG, DD_CODE, DD_NAME, DD_ORDER, IS_DEFAULT) VALUES ('pep_workflow_status_doing', 'PEPProcInstStateEnum', 'DOING', '处理中', 0, 'Y');
INSERT INTO PEP_SYS_DATA_DIC (ID, DD_CATALOG, DD_CODE, DD_NAME, DD_ORDER, IS_DEFAULT) VALUES ('pep_workflow_status_done', 'PEPProcInstStateEnum', 'DONE', '已完成', 1, 'N');


--WEB_ADDRESS
INSERT INTO PEP_SYS_DATA_DIC (ID, DD_CATALOG, DD_CODE, DD_NAME, DD_ORDER, IS_DEFAULT) VALUES ('basePath', 'AppConfigEnum', 'WEB_ADDRESS', 'http://test', 1, 'N');

