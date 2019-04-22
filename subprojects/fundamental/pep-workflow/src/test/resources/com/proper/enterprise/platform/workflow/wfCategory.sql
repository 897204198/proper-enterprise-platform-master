INSERT INTO PEP_WF_CATEGORY (id, name, code, sort) VALUES ('parentCategory', '父类别', 'PARENT_CATEGORY', 0);
INSERT INTO PEP_WF_CATEGORY (id, name, code, sort) VALUES ('defaultCategory', '默认类别', 'DEFAULT_CATEGORY', 0);
INSERT INTO PEP_WF_CATEGORY (id, name, code, sort, parent_id) VALUES ('defaultFlowableCategory', '默认Flowable类别', 'http://www.flowable.org/processdef', 0, 'defaultCategory');
INSERT INTO PEP_WF_CATEGORY (id, name, code, sort, parent_id) VALUES ('defaultActivitiCategory', '默认Activiti类别', 'http://www.activiti.org/processdef', 0, 'defaultCategory');

