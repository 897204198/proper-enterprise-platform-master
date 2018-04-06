package com.proper.enterprise.platform.oopsearch.api.serivce;

import com.proper.enterprise.platform.oopsearch.api.document.OOPSearchDocument;

import java.util.Collection;

/**
 * 查询服务类
 * */
public interface SearchService {

    /**
     * 从mongodb中根据输入内容，模糊查询到匹配内容集合并返回
     * @param  inputStr 查询框输入的字符串内容
     * @param  moduleName 查询配置类
     *
     * @return 查询文档集合
     * */
    Collection<? extends OOPSearchDocument> getSearchInfo(String inputStr, String moduleName);
}
