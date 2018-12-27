package com.proper.enterprise.platform.auth.common.service.impl

import com.proper.enterprise.platform.api.auth.dao.MenuDao
import com.proper.enterprise.platform.api.auth.dao.ResourceDao
import com.proper.enterprise.platform.api.auth.dao.RoleDao
import com.proper.enterprise.platform.api.auth.dao.UserDao
import com.proper.enterprise.platform.api.auth.enums.EnableEnum
import com.proper.enterprise.platform.api.auth.model.Menu
import com.proper.enterprise.platform.api.auth.service.MenuService
import com.proper.enterprise.platform.api.auth.service.RoleService
import com.proper.enterprise.platform.auth.common.jpa.entity.MenuEntity
import com.proper.enterprise.platform.auth.common.jpa.entity.ResourceEntity
import com.proper.enterprise.platform.auth.common.jpa.entity.RoleEntity
import com.proper.enterprise.platform.auth.common.jpa.entity.UserEntity
import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.core.exception.ErrMsgException
import com.proper.enterprise.platform.core.i18n.I18NUtil
import com.proper.enterprise.platform.core.security.Authentication
import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

import static com.proper.enterprise.platform.core.PEPApplicationContext.getBean

@Sql("/com/proper/enterprise/platform/auth/common/service/impl/identity.sql")
class MenuServiceImplTest extends AbstractJPATest {

    @Autowired
    private MenuService menuService
    @Autowired
    private MenuDao menuDao

    @Autowired
    private UserDao userDao;

    @Autowired
    private ResourceDao resourceDao

    @Autowired
    private RoleService roleService
    @Autowired
    private RoleDao roleDao

    /**
     * 保存菜单
     */
    @Test
    void testSaveMenu() {
        def menu = new MenuEntity()
        try {
            menuService.save(menu)
        } catch (ErrMsgException e) {
            assert e.getMessage() == I18NUtil.getMessage("pep.auth.common.menu.route.empty")
        }
        menu.setRoute("ppp")
        try {
            menuService.save(menu)
        } catch (ErrMsgException e) {
            assert e.getMessage() == I18NUtil.getMessage("pep.auth.common.menu.name.empty")
        }

        menu.setName("menu1")
        try {
            menuService.update(menu)
        } catch (ErrMsgException e) {
            assert e.getMessage() == I18NUtil.getMessage("pep.auth.common.menu.name.duplicated")
        }
        menu.setName('测试update')
        menu.setRoute("/test3")
        try {
            menuService.update(menu)
        } catch (ErrMsgException e) {
            assert e.getMessage() == I18NUtil.getMessage("pep.auth.common.menu.route.duplicated")
        }
        Menu menu2 = menuInit()
        assert menu2.name == '测试'

    }

    private Menu menuInit() {
        def menuEntity = new MenuEntity()
        menuEntity.setName('测试')
        menuEntity.setRoute('/test')
        menuEntity.setParentId('/parentIdTest')
        menuEntity.setEnable(true)
        menuEntity.setDescription('description')

        def resourcesEntuty = new ResourceEntity()
        resourcesEntuty.setName('测试')
        resourcesEntuty.setUrl('/test')
        resourcesEntuty.setIdentifier('identifier')
        def resource = resourceDao.save(resourcesEntuty)

        def list = new ArrayList()
        list.add(resource)
        menuEntity.setResourceEntities(list)

        def roleEntuty = new RoleEntity()
        roleEntuty.setName('name1')
        roleEntuty.setEnable(true)
        def role = roleService.save(roleEntuty)
        def roleList = new ArrayList()
        roleList.add(role)
        menuEntity.setRoleEntities(roleList)

        def menu = menuService.save(menuEntity)
        menu
    }

    private UserEntity userInit() {
        def userEntity = new UserEntity()
        userEntity.setName("user")
        userEntity.setUsername('user')
        userEntity.setPassword('user')
        userEntity.setSuperuser(true)
        def entity = userDao.save(userEntity) as UserEntity
        entity

    }

    @Test
    void testUpdate() {
        def menu = menuInit()
        menu.setName('测试update')
        menu.setMenuCode("2")
        def updateMenu = menuService.update(menu)
        assert '测试update' == updateMenu.getName()
    }

    @Test
    void testGetMenusUser() {
        menuInit()
        def user = userInit()
        def menus = menuService.getMenus(user)
        assert menus.size() > 0

        def user2 = userDao.getByUsername("testuser2", EnableEnum.ENABLE)
        def menus2 = menuService.getMenus(user2)
        assert menus2.size() == 1
    }

