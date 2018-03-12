-- 角色ID限制
CREATE CONSTRAINT ON (n:PEP_AUTH_ROLES) ASSERT n.id IS UNIQUE;
-- 角色
create (n:PEP_AUTH_ROLES { id:'pep-role-admin', name: 'PEP超级管理员角色', description: 'PEP超级管理员角色', enable: true, valid: true}) return n;
