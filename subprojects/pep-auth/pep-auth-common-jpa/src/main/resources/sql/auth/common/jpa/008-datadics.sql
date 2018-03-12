-- 菜单类型
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order, is_default) VALUES ('pep_auth_menu_type_app', 'MENU_TYPE', '0', '应用', 0, 'Y');
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order) VALUES ('pep_auth_menu_type_page', 'MENU_TYPE', '1', '页面', 1);
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order) VALUES ('pep_auth_menu_type_function', 'MENU_TYPE', '2', '功能', 2);

-- 资源类型
INSERT INTO pep_sys_data_dic (id, dd_catalog, dd_code, dd_name, dd_order, is_default) VALUES ('pep_auth_resource_type_method', 'RESOURCE_TYPE', '0', '方法', 0, 'Y');

COMMIT;
