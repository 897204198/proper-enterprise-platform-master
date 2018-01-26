INSERT INTO pep_auth_menus (id, name, route, icon, parent_id, sequence_number, menu_type) VALUES ('pep-auth', '权限管理', '/platform/auth/security', 'settings_applications_black', null, '1', 'MENU_TYPE;0');
INSERT INTO pep_auth_menus (id, name, route, icon, parent_id, sequence_number, menu_type) VALUES ('pep-auth-users', '用户管理', '/platform/auth/security/users', 'person_black', 'pep-auth', '0', 'MENU_TYPE;1');
INSERT INTO pep_auth_menus (id, name, route, icon, parent_id, sequence_number, menu_type) VALUES ('pep-auth-functions', '功能管理', '/platform/auth/security/functions', 'functions_black', 'pep-auth', '1', 'MENU_TYPE;1');
INSERT INTO pep_auth_menus (id, name, route, icon, parent_id, sequence_number, menu_type) VALUES ('pep-auth-roles', '角色管理', '/platform/auth/security/roles', 'business_black', 'pep-auth', '2', 'MENU_TYPE;1');
INSERT INTO pep_auth_menus (id, name, route, icon, parent_id, sequence_number, menu_type) VALUES ('pep-auth-user-groups', '用户组管理', '/platform/auth/security/user-groups', 'supervisor_account_black', 'pep-auth', '3', 'MENU_TYPE;1');

COMMIT;
