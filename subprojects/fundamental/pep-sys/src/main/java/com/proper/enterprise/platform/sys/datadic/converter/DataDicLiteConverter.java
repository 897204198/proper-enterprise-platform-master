package com.proper.enterprise.platform.sys.datadic.converter;

import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.datadic.DataDicLite;
import com.proper.enterprise.platform.sys.datadic.DataDicLiteBean;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import static com.proper.enterprise.platform.sys.datadic.DataDicLite.DD_CATALOG_CODE_SEPARATOR;

@Converter(autoApply = true)
public class DataDicLiteConverter implements AttributeConverter<DataDicLite, String> {

    /**
     * 将简化数据字典类型转换为关系型数据库中的形式
     * 具体形式为：类比 + 分割符 + 编码
     * @param attribute 数据字典属性
     * @return 字符串表示
     */
    @Override
    public String convertToDatabaseColumn(DataDicLite attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCatalog() + DD_CATALOG_CODE_SEPARATOR + attribute.getCode();
    }

    /**
     * 从数据库中存储内容转换成数据字典简化类型
     *
     * @param  dbData 关系型数据库中的数据字典表示
     * @return 简化数据字典类型
     */
    @Override
    public DataDicLite convertToEntityAttribute(String dbData) {
        if (StringUtil.isNull(dbData) || !dbData.contains(DD_CATALOG_CODE_SEPARATOR)) {
            return null;
        }
        String[] args = dbData.split(DD_CATALOG_CODE_SEPARATOR);
        return new DataDicLiteBean(args[0], args[1]);
    }

}
