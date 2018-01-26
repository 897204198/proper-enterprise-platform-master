INSERT INTO pep_auth_menus (id, name, route, icon, parent_id, sequence_number, menu_type) VALUES ('pep-workflow', '流程设置', '/workflow', 'dns', null, '0', 'MENU_TYPE;0');
INSERT INTO pep_auth_menus (id, name, route, icon, parent_id, sequence_number, menu_type) VALUES ('pep-workflow-designer', '流程设计器', '/workflow/designer', 'palette', 'pep-workflow', '0', 'MENU_TYPE;1');
INSERT INTO pep_auth_menus (id, name, route, icon, parent_id, sequence_number, menu_type) VALUES ('pep-workflow-instances', '流程实例', '/workflow/instances', 'flight_takeoff', 'pep-workflow', '1', 'MENU_TYPE;1');

COMMIT;
