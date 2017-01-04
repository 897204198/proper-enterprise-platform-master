INSERT INTO pep_auth_menus (id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, route, icon, parent_id, sequence_number) VALUES ('pep-auth', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', '安全设置', '/platform/auth/security', null, null, '0');
INSERT INTO pep_auth_menus (id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, route, icon, parent_id, sequence_number) VALUES ('pep-auth-resources', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', '资源', '/platform/auth/security/resources', null, 'pep-auth', '0');
INSERT INTO pep_auth_menus (id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, route, icon, parent_id, sequence_number) VALUES ('pep-auth-roles', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', '角色', '/platform/auth/security/roles', null, 'pep-auth', '1');
INSERT INTO pep_auth_menus (id, create_user_id, create_time, last_modify_user_id, last_modify_time, name, route, icon, parent_id, sequence_number) VALUES ('pep-auth-users', 'pep', '2015-08-18 09:38:00', 'pep', '2015-08-18 09:38:00', '用户', '/platform/auth/security/users', null, 'pep-auth', '2');

COMMIT;
