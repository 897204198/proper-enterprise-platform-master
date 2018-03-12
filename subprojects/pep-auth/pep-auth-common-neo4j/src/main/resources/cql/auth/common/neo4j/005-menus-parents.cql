-- 菜单继承关联边
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_MENUS) WHERE a.id = 'pep-auth' AND b.id = 'pep-auth-users' merge (a)-[r:parent_to]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_MENUS) WHERE a.id = 'pep-auth' AND b.id = 'pep-auth-functions' merge (a)-[r:parent_to]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_MENUS) WHERE a.id = 'pep-auth' AND b.id = 'pep-auth-roles' merge (a)-[r:parent_to]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_MENUS) WHERE a.id = 'pep-auth' AND b.id = 'pep-auth-user-groups' merge (a)-[r:parent_to]->(b) RETURN r;
