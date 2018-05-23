package com.proper.enterprise.platform.core.utils;

import com.proper.enterprise.platform.core.convert.ConvertElement;
import com.proper.enterprise.platform.core.convert.CycleConvertElement;
import com.proper.enterprise.platform.core.convert.ViewConvertElement;
import com.proper.enterprise.platform.core.convert.enums.ConvertType;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.FatalBeanException;

import java.lang.reflect.*;
import java.util.*;

public class BeanUtil {

    private BeanUtil() {
    }

    /**
     * 根据类型实例化对象
     *
     * @param classType 类型
     * @param <T>       泛型
     * @return 泛型实例
     */
    public static <T> T newInstance(Class<T> classType) {
        T t;
        try {
            t = classType.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new FatalBeanException("Could newInstance" + classType.getName(), e);
        }
        return t;
    }

    /**
     * 获取所有属性，包括父类属性
     *
     * @param entityClass 业务实体类
     * @return 属性列表
     */
    public static List<Field> getAllFields(Class<?> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();
        return getAllFields(entityClass.getSuperclass(), new ArrayList<>(Arrays.asList(fields)));
    }

    private static List<Field> getAllFields(Class<?> entityClass, List<Field> fields) {
        if (Object.class.equals(entityClass)) {
            return fields;
        }
        Collections.addAll(fields, entityClass.getDeclaredFields());
        return getAllFields(entityClass.getSuperclass(), fields);
    }

    /**
     * 根据属性名称获取属性
     *
     * @param entityClass 业务实体类
     * @param fieldName   属性名称
     * @return 属性
     */
    public static Field getField(Class<?> entityClass, String fieldName) {
        if (StringUtils.isEmpty(fieldName)) {
            return null;
        }
        List<Field> fields = getAllFields(entityClass);
        for (Field field : fields) {
            if (fieldName.equals(field.getName())) {
                return field;
            }
        }
        return null;
    }

    public static <T, S> T convertToDO(S source, Class<T> classType) {
        return convert(source, classType, ConvertType.TARGET_TYPE, false, false, null);
    }

    public static <T, S> Collection<T> convertToDO(Collection<S> collection, Class<T> classType) {
        return convert(collection, classType, ConvertType.TARGET_TYPE, false, false, null);
    }

    public static <T, S> DataTrunk<T> convertToDO(DataTrunk<S> dataTrunk, Class<T> classType) {
        return convert(dataTrunk, classType, ConvertType.TARGET_TYPE, false, false, null);
    }

    public static <T, S> T convertToVO(S source, Class<T> classType, Class... showType) {
        return convert(source, classType, ConvertType.FROM_TYPE, false, true, showType);
    }

    public static <T, S> Collection<T> convertToVO(Collection<S> collection, Class<T> classType, Class... showType) {
        return convert(collection, classType, ConvertType.FROM_TYPE, false, true, showType);
    }

    public static <T, S> DataTrunk<T> convertToVO(DataTrunk<S> dataTrunk, Class<T> classType, Class... showType) {
        return convert(dataTrunk, classType, ConvertType.FROM_TYPE, false, true, showType);
    }

    public static <T, S> T convert(S source, Class<T> classType, ConvertType convertType,
                                   boolean ignoreCycle, boolean ignoreWithView, Class... showType) {
        if (ignoreWithView) {
            return new ViewConvertElement<>(source, classType, convertType, new HashMap<>(), showType).convert();
        }
        if (ignoreCycle) {
            return new CycleConvertElement<>(source, classType, convertType, new HashMap<>()).convert();
        }
        return new ConvertElement<>(source, classType, convertType).convert();
    }

    public static <T, S> Collection<T> convert(Collection<S> collection, Class<T> classType, ConvertType convertType,
                                               boolean ignoreCycle, boolean ignoreWithView, Class... showType) {
        if (null == collection) {
            return null;
        }
        Collection<T> result = new ArrayList<>(collection.size());
        for (S s : collection) {
            result.add(convert(s, classType, convertType, ignoreCycle, ignoreWithView, showType));
        }
        return result;
    }

    public static <T, S> DataTrunk<T> convert(DataTrunk<S> dataTrunk, Class<T> classType, ConvertType convertType,
                                              boolean ignoreCycle, boolean ignoreWithView, Class... showType) {
        if (null == dataTrunk) {
            return null;
        }
        DataTrunk<T> dataTrunkT = new DataTrunk<>();
        dataTrunkT.setCount(dataTrunk.getCount());
        dataTrunkT.setData(convert(dataTrunk.getData(), classType, convertType, ignoreCycle, ignoreWithView, showType));
        return dataTrunkT;
    }

    /**
     * 获得集合的泛型类型
     *
     * @param field 字段
     * @return 类型
     */
    public static Class getCollectionActualType(Field field) {
        ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
        Type type = parameterizedType.getActualTypeArguments()[0];
        return (Class) type;
    }

    /**
     * 根据className获得类型
     *
     * @param className className
     * @return 类型
     */
    public static Class getClassType(String className) {
        Class targetClassType = null;
        if (StringUtil.isNotEmpty(className)) {
            try {
                targetClassType = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new FatalBeanException("can't find class by className:" + className, e);
            }
        }
        return targetClassType;
    }

    /**
     * 判断两个类型是否一致
     *
     * @param a a类型
     * @param b b类型
     * @return 是否一致 一致true 不一致false
     */
    public static boolean classTypeEqual(Class a, Class b) {
        return a == b;
    }

}
