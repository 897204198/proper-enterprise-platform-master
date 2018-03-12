-- 菜单-资源关联边
-- 用户
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-users' AND b.id = 'pep-auth-users-r' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-users' AND b.id = 'pep-auth-users-c' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-users' AND b.id = 'pep-auth-users-u' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-users' AND b.id = 'pep-auth-users-d' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-users' AND b.id = 'pep-auth-users-user-g' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-users' AND b.id = 'pep-auth-users-user-u' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-users' AND b.id = 'pep-auth-users-user-d' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-users' AND b.id = 'pep-auth-users-user-role-c' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-users' AND b.id = 'pep-auth-users-user-role-d' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-users' AND b.id = 'pep-auth-users-user-roles-g' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-users' AND b.id = 'pep-auth-users-user-groups-g' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-users' AND b.id = 'pep-auth-users-query-g' merge (a)-[r:has_resource]->(b) RETURN r;
-- 功能_菜单
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-functions' AND b.id = 'pep-auth-menus-r' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-functions' AND b.id = 'pep-auth-menus-c' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-functions' AND b.id = 'pep-auth-menus-u' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-functions' AND b.id = 'pep-auth-menus-d' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-functions' AND b.id = 'pep-auth-menus-menu-g' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-functions' AND b.id = 'pep-auth-menus-menu-u' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-functions' AND b.id = 'pep-auth-menus-menu-resource-c' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-functions' AND b.id = 'pep-auth-menus-menu-resource-d' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-functions' AND b.id = 'pep-auth-menus-menu-resources-g' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-functions' AND b.id = 'pep-auth-menus-menu-roles-g' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-functions' AND b.id = 'pep-auth-menus-parents-g' merge (a)-[r:has_resource]->(b) RETURN r;
-- 功能_资源
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-functions' AND b.id = 'pep-auth-resources-c' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-functions' AND b.id = 'pep-auth-resources-u' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-functions' AND b.id = 'pep-auth-resources-d' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-functions' AND b.id = 'pep-auth-resources-resource-g' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-functions' AND b.id = 'pep-auth-resources-resource-u' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-functions' AND b.id = 'pep-auth-resources-resource-d' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-functions' AND b.id = 'pep-auth-resources-resource-menus-g' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-functions' AND b.id = 'pep-auth-resources-resource-roles-g' merge (a)-[r:has_resource]->(b) RETURN r;
-- 角色
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-roles' AND b.id = 'pep-auth-roles-r' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-roles' AND b.id = 'pep-auth-roles-c' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-roles' AND b.id = 'pep-auth-roles-u' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-roles' AND b.id = 'pep-auth-roles-d' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-roles' AND b.id = 'pep-auth-roles-role-g' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-roles' AND b.id = 'pep-auth-roles-role-u' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-roles' AND b.id = 'pep-auth-roles-role-menus-g' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-roles' AND b.id = 'pep-auth-roles-role-menus-c' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-roles' AND b.id = 'pep-auth-roles-role-menus-d' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-roles' AND b.id = 'pep-auth-roles-role-resources-g' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-roles' AND b.id = 'pep-auth-roles-role-resources-c' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-roles' AND b.id = 'pep-auth-roles-role-resources-d' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-roles' AND b.id = 'pep-auth-roles-role-users-g' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-roles' AND b.id = 'pep-auth-roles-role-user-groups-g' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-roles' AND b.id = 'pep-auth-roles-parents-g' merge (a)-[r:has_resource]->(b) RETURN r;
-- 用户组
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-user-groups' AND b.id = 'pep-auth-user-groups-r' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-user-groups' AND b.id = 'pep-auth-user-groups-c' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-user-groups' AND b.id = 'pep-auth-user-groups-u' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-user-groups' AND b.id = 'pep-auth-user-groups-d' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-user-groups' AND b.id = 'pep-auth-user-groups-group-g' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-user-groups' AND b.id = 'pep-auth-user-groups-group-u' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-user-groups' AND b.id = 'pep-auth-user-groups-group-d' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-user-groups' AND b.id = 'pep-auth-user-groups-group-role-c' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-user-groups' AND b.id = 'pep-auth-user-groups-group-role-d' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-user-groups' AND b.id = 'pep-auth-user-groups-group-roles-g' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-user-groups' AND b.id = 'pep-auth-user-groups-group-user-c' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-user-groups' AND b.id = 'pep-auth-user-groups-group-user-d' merge (a)-[r:has_resource]->(b) RETURN r;
MATCH (a:PEP_AUTH_MENUS),(b:PEP_AUTH_RESOURCES) WHERE a.id = 'pep-auth-user-groups' AND b.id = 'pep-auth-user-groups-group-user-g' merge (a)-[r:has_resource]->(b) RETURN r;
