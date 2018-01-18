package com.proper.enterprise.platform.api.auth.service;

import com.proper.enterprise.platform.api.auth.model.Resource;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;
import java.util.Map;

public interface ResourceService {

    Resource save(Resource resource);

    /**
     * 保存资源信息
     *
     * @param map 请求参数
     * @return 资源信息
     */
    Resource save(Map<String, Object> map);

    Resource get(String id);

    Resource get(String url, RequestMethod method);

    Collection<Resource> find();

    void delete(Resource resource);

    /**
     * 删除多条资源数据
     *
     * @param ids 以 , 分隔的待删除资源ID列表
     */
    boolean deleteByIds(String ids);

    /**
     * 更新资源状态
     *
     * @param idList 资源ID列表
     * @param enable 资源状态
     * @return 结果
     */
    Collection<? extends Resource> updateEanble(Collection<String> idList, boolean enable);
}
