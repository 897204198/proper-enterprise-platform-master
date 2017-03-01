INSERT INTO pep_auth_menus (id, name, route, icon, parent_id, sequence_number) VALUES ('pep-auth', '安全设置', '/platform/auth/security', null, null, '0');
INSERT INTO pep_auth_menus (id, name, route, icon, parent_id, sequence_number) VALUES ('pep-auth-resources', '资源', '/platform/auth/security/resources', null, 'pep-auth', '0');
INSERT INTO pep_auth_menus (id, name, route, icon, parent_id, sequence_number) VALUES ('pep-auth-roles', '角色', '/platform/auth/security/roles', null, 'pep-auth', '1');
INSERT INTO pep_auth_menus (id, name, route, icon, parent_id, sequence_number) VALUES ('pep-auth-users', '用户', '/platform/auth/security/users', null, 'pep-auth', '2');

COMMIT;
