-- 菜单ID限制
CREATE CONSTRAINT ON (n:PEP_AUTH_MENUS) ASSERT n.id IS UNIQUE;
-- 菜单
create (n:PEP_AUTH_MENUS { id:'pep-auth', name: '权限管理', route: 'auth', sequenceNumber: 1, icon: 'lock', description: '权限管理', menuType: 'MENU_TYPE;0', enable: true, identifier: '', valid: true}) return n;
create (n:PEP_AUTH_MENUS { id:'pep-auth-users', name: '用户管理', route: 'user', sequenceNumber: 1, icon: 'solution', description: '用户管理', menuType: 'MENU_TYPE;1', enable: true, identifier: '', valid: true}) return n;
create (n:PEP_AUTH_MENUS { id:'pep-auth-functions', name: '功能管理', route: 'func', sequenceNumber: 2, icon: 'bars', description: '功能管理', menuType: 'MENU_TYPE;1', enable: true, identifier: '', valid: true}) return n;
create (n:PEP_AUTH_MENUS { id:'pep-auth-roles', name: '角色管理', route: 'role', sequenceNumber: 3, icon: 'skin', description: '角色管理', menuType: 'MENU_TYPE;1', enable: true, identifier: '', valid: true}) return n;
create (n:PEP_AUTH_MENUS { id:'pep-auth-user-groups', name: '用户组管理', route: 'group', sequenceNumber: 4, icon: 'team', description: '用户组管理', menuType: 'MENU_TYPE;1', enable: true, identifier: '', valid: true}) return n;
