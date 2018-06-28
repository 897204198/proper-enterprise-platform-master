package com.proper.enterprise.platform.core.utils;

import com.proper.enterprise.platform.core.convert.ConvertElement;
import com.proper.enterprise.platform.core.convert.CycleConvertElement;
import com.proper.enterprise.platform.core.convert.ViewConvertElement;
import com.proper.enterprise.platform.core.convert.enums.ConvertType;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
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

    /**
     * copy source的属性值至具有相同属性的target
     * 支持同属性不同类型的copy
     * 支持集合copy 集合支持list，set
     *
     * @param source           source
     * @param target           target
     * @param ignoreProperties 需要忽略的属性
     */
    public static void copyProperties(Object source, Object target, String... ignoreProperties) {
        copyProperties(source, target, false, ignoreProperties);
    }

    /**
     * copy source的属性值至具有相同属性的target
     * 支持同属性不同类型的copy
     * 支持集合copy 集合支持list，set
     *
     * @param source           source
     * @param target           target
     * @param ignoreNull       是否忽略空属性 默认false
     * @param ignoreProperties 需要忽略的属性
     */
    public static void copyProperties(Object source, Object target, boolean ignoreNull, String... ignoreProperties) {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");
        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
        List<String> ignoreList = ignoreProperties != null ? Arrays.asList(ignoreProperties) : null;
        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            boolean ignoreRes = ignoreList == null || !ignoreList.contains(targetPd.getName());
            if (writeMethod != null && ignoreRes) {
                PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null
                        && ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                        try {
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            Object value = readMethod.invoke(source);
                            if (null == value && ignoreNull) {
                                continue;
                            }
                            //处理集合
                            if (Collection.class.isAssignableFrom(sourcePd.getPropertyType())) {
                                copyCollection(value, source, sourcePd, target, targetPd, ignoreProperties);
                                continue;
                            }
                            //处理类型不一致  多用于bean类型不一致但是bean之间有相同属性
                            if (targetPd.getPropertyType() != sourcePd.getPropertyType()) {
                                copyBean(value, target, sourcePd, targetPd, ignoreProperties);
                                continue;
                            }
                            writeMethod.invoke(target, value);
                        } catch (Throwable ex) {
                            throw new FatalBeanException(
                                "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                        }
                    }
                }
            }
        }
    }

    /**
     * 处理bean类型不一致的copy
     *
     * @param value            source值
     * @param target           目标对象
     * @param targetPd         targetPd
     * @param ignoreProperties 忽略的属性
     * @throws InvocationTargetException 对象封装异常
     * @throws IllegalAccessException    编译异常
     */
    private static void copyBean(Object value,
                                 Object target, PropertyDescriptor sourcePd, PropertyDescriptor targetPd,
                                 String... ignoreProperties) throws InvocationTargetException, IllegalAccessException {
        Method writeMethod = targetPd.getWriteMethod();
        if (null == value) {
            return;
        }
        Object copy = BeanUtil.newInstance(targetPd.getPropertyType());
        copyProperties(value, copy, handleIgnoreProperties(sourcePd.getName(), ignoreProperties));
        writeMethod.invoke(target, copy);
    }

    private static String[] handleIgnoreProperties(String sourceFiledName, String[] ignoreProperties) {
        if (ignoreProperties == null || ignoreProperties.length == 0) {
            return null;
        }
        List<String> nextIgnoreProperties = new ArrayList<>();
        for (String ignoreProperty : ignoreProperties) {
            if (ignoreProperty.startsWith(sourceFiledName + ".")) {
                nextIgnoreProperties.add(ignoreProperty.replaceFirst(sourceFiledName + "\\.", ""));
            }
        }
        if (0 == nextIgnoreProperties.size()) {
            return null;
        }
        return nextIgnoreProperties.toArray(new String[0]);
    }

    private static void copyCollection(Object value, Object source,
                                       PropertyDescriptor sourcePd, Object target,
                                       PropertyDescriptor targetPd,
                                       String... ignoreProperties) throws InvocationTargetException, IllegalAccessException {
        Class sourceGenericityType = BeanUtil.getCollectionGenericityType(source.getClass(), sourcePd.getName());
        Class targetGenericityType = BeanUtil.getCollectionGenericityType(target.getClass(), targetPd.getName());
        Method writeMethod = targetPd.getWriteMethod();
        if (null == value || sourceGenericityType == targetGenericityType) {
            writeMethod.invoke(target, value);
            return;
        }
        value = convertCollection((Collection) value, targetGenericityType, targetPd, handleIgnoreProperties(sourcePd.getName(), ignoreProperties));
        writeMethod.invoke(target, value);
    }

    private static Collection convertCollection(Collection sources,
                                                Class targetGenericityType,
                                                PropertyDescriptor targetPd, String... ignoreProperties) {
        if (CollectionUtil.isEmpty(sources)) {
            return null;
        }
        Collection targets;
        //特殊处理集合 目前支持Set,List的copy
        if (Set.class.isAssignableFrom(targetPd.getPropertyType())) {
            targets = new HashSet();
        } else if (List.class.isAssignableFrom(targetPd.getPropertyType())) {
            targets = new ArrayList();
        } else {
            return null;
        }
        for (Object source : sources) {
            Object target = BeanUtil.newInstance(targetGenericityType);
            copyProperties(source, target, ignoreProperties);
            targets.add(target);
        }
        return targets;
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
            return new ViewConvertElement<>(source, classType, convertType, new HashMap<>(1), showType).convert();
        }
        if (ignoreCycle) {
            return new CycleConvertElement<>(source, classType, convertType, new HashMap<>(1)).convert();
        }
        return new ConvertElement<>(source, classType, convertType).convert();
    }

    /**
     * 类型转换
     *
     * @param source           源对象
     * @param targetCls        目标对象类型
     * @param ignoreProperties 忽略属性
     * @param <T>              目标对象泛型
     * @return 目标对象实例
     */
    public static <T> T convert(Object source, Class<T> targetCls, String... ignoreProperties) {
        Assert.notNull(source, "Source must not be null");
        T t = newInstance(targetCls);
        copyProperties(source, t, false, ignoreProperties);
        return t;
    }

    /**
     * 集合类型转换
     *
     * @param sources          源对象集合
     * @param targetCls        目标对象类型
     * @param ignoreProperties 忽略属性
     * @param <T>              目标对象泛型
     * @return 目标对象实例集合
     */
    public static <T> Collection<T> convert(Collection sources, Class<T> targetCls, String... ignoreProperties) {
        Assert.notNull(sources, "Source must not be null");
        // 区分list和set，其余按照list处理
        Collection<T> targets;
        if (Set.class.isAssignableFrom(sources.getClass())) {
            targets = new HashSet();
        } else {
            targets = new ArrayList();
        }
        for (Object source : sources) {
            targets.add(convert(source, targetCls, ignoreProperties));
        }
        return targets;
    }

    @Deprecated
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
     * 获取类集合属性的泛型的具体类型
     *
     * @param cls       目标类
     * @param fieldName 属性名称
     * @return 泛型的具体类型
     */
    public static Class getCollectionGenericityType(Class cls, String fieldName) {
        return BeanUtil.getCollectionActualType(Objects.requireNonNull(BeanUtil.getField(cls, fieldName)));
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
