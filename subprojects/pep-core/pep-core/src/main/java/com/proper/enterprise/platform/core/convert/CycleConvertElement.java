package com.proper.enterprise.platform.core.convert;

import com.proper.enterprise.platform.core.api.IBase;
import com.proper.enterprise.platform.core.convert.enums.ConvertType;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * 处理循环
 * A-B类型
 * 若A跟B的关系以及有了 则不会再记录B-A的关系
 * A自己递归不记录
 *
 * @param <T> 目标泛型
 */
public class CycleConvertElement<T> extends ConvertElement<T> {


    public CycleConvertElement(Object source, Class<T> classType, ConvertType type, Map<String, Boolean> pathMap) {
        super(source, classType, type);
        this.setPathMap(pathMap);
    }

    public CycleConvertElement(Object source, String className, ConvertType type, Map<String, Boolean> pathMap) {
        super(source, className, type);
        this.setPathMap(pathMap);
    }

    /**
     * 已经解析的关系路径
     */
    private Map<String, Boolean> pathMap;

    @Override
    public T convert() {
        if (null == getFrom()) {
            return null;
        }
        BeanUtils.copyProperties(getFrom(), getTarget());
        for (Field field : getAnnotationFields()) {
            this.setCurrentField(field);
            handlePOJOConverters();
            handlePOJOConverter();
        }
        return getTarget();
    }


    @Override
    public Object readValue() {
        if (Iterable.class.isAssignableFrom(getWriteField().getType())) {
            if (IBase.class.isAssignableFrom(BeanUtil.getCollectionActualType(getWriteField()))) {
                if (havePath(getTarget().getClass(), BeanUtil.getCollectionActualType(getWriteField()), this.getPathMap())) {
                    return null;
                }
                recordPath(getTarget().getClass(), BeanUtil.getCollectionActualType(getWriteField()), this.getPathMap());
            }
        }
        if (IBase.class.isAssignableFrom(getWriteField().getType())) {
            if (havePath(getTarget().getClass(), getWriteField().getType(), this.getPathMap())) {
                return null;
            }
            recordPath(getTarget().getClass(), getWriteField().getType(), this.getPathMap());
        }
        return super.readValue();
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
                    result.add(new CycleConvertElement<>(o,
                        BeanUtil.getCollectionActualType(getWriteField()), getType(), this.getPathMap()).convert());
                }
                getWriteMethod().invoke(getTarget(), result);
                cleanPath(BeanUtil.getCollectionActualType(getWriteField()),  BeanUtil.getCollectionActualType(getReadField()));
                return;
            }
            if (getValue() instanceof IBase) {
                //若写入的时候发现sourceValue类型可以递归  则将sourceValue和写入类型继续做递归转换
                getWriteMethod().invoke(getTarget(),
                    new CycleConvertElement<>(getValue(),
                        getWriteField().getType(), getType(), this.getPathMap()).convert());
                cleanPath(getWriteField().getType(), getReadField().getType());
                return;
            }
            getWriteMethod().invoke(getTarget(), getValue());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new FatalBeanException("can't setValue targetField:" + getWriteMethod().getName(), e);
        }
    }


    protected Map<String, Boolean> getPathMap() {
        return pathMap;
    }

    protected void setPathMap(Map<String, Boolean> pathMap) {
        this.pathMap = pathMap;
    }

    private void recordPath(Class a, Class b, Map<String, Boolean> pathMap) {
        if (null == pathMap) {
            return;
        }
        if (a == null || b == null) {
            return;
        }
        if (BeanUtil.classTypeEqual(a, b)) {
            return;
        }
        pathMap.put(getSign(a, b), true);
    }

    private String getSign(Class a, Class b) {
        if (a == null || b == null) {
            return null;
        }
        return a.getName() + "|" + b.getName();
    }

    private boolean havePath(Class a, Class b, Map<String, Boolean> pathMap) {
        if (null == pathMap) {
            return false;
        }
        if (a == null || b == null) {
            return false;
        }
        return null != pathMap.get(getSign(a, b)) || null != pathMap.get(getSign(b, a));
    }

    protected void cleanPath(Class a, Class b) {
        this.getPathMap().remove(getSign(a, b));
        this.getPathMap().remove(getSign(b, a));
    }

}
