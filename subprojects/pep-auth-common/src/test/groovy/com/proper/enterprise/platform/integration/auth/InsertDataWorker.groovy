package com.proper.enterprise.platform.integration.auth

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Component
class InsertDataWorker {

    Logger logger = LoggerFactory.getLogger(InsertDataWorker.class)

    @PersistenceContext
    EntityManager em

    def sqls = new ArrayList<String>()
    def user1name = 'hinex1', user2name = 'hinex2'
    def userpwd = 'e10adc3949ba59abbe56e057f20f883e'
    def roleAcode = 'roleA', roleBcode = 'roleB', roleCcode = 'roleC'

    public void insertData() {
        createUser()
        createRoles()
        grantUserRoles()
        createResources()
        grantRoleResources()

        try {
            sqls.each { sql ->
                logger.trace(sql)
                em.createNativeQuery(sql).executeUpdate()
            }
        } catch (ex) { }
    }

    private void createUser() {
        [user1name, user2name].each { username ->
            sqls << """
INSERT INTO pep_auth_user
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, login_name, password)
VALUES
('$username-$userpwd', 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', '${username}', '${userpwd}');"""
        }
    }

    private void createRoles() {
        [roleAcode, roleBcode, roleCcode].each { role ->
            sqls << """
INSERT INTO pep_auth_role
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name)
VALUES
('$role-$role', 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', '$role', '$role');"""
        }
    }

    private void grantUserRoles() {
        [["$user1name", roleAcode], ["$user1name", roleBcode],
         ["$user2name", roleAcode], ["$user2name", roleBcode], ["$user2name", roleCcode]].each {
            def username = it[0]
            def role = it[1]
            sqls << """
INSERT INTO pep_auth_user_roles
(users, roles)
VALUES
('$username-$userpwd', '$role-$role');"""
        }
    }

    private void createResources() {
        (1..10).each { idx ->
            sqls << """
INSERT INTO pep_auth_resource
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, url, sequence_number)
VALUES
('res$idx', 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', '/auth/res$idx', $idx);"""
        }
    }

    private void grantRoleResources() {
        ["$roleAcode": (1..6), "$roleBcode": (5..10), "$roleCcode": (1..10)].each { role, range ->
            range.each { idx ->
                sqls << """
INSERT INTO pep_auth_role_resources
(roles, resources)
VALUES
('$role-$role', 'res$idx');"""
            }
        }
    }

}
