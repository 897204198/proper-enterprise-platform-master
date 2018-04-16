-- 菜单资源
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-menus-r', '检索菜单', '/auth/menus', 'GET',
'RESOURCE_TYPE;0', 'find');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-menus-c', '创建菜单', '/auth/menus', 'POST',
'RESOURCE_TYPE;0', 'creat');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-menus-u', '更新多个菜单状态', '/auth/menus', 'PUT',
'RESOURCE_TYPE;0', 'edit');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-menus-d', '删除多个菜单', '/auth/menus', 'DELETE',
'RESOURCE_TYPE;0', 'delete');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-menus-menu-g', '获得菜单', '/auth/menus/*', 'GET',
'RESOURCE_TYPE;0', 'find');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-menus-menu-u', '更新菜单', '/auth/menus/*', 'PUT',
'RESOURCE_TYPE;0', 'update');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-menus-menu-resource-c', '菜单添加资源',
'/auth/menus/*/resource/*', 'POST', 'RESOURCE_TYPE;0', 'add');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-menus-menu-resource-d', '菜单删除资源',
'/auth/menus/*/resource/*', 'DELETE', 'RESOURCE_TYPE;0', 'delete');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-menus-menu-resources-g', '取得菜单的资源集合',
'/auth/menus/*/resources', 'GET', 'RESOURCE_TYPE;0', 'find');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-menus-menu-roles-g', '取得菜单的角色集合',
'/auth/menus/*/roles', 'GET', 'RESOURCE_TYPE;0', 'find');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-menus-parents-g', '取得可选父菜单的集合',
'/auth/menus/parents', 'GET', 'RESOURCE_TYPE;0', 'find');
-- 资源
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-resources-c', '创建资源', '/auth/resources',
'POST', 'RESOURCE_TYPE;0', 'creat');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-resources-u', '更新多个资源状态', '/auth/resources',
'PUT', 'RESOURCE_TYPE;0', 'update');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-resources-d', '删除多个资源', '/auth/resources',
'DELETE', 'RESOURCE_TYPE;0', 'delete');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-resources-resource-g', '获得资源',
'/auth/resources/*', 'GET', 'RESOURCE_TYPE;0', 'find');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-resources-resource-u', '更新资源',
'/auth/resources/*', 'PUT', 'RESOURCE_TYPE;0','update');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-resources-resource-d', '删除资源',
'/auth/resources/*', 'DELETE', 'RESOURCE_TYPE;0', 'delete');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-resources-resource-menus-g', '取得资源的菜单集合',
'/auth/resources/*/menus', 'GET', 'RESOURCE_TYPE;0', 'find');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-resources-resource-roles-g', '取得资源的角色集合',
'/auth/resources/*/roles', 'GET', 'RESOURCE_TYPE;0', 'find');
-- 角色资源
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-roles-r', '检索角色', '/auth/roles', 'GET',
'RESOURCE_TYPE;0', 'find');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-roles-c', '创建角色', '/auth/roles', 'POST',
'RESOURCE_TYPE;0', 'creat');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-roles-u', '更新多个角色状态', '/auth/roles', 'PUT',
'RESOURCE_TYPE;0', 'update');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-roles-d', '删除多个角色', '/auth/roles', 'DELETE',
'RESOURCE_TYPE;0', 'delete');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-roles-role-g', '获得角色', '/auth/roles/*', 'GET',
'RESOURCE_TYPE;0', 'find');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-roles-role-u', '更新角色', '/auth/roles/*', 'PUT',
'RESOURCE_TYPE;0', 'update');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-roles-role-menus-g', '取得角色的菜单集合',
'/auth/roles/*/menus', 'GET', 'RESOURCE_TYPE;0', 'find');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-roles-role-menus-c', '角色添加菜单',
'/auth/roles/*/menus', 'POST', 'RESOURCE_TYPE;0', 'add');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-roles-role-menus-d', '角色删除菜单',
'/auth/roles/*/menus', 'DELETE', 'RESOURCE_TYPE;0', 'delete');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-roles-role-resources-g', '取得角色的资源集合',
'/auth/roles/*/resources', 'GET', 'RESOURCE_TYPE;0', 'find');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-roles-role-resources-c', '角色添加资源',
'/auth/roles/*/resources', 'POST', 'RESOURCE_TYPE;0', 'add');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-roles-role-resources-d', '角色删除资源',
'/auth/roles/*/resources', 'DELETE', 'RESOURCE_TYPE;0', 'delete');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-roles-role-users-g', '取得角色的用户集合',
'/auth/roles/*/users', 'GET', 'RESOURCE_TYPE;0', 'find');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-roles-role-user-groups-g', '取得角色的用户组集合',
'/auth/roles/*/user-groups', 'GET', 'RESOURCE_TYPE;0', 'find');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-roles-parents-g', '取得可选父角色的集合',
'/auth/roles/parents', 'GET', 'RESOURCE_TYPE;0', 'find');
-- 用户资源
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-users-r', '检索用户', '/auth/users', 'GET',
'RESOURCE_TYPE;0', 'get');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-users-c', '创建用户', '/auth/users', 'POST',
'RESOURCE_TYPE;0', 'add');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-users-u', '更新多个用户状态', '/auth/users', 'PUT', 
'RESOURCE_TYPE;0', 'edit');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-users-d', '删除多个用户', '/auth/users', 'DELETE', 
'RESOURCE_TYPE;0', 'delete');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-users-user-g', '获得用户', '/auth/users/*', 'GET', 
'RESOURCE_TYPE;0', 'find');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-users-user-u', '更新用户', '/auth/users/*', 'PUT', 
'RESOURCE_TYPE;0', 'update');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-users-user-d', '删除用户', '/auth/users/*', 'DELETE', 
'RESOURCE_TYPE;0', 'delete');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-users-user-role-c', '用户添加角色', '/auth/users/*/role/*', 
'POST', 'RESOURCE_TYPE;0', 'add');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-users-user-role-d', '用户删除角色', '/auth/users/*/role/*', 
'DELETE', 'RESOURCE_TYPE;0', 'delete');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-users-user-roles-g', '取得用户的角色集合', '/auth/users/*/roles', 
'GET', 'RESOURCE_TYPE;0', 'find');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-users-user-groups-g', '取得用户的用户组集合', 
'/auth/users/*/user-groups', 'GET', 'RESOURCE_TYPE;0', 'find');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-users-query-g', '按用户名或显示名或手机号检索用户集合', '/auth/users/query',
 'GET', 'RESOURCE_TYPE;0', 'find');
