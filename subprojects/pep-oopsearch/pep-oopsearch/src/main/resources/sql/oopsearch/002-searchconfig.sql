INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('001', 'authusers', 'pep_auth_users', 'username', 'string', '用户名', 'username', '/auth/users');
INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('002', 'authusers', 'pep_auth_users', 'name', 'string', '用户显示名称', 'name', '/auth/users');
INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('003', 'authusers', 'pep_auth_users', 'email', 'string', '电子邮箱', 'email', '/auth/users');
INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('004', 'authusers', 'pep_auth_users', 'phone', 'string', '手机号码', 'phone', '/auth/users');
INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('005', 'authusers', 'pep_auth_users', 'enable', 'string', '状态', 'userEnable', '/auth/users');

INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('006', 'authmenus', 'pep_auth_menus', 'name', 'string', '菜单名称', 'name', '/auth/menus');
INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('007', 'authmenus', 'pep_auth_menus', 'description', 'string', '菜单名称', 'description', '/auth/menus');
INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('008', 'authmenus', 'pep_auth_menus', 'route', 'string', '标识', 'route', '/auth/menus');
INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('019', 'authmenus', 'pep_auth_menus', 'enable', 'string', '状态', 'menuEnable', '/auth/menus');
INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('010', 'authmenus', 'pep_auth_menus', 'parent_id', 'string', '父节点id', 'parentId', '/auth/menus');

INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('011', 'authroles', 'pep_auth_roles', 'name', 'string', '名称', 'name', '/auth/roles');
INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('012', 'authroles', 'pep_auth_roles', 'description', 'string', '功能描述说明', 'description', '/auth/roles');
INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('013', 'authroles', 'pep_auth_roles', 'parent_id', 'string', '父节点id', 'parentId', '/auth/roles');
INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('014', 'authroles', 'pep_auth_roles', 'enable', 'string', '状态', 'roleEnable', '/auth/roles');

INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('015', 'authusergroups', 'pep_auth_usergroups', 'name', 'string', '用户组名称', 'name', '/auth/user-groups');
INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('016', 'authusergroups', 'pep_auth_usergroups', 'description', 'string', '描述说明', 'description', '/auth/user-groups');
INSERT INTO pep_oopsearch_config (id, module_name, table_name, search_column, column_type, column_desc, column_alias, url) VALUES ('017', 'authusergroups', 'pep_auth_usergroups', 'enable', 'string', '状态', 'userGroupEnable', ' /auth/user-groups');


COMMIT;
