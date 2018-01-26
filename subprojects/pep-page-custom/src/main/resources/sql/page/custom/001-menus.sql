INSERT INTO pep_auth_menus (id, name, route, icon, parent_id, sequence_number, menu_type) VALUES ('pep-pc', '功能自定义', '/custom', 'settings', null, '2', 'MENU_TYPE;0');
INSERT INTO pep_auth_menus (id, name, route, icon, parent_id, sequence_number, menu_type) VALUES ('pep-pc-setting', '列表自定义', '/custom/gridConfig', 'panorama_fish_eye', 'pep-pc', '0', 'MENU_TYPE;1');

COMMIT;
