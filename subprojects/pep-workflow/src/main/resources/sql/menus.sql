INSERT INTO pep_auth_menus (id, name, route, icon, parent_id, sequence_number) VALUES ('pep-workflow', '流程设置', '/workflow', null, null, '0');
INSERT INTO pep_auth_menus (id, name, route, icon, parent_id, sequence_number) VALUES ('pep-workflow-designer', '流程设计器', '/workflow/designer', null, 'pep-workflow', '0');
INSERT INTO pep_auth_menus (id, name, route, icon, parent_id, sequence_number) VALUES ('pep-workflow-instances', '流程实例', '/workflow/instances', null, 'pep-workflow', '1');

COMMIT;
