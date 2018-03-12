-- 资源ID限制
CREATE CONSTRAINT ON (n:PEP_AUTH_RESOURCES) ASSERT n.id IS UNIQUE;
-- 菜单资源
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-menus-r', name: '检索菜单', url: '/auth/menus', method: 'GET', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-menus-c', name: '创建菜单', url: '/auth/menus', method: 'POST', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-menus-u', name: '更新多个菜单状态', url: '/auth/menus', method: 'PUT', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-menus-d', name: '删除多个菜单', url: '/auth/menus', method: 'DELETE', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-menus-menu-g', name: '获得菜单', url: '/auth/menus/*', method: 'GET', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-menus-menu-u', name: '更新菜单', url: '/auth/menus/*', method: 'PUT', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-menus-menu-resource-c', name: '菜单添加资源', url: '/auth/menus/*/resource/*', method: 'POST', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-menus-menu-resource-d', name: '菜单删除资源', url: '/auth/menus/*/resource/*', method: 'DELETE', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-menus-menu-resources-g', name: '取得菜单的资源集合', url: '/auth/menus/*/resources', method: 'GET', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-menus-menu-roles-g', name: '取得菜单的角色集合', url: '/auth/menus/*/roles', method: 'GET', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-menus-parents-g', name: '取得可选父菜单的集合', url: '/auth/menus/parents', method: 'GET', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
-- 资源
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-resources-c', name: '创建资源', url: '/auth/resources', method: 'POST', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-resources-u', name: '更新多个资源状态', url: '/auth/resources', method: 'PUT', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-resources-d', name: '删除多个资源', url: '/auth/resources', method: 'DELETE', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-resources-resource-g', name: '获得资源', url: '/auth/resources/*', method: 'GET', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-resources-resource-u', name: '更新资源', url: '/auth/resources/*', method: 'PUT', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-resources-resource-d', name: '删除资源', url: '/auth/resources/*', method: 'DELETE', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-resources-resource-menus-g', name: '取得资源的菜单集合', url: '/auth/resources/*/menus', method: 'GET', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-resources-resource-roles-g', name: '取得资源的角色集合', url: '/auth/resources/*/roles', method: 'GET', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
-- 角色资源
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-roles-r', name: '检索角色', url: '/auth/roles', method: 'GET', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-roles-c', name: '创建角色', url: '/auth/roles', method: 'POST', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-roles-u', name: '更新多个角色状态', url: '/auth/roles', method: 'PUT', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-roles-d', name: '删除多个角色', url: '/auth/roles', method: 'DELETE', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-roles-role-g', name: '获得角色', url: '/auth/roles/*', method: 'GET', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-roles-role-u', name: '更新角色', url: '/auth/roles/*', method: 'PUT', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-roles-role-menus-g', name: '取得角色的菜单集合', url: '/auth/roles/*/menus', method: 'GET', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-roles-role-menus-c', name: '角色添加菜单', url: '/auth/roles/*/menus', method: 'POST', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-roles-role-menus-d', name: '角色删除菜单', url: '/auth/roles/*/menus', method: 'DELETE', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-roles-role-resources-g', name: '取得角色的资源集合', url: '/auth/roles/*/resources', method: 'GET', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-roles-role-resources-c', name: '角色添加资源', url: '/auth/roles/*/resources', method: 'POST', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-roles-role-resources-d', name: '角色删除资源', url: '/auth/roles/*/resources', method: 'DELETE', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-roles-role-users-g', name: '取得角色的用户集合', url: '/auth/roles/*/users', method: 'GET', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-roles-role-user-groups-g', name: '取得角色的用户组集合', url: '/auth/roles/*/user-groups', method: 'GET', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-roles-parents-g', name: '取得可选父角色的集合', url: '/auth/roles/parents', method: 'GET', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
-- 用户资源
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-users-r', name: '检索用户', url: '/auth/users', method: 'GET', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-users-c', name: '创建用户', url: '/auth/users', method: 'POST', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-users-u', name: '更新多个用户状态', url: '/auth/users', method: 'PUT', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-users-d', name: '删除多个用户', url: '/auth/users', method: 'DELETE', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-users-user-g', name: '获得用户', url: '/auth/users/*', method: 'GET', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-users-user-u', name: '更新用户', url: '/auth/users/*', method: 'PUT', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-users-user-d', name: '删除用户', url: '/auth/users/*', method: 'DELETE', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-users-user-role-c', name: '用户添加角色', url: '/auth/users/*/role/*', method: 'POST', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-users-user-role-d', name: '用户删除角色', url: '/auth/users/*/role/*', method: 'DELETE', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-users-user-roles-g', name: '取得用户的角色集合', url: '/auth/users/*/roles', method: 'GET', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-users-user-groups-g', name: '取得用户的用户组集合', url: '/auth/users/*/user-groups', method: 'GET', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-users-query-g', name: '按用户名或显示名或手机号检索用户集合', url: '/auth/users/query', method: 'GET', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
-- 用户组资源
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-user-groups-r', name: '检索用户组', url: '/auth/user-groups', method: 'GET', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-user-groups-c', name: '创建用户组', url: '/auth/user-groups', method: 'POST', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-user-groups-u', name: '更新多个用户组状态', url: '/auth/user-groups', method: 'PUT', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-user-groups-d', name: '删除多个用户组', url: '/auth/user-groups', method: 'DELETE', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-user-groups-group-g', name: '获得用户组', url: '/auth/user-groups/*', method: 'GET', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-user-groups-group-u', name: '更新用户组', url: '/auth/user-groups/*', method: 'PUT', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-user-groups-group-d', name: '删除用户组', url: '/auth/user-groups/*', method: 'DELETE', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-user-groups-group-role-c', name: '用户组添加角色', url: '/auth/user-groups/*/role/*', method: 'POST', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-user-groups-group-role-d', name: '用户组删除角色', url: '/auth/user-groups/*/role/*', method: 'DELETE', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-user-groups-group-roles-g', name: '取得用户组的角色集合', url: '/auth/user-groups/*/roles', method: 'GET', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-user-groups-group-user-c', name: '用户组添加用户', url: '/auth/user-groups/*/user/*', method: 'POST', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-user-groups-group-user-d', name: '用户组删除用户', url: '/auth/user-groups/*/user/*', method: 'DELETE', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
create (n:PEP_AUTH_RESOURCES { id: 'pep-auth-user-groups-group-users-g', name: '取得用户组的用户集合', url: '/auth/user-groups/*/users', method: 'GET', resourceType: 'RESOURCE_TYPE;0', enable: true, identifier: '', valid: true }) return n;
