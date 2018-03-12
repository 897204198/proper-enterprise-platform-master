-- 角色-菜单关联边
MATCH (a:PEP_AUTH_ROLES),(b:PEP_AUTH_MENUS) WHERE a.id = 'pep-role-admin' AND b.id = 'pep-auth' merge (a)-[r:has_menu]->(b) RETURN r;
