INSERT INTO pep_auth_menus (id, name, route, icon, parent_id, sequence_number, menu_type) VALUES ('pep-devtools', '开发者工具', 'devtools', 'tool',
null, 2,  'MENU_TYPE;0');
INSERT INTO pep_auth_menus (id, name, route, icon, parent_id, sequence_number, menu_type) VALUES ('pep-devtools-appver', 'APP版本管理',
'devtools/appver', 'tag-o', 'pep-devtools', 0, 'MENU_TYPE;1');

COMMIT;
