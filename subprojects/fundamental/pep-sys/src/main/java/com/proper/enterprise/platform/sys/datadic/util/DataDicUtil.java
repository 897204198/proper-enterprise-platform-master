package com.proper.enterprise.platform.sys.datadic.util;

import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.datadic.DataDic;
import com.proper.enterprise.platform.sys.datadic.DataDicLite;
import com.proper.enterprise.platform.sys.datadic.DataDicVO;
import com.proper.enterprise.platform.sys.datadic.service.DataDicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.Collection;

/**
 * 数据字典工具类
 */
public class DataDicUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataDicUtil.class);

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

    public static DataDic convert(DataDicLite dataDicLite) {
        if (dataDicLite == null) {
            return null;
        }
        DataDic dataDic = DataDicUtil.get(dataDicLite.getCatalog(), dataDicLite.getCode());
        if (dataDic == null) {
            LOGGER.debug("Could NOT find data dictionary with catalog {} and code {}", dataDicLite.getCatalog(), dataDicLite.getCode());
            return null;
        }
        DataDic vo = new DataDicVO();
        BeanUtils.copyProperties(dataDic, vo);
        return vo;
    }

    public static boolean isNull(DataDicLite dataDicLite) {
        if (dataDicLite == null) {
            return true;
        }
        if (StringUtil.isNull(dataDicLite.getCatalog())) {
            return true;
        }
        if (StringUtil.isNull(dataDicLite.getCode())) {
            return true;
        }
        return false;
    }

}
