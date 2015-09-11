package com.proper.enterprise.platform.integration.auth

import org.springframework.stereotype.Component

@Component
class InsertDataWorker {

    def sqls = []
    def user1name = 'hinex1', user2name = 'hinex2'
    def userpwd = 'abc'
    def roleAcode = 'roleA', roleBcode = 'roleB', roleCcode = 'roleC'

    public def getBeforeDMLs() {
        createUser()
        createRoles()
        grantUserRoles()
        createResources()
        grantRoleResources()
        sqls
    }

    public def getAfterDMLs() {
        [
            'DELETE FROM pep_auth_role_resources;',
            'DELETE FROM pep_auth_resource;',
            'DELETE FROM pep_auth_user_roles;',
            'DELETE FROM pep_auth_role;',
            'DELETE FROM pep_auth_user;'
        ]
    }

    private void createUser() {
        [user1name, user2name].each { username ->
            sqls << """
INSERT INTO pep_auth_user
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, login_name, password)
VALUES
(${username + '-' + userpwd}, 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', $username, $userpwd);"""
        }
    }

    private void createRoles() {
        [roleAcode, roleBcode, roleCcode].each { role ->
            sqls << """
INSERT INTO pep_auth_role
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name)
VALUES
(${role + '-' + role}, 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', $role, $role);"""
        }
    }

    private void grantUserRoles() {
        [["$user1name", roleAcode], ["$user1name", roleBcode],
         ["$user2name", roleAcode], ["$user2name", roleBcode], ["$user2name", roleCcode]].each {
            def username = it[0]
            def role = it[1]
            def users = "$username-$userpwd".toString()
            def roles = "$role-$role".toString()
            sqls << """
INSERT INTO pep_auth_user_roles
(users, roles)
VALUES
($users, $roles);"""
        }
    }

    private void createResources() {
        (1..10).each { idx ->
            sqls << """
INSERT INTO pep_auth_resource
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, url, method, sequence_number)
VALUES
(${'res' + idx}, 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', ${'/auth/res' + idx}, 'GET', $idx);"""
        }
    }

    private void grantRoleResources() {
        ["$roleAcode": (1..6), "$roleBcode": (5..10), "$roleCcode": (1..10)].each { role, range ->
            range.each { idx ->
                sqls << """
INSERT INTO pep_auth_role_resources
(roles, resources)
VALUES
(${(role + '-' + role).toString()}, ${'res' + idx});"""
            }
        }
    }

}
