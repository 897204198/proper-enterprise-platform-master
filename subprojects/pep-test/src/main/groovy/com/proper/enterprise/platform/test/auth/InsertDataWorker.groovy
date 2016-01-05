package com.proper.enterprise.platform.test.auth

import org.springframework.stereotype.Component

@Component
class InsertDataWorker {

    def sqls = []
    def user1name = 'hinex1', user2name = 'hinex2'
    def userpwd = 'abc'
    def roleAcode = 'roleA', roleBcode = 'roleB', roleCcode = 'roleC'

    public def getBeforeDMLs() {
        sqls << getAfterDMLs()
        createUser()
        createRoles()
        grantUserRoles()
        createResources()
        grantRoleResources()
        sqls
    }

    public def getAfterDMLs() {
        [
            "DELETE FROM pep_auth_roles_resources WHERE roles LIKE 'IDW-%';",
            "DELETE FROM pep_auth_resources WHERE id LIKE 'IDW-%';",
            "DELETE FROM pep_auth_users_roles WHERE users LIKE 'IDW-%';",
            "DELETE FROM pep_auth_roles WHERE id LIKE 'IDW-%';",
            "DELETE FROM pep_auth_users WHERE id LIKE 'IDW-%';"
        ]
    }

    private void createUser() {
        [user1name, user2name].each { username ->
            sqls << """
INSERT INTO pep_auth_users
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, login_name, password)
VALUES
(${'IDW-' + username}, 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', $username, $userpwd);"""
        }
    }

    private void createRoles() {
        [roleAcode, roleBcode, roleCcode].each { role ->
            sqls << """
INSERT INTO pep_auth_roles
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name)
VALUES
(${'IDW-' + role}, 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', $role, $role);"""
        }
    }

    private void grantUserRoles() {
        [["$user1name", roleAcode], ["$user1name", roleBcode],
         ["$user2name", roleAcode], ["$user2name", roleBcode], ["$user2name", roleCcode]].each {
            def username = it[0]
            def role = it[1]
            def users = "IDW-$username".toString()
            def roles = "IDW-$role".toString()
            sqls << """
INSERT INTO pep_auth_users_roles
(users, roles)
VALUES
($users, $roles);"""
        }
    }

    private void createResources() {
        (1..10).each { idx ->
            sqls << """
INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, url, method, sequence_number)
VALUES
(${'IDW-res' + idx}, 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', ${'/auth/res' + idx}, 'GET', $idx);"""
        }
    }

    private void grantRoleResources() {
        ["$roleAcode": (1..6), "$roleBcode": (5..10), "$roleCcode": (1..10)].each { role, range ->
            range.each { idx ->
                sqls << """
INSERT INTO pep_auth_roles_resources
(roles, resources)
VALUES
(${('IDW-' + role).toString()}, ${'IDW-res' + idx});"""
            }
        }
    }

}
