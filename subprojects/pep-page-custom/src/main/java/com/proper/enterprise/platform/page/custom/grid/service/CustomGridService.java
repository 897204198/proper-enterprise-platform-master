package com.proper.enterprise.platform.page.custom.grid.service;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.page.custom.grid.document.CustomGridDocument;

public interface CustomGridService {

    /**
     * 配置信息翻页数据
     */
    DataTrunk<CustomGridDocument> getCustomGridForPage(String title, int pageNo, int pageSize);

    /**
     * 通过code取得配置信息
     */
    CustomGridDocument getCustomGridByCode(String code);

    /**
     * 通过id取得配置信息
     */
    CustomGridDocument getCustomGridById(String id);

    /**
     * 保存或更新配置信息
     */
    void saveOrUpdateCustomGrid(CustomGridDocument customGridDocument);

    /**
     * 删除配置信息
     */
    void deleteCustomGridByIds(String ids);
}
