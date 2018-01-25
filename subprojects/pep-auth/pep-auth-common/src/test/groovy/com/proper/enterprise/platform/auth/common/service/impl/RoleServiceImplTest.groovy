package com.proper.enterprise.platform.auth.common.service.impl

import com.proper.enterprise.platform.api.auth.service.RoleService
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.common.entity.RoleEntity
import com.proper.enterprise.platform.auth.common.entity.UserEntity
import com.proper.enterprise.platform.auth.common.repository.RoleRepository
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.sys.i18n.I18NService
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class RoleServiceImplTest extends AbstractTest {


    @Autowired
    RoleRepository roleRepository;

    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService

    @Autowired
    I18NService i18NService

    void initRoleData(boolean hasCircleInherit) {
        for (int i = 1; i < 11; i++) {
            RoleEntity roleEntity = new RoleEntity()
            roleEntity.setName('role' + i)
            roleRepository.save(roleEntity)
        }
        List<RoleEntity> list = roleRepository.findAll()
        Map<String, RoleEntity> map = new TreeMap<>()
        for (int i = 0; i < list.size(); i++) {
            RoleEntity roleEntity = list.get(i)
            map.put(roleEntity.getName(), roleEntity)
        }
        List<RoleEntity> list1 = new ArrayList<>(list.size())
        for (int i = 1; i < 11; i++) {
            RoleEntity roleEntity = new RoleEntity()
            roleEntity = map.get('role' + i)
            if (i == 1) {
                if (hasCircleInherit) {
                    roleEntity.setParent(map.get('role10'))
                }
            } else if (i == 2 || i == 3 || i == 4) {
                roleEntity.setParent(map.get('role1'))
            } else if (i == 5) {
                roleEntity.setParent(map.get('role2'))
            } else if (i == 6) {
                roleEntity.setParent(map.get('role3'))
            } else if (i == 7) {
                roleEntity.setParent(map.get('role4'))
            } else if (i == 8) {
                roleEntity.setParent(map.get('role4'))
            } else if (i == 9) {
                roleEntity.setParent(map.get('role5'))
            } else if (i == 10) {
                roleEntity.setParent(map.get('role9'))
            }
            list1.add(roleEntity)
        }
        roleRepository.save(list1)

    }

    @Test
    void testEnable() {

        RoleEntity roleEntity = new RoleEntity()
        roleEntity.setName('role1')
        roleEntity = roleRepository.save(roleEntity)

        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setName("role2")
        roleEntity1.setParent(roleEntity)
        roleRepository.save(roleEntity1)

        RoleEntity roleEntity2 = new RoleEntity()
        roleEntity2.setName('role3')
        roleEntity2.setParent(roleEntity)
        roleRepository.save(roleEntity2)

        Map<String, Object> map = new HashMap<>()
        map.put("id", roleEntity.getId())
        map.put("enable", false)

        UserEntity userEntity = new UserEntity('u', 'p')
        userEntity.setSuperuser(true)
        userEntity = userService.save(userEntity)

        mockUser(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword())

        assert post('/auth/roles', JSONUtil.toJSON(map), HttpStatus.BAD_REQUEST).getResponse()
            .getContentAsString() == i18NService.getMessage("pep.auth.common.role.delete.relation.failed")

        roleService.delete(roleEntity1)
    }



    @Test
    void testHasCircleInherit() {
        initRoleData(true)
        assert roleService.hasCircleInherit(roleRepository.findAll())
        roleRepository.deleteAll()

        initRoleData(false)
        assert !roleService.hasCircleInherit(roleRepository.findAll())
        roleRepository.deleteAll()

    }

}
