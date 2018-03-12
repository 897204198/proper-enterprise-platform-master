-- 用户-角色关联边
MATCH (a:PEP_AUTH_USERS),(b:PEP_AUTH_ROLES) WHERE a.id = 'pep-sysadmin' AND b.id = 'pep-role-admin' merge (a)-[r:has_role]->(b) RETURN r;
