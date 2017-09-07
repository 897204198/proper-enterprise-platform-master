INSERT INTO pep_auth_menus (id, name, route, icon, parent_id, sequence_number) VALUES ('pep-pc', '功能自定义', '/custom', 'settings', null, '0');
INSERT INTO pep_auth_menus (id, name, route, icon, parent_id, sequence_number) VALUES ('pep-pc-setting', '列表自定义', '/custom/gridConfig', 'panorama_fish_eye', 'pep-pc', '0');

COMMIT;
