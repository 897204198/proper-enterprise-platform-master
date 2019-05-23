package com.proper.enterprise.platform.auth.common.convert;

import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.auth.common.vo.MenuVO;
import com.proper.enterprise.platform.core.utils.CollectionUtil;

import java.util.*;

public class MenuConvert {

    public static MenuVO convert(Menu menu) {
        if (null == menu) {
            return null;
        }
        MenuVO menuVO = new MenuVO();
        menuVO.setName(menu.getName());
        menuVO.setMenuType(menu.getMenuType());
        menuVO.setId(menu.getId());
        menuVO.setParentId(null == menu.getParent() ? null : menu.getParent().getId());
        menuVO.setDescription(menu.getDescription());
        menuVO.setIcon(menu.getIcon());
        menuVO.setIdentifier(menu.getIdentifier());
        menuVO.setRoute(menu.getRoute());
        menuVO.setEnable(menu.getEnable());
        menuVO.setSequenceNumber(menu.getSequenceNumber());
        return menuVO;
    }

    public static Collection<MenuVO> convert(Collection<? extends Menu> menus) {
        List<MenuVO> menuVOs = new ArrayList<>();
        if (CollectionUtil.isEmpty(menus)) {
            return menuVOs;
        }
        Map<String, MenuVO> matchFast = new HashMap<>(menus.size());
        for (Menu menu : menus) {
            MenuVO menuVO = convert(menu);
            menuVOs.add(menuVO);
            matchFast.put(menuVO.getId(), menuVO);
        }
        for (Menu menu : menus) {
            if (null != menu.getParent()) {
                MenuVO parentVO = matchFast.get(menu.getParent().getId());
                if (null == parentVO) {
                    continue;
                }
                parentVO.addChild(matchFast.get(menu.getId()));
            }
        }
        return menuVOs;
    }
}