-- 用户组资源
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-user-groups-r', '检索用户组', '/auth/user-groups', 'GET',
'RESOURCE_TYPE;0', 'find', 'get');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-user-groups-c', '创建用户组', '/auth/user-groups', 'POST', 
'RESOURCE_TYPE;0', 'add');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-user-groups-u', '更新多个用户组状态', 
'/auth/user-groups', 'PUT', 
'RESOURCE_TYPE;0', 'update');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-user-groups-d', '删除多个用户组', '/auth/user-groups',
 'DELETE', 
'RESOURCE_TYPE;0', 'delete');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-user-groups-group-g', '获得用户组', '/auth/user-groups/*', 
'GET', 'RESOURCE_TYPE;0','get');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-user-groups-group-u', '更新用户组', '/auth/user-groups/*', 
'PUT', 'RESOURCE_TYPE;0', 'update');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-user-groups-group-d', '删除用户组', '/auth/user-groups/*', 
'DELETE', 'RESOURCE_TYPE;0', 'delete');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-user-groups-group-role-c', '用户组添加角色', 
'/auth/user-groups/*/role/*', 'POST', 'RESOURCE_TYPE;0', 'add');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-user-groups-group-role-d', '用户组删除角色', 
'/auth/user-groups/*/role/*', 'DELETE', 'RESOURCE_TYPE;0', 'delete');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-user-groups-group-roles-g', '取得用户组的角色集合', 
'/auth/user-groups/*/roles', 'GET', 'RESOURCE_TYPE;0', 'find');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-user-groups-group-user-c', '用户组添加用户', 
'/auth/user-groups/*/user/*', 'POST', 'RESOURCE_TYPE;0', 'add');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-user-groups-group-user-d', '用户组删除用户', 
'/auth/user-groups/*/user/*', 'DELETE', 'RESOURCE_TYPE;0', 'delete');
INSERT INTO pep_auth_resources (id, name, url, method, resource_type, identifier) VALUES ('pep-auth-user-groups-group-users-g', '取得用户组的用户集合', 
'/auth/user-groups/*/users', 'GET', 'RESOURCE_TYPE;0', 'get');

COMMIT;
