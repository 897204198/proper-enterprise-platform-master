package com.proper.enterprise.platform.core.convert;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.core.api.IBase;
import com.proper.enterprise.platform.core.convert.enums.ConvertType;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 与JSON相关联的转换
 * 忽略JsonIgnore字段和JsonView未匹配字段
 *
 * @param <T> 目标泛型
 */
public class ViewConvertElement<T> extends CycleConvertElement<T> {

    public ViewConvertElement(Object source, Class<T> classType, ConvertType type, Map<String, Boolean> pathMap, Class[] showTypes) {
        super(source, classType, type, pathMap);
        this.setShowTypes(showTypes);
    }

    public ViewConvertElement(Object source, String className, ConvertType type, Map<String, Boolean> pathMap, Class[] showTypes) {
        super(source, className, type, pathMap);
        this.setShowTypes(showTypes);
    }

    private Class[] showTypes;

    @Override
    public T convert() {
        if (null == getFrom()) {
            return null;
        }
        Map<String, Boolean> ignoreFileName = new HashMap<>();
        if (ConvertType.FROM_TYPE == getType()) {
            for (Field field : getAnnotationFields()) {
                if (null != getShowTypes() && getShowTypes().length > 0) {
                    if (null != field.getAnnotation(JsonIgnore.class)) {
                        ignoreFileName.put(field.getName(), true);
                        continue;
                    }
                    JsonView jsonView = field.getAnnotation(JsonView.class);
                    if (null == jsonView) {
                        ignoreFileName.put(field.getName(), true);
                        continue;
                    }
                    Class[] showTypes = jsonView.value();
                    if (0 == showTypes.length) {
                        ignoreFileName.put(field.getName(), true);
                        continue;
                    }
                    boolean haveShowType = false;
                    for (Class jsonShowType : showTypes) {
                        for (Class meShowType : getShowTypes()) {
                            if (jsonShowType.isAssignableFrom(meShowType)) {
                                haveShowType = true;
                            }
                        }
                    }
                    if (!haveShowType) {
                        ignoreFileName.put(field.getName(), true);
                    }
                }
            }
        }
        BeanUtils.copyProperties(getFrom(), getTarget(), ignoreFileName.keySet().toArray(new String[ignoreFileName.size()]));
        for (Field field : getAnnotationFields()) {
            if (null == ignoreFileName.get(field.getName())) {
                this.setCurrentField(field);
                handlePOJOConverters();
                handlePOJOConverter();
            }
        }
        return getTarget();
    }


    @Override
    public void writeValue() {
        if (null == getWriteMethod()) {
            return;
        }
        try {
            if (getValue() instanceof Collection) {
                //若写入的时候发现sourceValue类型可以递归  则将sourceValue和写入类型继续做递归转换
                Collection collectionValue = (Collection) getValue();
                Collection result = new ArrayList<>(collectionValue.size());
                for (Object o : collectionValue) {
                    result.add(new ViewConvertElement<>(o,
                        BeanUtil.getCollectionActualType(getWriteField()), getType(), this.getPathMap(), getShowTypes()).convert());
                }
                getWriteMethod().invoke(getTarget(), result);
                cleanPath(getTarget().getClass(), BeanUtil.getCollectionActualType(getWriteField()));
                return;
            }
            if (getValue() instanceof IBase) {
                //若写入的时候发现sourceValue类型可以递归  则将sourceValue和写入类型继续做递归转换
                getWriteMethod().invoke(getTarget(),
                    new ViewConvertElement<>(getValue(),
                        getWriteField().getType(), getType(), this.getPathMap(), getShowTypes()).convert());
                cleanPath(getTarget().getClass(), getWriteField().getType());
                return;
            }
            getWriteMethod().invoke(getTarget(), getValue());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new FatalBeanException("can't setValue targetField:" + getWriteMethod().getName(), e);
        }
    }

    private Class[] getShowTypes() {
        return showTypes;
    }

    private void setShowTypes(Class[] showTypes) {
        this.showTypes = showTypes;
    }

}
