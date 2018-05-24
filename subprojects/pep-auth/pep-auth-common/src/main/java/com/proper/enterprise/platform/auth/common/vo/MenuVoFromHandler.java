package com.proper.enterprise.platform.auth.common.vo;

import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.core.convert.handler.FromHandler;
import com.proper.enterprise.platform.core.utils.CollectionUtil;

public class MenuVoFromHandler implements FromHandler<MenuVO, Menu> {

    @Override
    public void from(MenuVO menuVO, Menu menu) {
        menuVO.setLeaf(false);
        if (CollectionUtil.isEmpty(menu.getChildren()) && CollectionUtil.isEmpty(menu.getResources())) {
            menuVO.setLeaf(true);
        }
        if (null != menu.getParent()) {
            menuVO.setParentId(menu.getParent().getId());
        }
    }
}
