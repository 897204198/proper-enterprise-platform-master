package com.proper.enterprise.platform.auth.common.neo4j.converter;

import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.datadic.DataDicLite;
import com.proper.enterprise.platform.sys.datadic.DataDicLiteBean;
import org.neo4j.ogm.typeconversion.AttributeConverter;

import static com.proper.enterprise.platform.sys.datadic.DataDicLite.DD_CATALOG_CODE_SEPARATOR;

public class GraphDataDicLiteConverter implements AttributeConverter<DataDicLite, String> {

    /**
     * 将简化数据字典类型转换为Neo4j数据库中的形式
     * 具体形式为：类比 + 分割符 + 编码
     * @param value 数据字典属性
     * @return 字符串表示
     */
    @Override
    public String toGraphProperty(DataDicLite value) {
        if (value == null) {
            return null;
        }
        return value.getCatalog() + DD_CATALOG_CODE_SEPARATOR + value.getCode();
    }

    /**
     * 从数据库中存储内容转换成数据字典简化类型
     *
     * @param  value Neo4j数据库中的数据字典表示
     * @return 简化数据字典类型
     */
    @Override
    public DataDicLiteBean toEntityAttribute(String value) {
        if (StringUtil.isNull(value) || !value.contains(DD_CATALOG_CODE_SEPARATOR)) {
            return null;
        }
        String[] args = value.split(DD_CATALOG_CODE_SEPARATOR);
        return new DataDicLiteBean(args[0], args[1]);
    }
}