    @Test
    void testGetMenusParam() {
        Authentication.setCurrentUserId("admin")
        def menus = menuService.getMenus("menu2", null, null, EnableEnum.ENABLE, null)
        assert menus.size() > 0

        def menus1 = menuService.getMenus("menu3", null, null, EnableEnum.DISABLE, null)
        assert menus1.size() > 0

        def menus2 = menuService.getMenus("menu3", null, null, EnableEnum.ALL, null)
        assert menus2.size() > 0
    }

    @Test
    void testFindMenusPagination() {
        def menu = menuInit()
        DataTrunk dataTrunkMenu = JSONUtil.parse(get('/auth/menus?pageNo=1&pageSize=3&' + menu.name + '', HttpStatus.OK).response.getContentAsString(), DataTrunk.class)
        assert dataTrunkMenu.count > 0

    }

    @Test
    void testGetFilterMenusAndParent() {
        def menus = new ArrayList()
        def menu = menuInit()
        menus.add(menu)
        def parent = menuService.getFilterMenusAndParent(menus)
        assert parent.size() > 0
    }

    @Test
    void testDeleteByIds() {
        def init = menuInit()
        def string = delete('/auth/menus?ids=' + init.id + '', HttpStatus.INTERNAL_SERVER_ERROR).getResponse().contentAsString
        assert string == I18NUtil.getMessage('pep.auth.common.menu.delete.relation.role')
        menuService.deleteByIds("998")
    }

    @Test
    void testGetMenuParents() {
        menuInit()
        def menus = menuService.getMenuParents(EnableEnum.ENABLE)
        assert menus.size() > 0
    }

    @Test
    void testUpdateEnable() {
        def menu = menuInit()
        def menus = new ArrayList()
        menus.add(menu.id)
        menus = menuService.updateEnable(menus, false)
        assert menus[0].getEnable() == false
    }


    @Test
    void testAddResourceOfMenu() {
        def menu = menuInit()
        ResourceEntity resourceEntity = new ResourceEntity()
        resourceEntity.setName('资源')
        resourceEntity.setUrl('/test/test')
        resourceEntity.setResourceCode('testCode')
        resourceEntity.setIdentifier('identifier2')
        def resource = menuService.addResourceOfMenu(menu.id, resourceEntity)
        assert '资源' == resource.name
    }

    @Test
    void testDeleteResourceOfMenu() {
        def menu = menuInit()
        ResourceEntity resourceEntity = new ResourceEntity()
        resourceEntity.setName('测试删除资源')
        resourceEntity.setUrl('/test/test')
        resourceEntity.setResourceCode('testCode')
        resourceEntity.setIdentifier('identifier2')
        def resource = menuService.addResourceOfMenu(menu.id, resourceEntity)
        assert '测试删除资源' == resource.name
        menuService.deleteResourceOfMenu(menu.id, resource.id)
        def resources = menuService.getMenuResources(menu.id, EnableEnum.ALL, EnableEnum.ALL)
        assert resources.size() == 1
        assert resources[0].name != '测试删除资源'
    }

    @Test
    void test_GetMenuResources() {
        def menu = menuInit()
        def resources = menuService.getMenuResources(menu.id, EnableEnum.ENABLE, EnableEnum.ENABLE)
        assert resources.size() > 0

        menuService.getMenuResources("ttt", EnableEnum.ENABLE, EnableEnum.ENABLE)

    }

    @Test
    void test_GetMenuRoles() {
        def menu = menuInit()
        def roles = menuService.getMenuRoles(menu.id, EnableEnum.DISABLE, EnableEnum.ENABLE)
        assert roles.size() > 0
        menuService.getMenuRoles("ttt", EnableEnum.DISABLE, EnableEnum.ENABLE)
    }

    @Test

    void test_GetMenuAllResources() {
        Authentication.setCurrentUserId('admin')
        menuInit()
        def resources = menuService.getMenuAllResources()
        assert resources.size() > 0
    }


    @Test
    void testAccessible() {
        def bean = (List<String>) getBean("ignorePatternsList")
        bean.add("*/test/**")
        assert true == menuService.accessible(null, "user2")

        def resourceEntity = new ResourceEntity()
        resourceEntity.setName("test")
        resourceEntity.setUrl("/test")
        def resource = resourceDao.save(resourceEntity)
        assert true == menuService.accessible(resource, "user2")

        resourceEntity.setUrl("/aaa")
        resourceEntity.setEnable(false)
        assert false == menuService.accessible(resource, "user2")

        resourceEntity.setEnable(true)
        assert true == menuService.accessible(resourceEntity, "user2")

        def menus = menuDao.findAll()
        resourceEntity.setMenuEntities(menus)
        assert false == menuService.accessible(resourceEntity, "user2")

        def user = new UserEntity()
        user.setSuperuser(true)
        user.setUsername("Zltest1")
        user.setPassword("ZLPassword")
        def save = userDao.save(user)
        assert true == menuService.accessible(resourceEntity, save.getId())

        bean.clear()
    }

}
