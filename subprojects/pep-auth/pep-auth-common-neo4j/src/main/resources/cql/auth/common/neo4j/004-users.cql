-- 用户ID限制
CREATE CONSTRAINT ON (n:PEP_AUTH_USERS) ASSERT n.id IS UNIQUE;
-- 用户
create (n:PEP_AUTH_USERS { id:'pep-sysadmin', username: 'admin', password: 'e10adc3949ba59abbe56e057f20f883e', email: '', superuser: true, phone: '', name: '超级管理员', pepDtype: 'UserNodeEntity', enable: true, valid: true}) return n;
