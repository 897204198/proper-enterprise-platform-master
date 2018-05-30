package com.proper.enterprise.platform.sys.datadic.util;

import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.sys.datadic.DataDic;
import com.proper.enterprise.platform.sys.datadic.service.DataDicService;

import java.util.Collection;

/**
 * 数据字典工具类
 */
public class DataDicUtil {

    private DataDicUtil() {

    }

    /**
     * 获得某分类下的数据字典集合
     *
     * @param catalog 分类名
     * @return 数据字典集合
     */
    public static Collection<? extends DataDic> findByCatalog(String catalog) {
        return PEPApplicationContext.getBean(DataDicService.class).findByCatalog(catalog);
    }

    /**
     * 根据分类及编码获得数据字典节点
     *
     * @param catalog 分类
     * @param code    编码
     * @return 数据字典节点
     */
    public static DataDic get(String catalog, String code) {
        return PEPApplicationContext.getBean(DataDicService.class).get(catalog, code);
    }

    /**
     * 根据分类及编码获得数据字典节点
     *
     * @param dataDicEnum 字典枚举
     * @return 数据字典节点
     */
    public static DataDic get(Enum dataDicEnum) {
        return PEPApplicationContext.getBean(DataDicService.class).get(dataDicEnum);
    }

    /**
     * 获得某分类下的默认数据字典项
     *
     * @param catalog 分类
     * @return 默认项，或 null
     */
    public static DataDic getDefault(String catalog) {
        return PEPApplicationContext.getBean(DataDicService.class).getDefault(catalog);
    }

}
