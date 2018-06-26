package com.proper.enterprise.platform.core.convert;

import com.proper.enterprise.platform.core.api.IBase;
import com.proper.enterprise.platform.core.convert.annotation.POJOConverter;
import com.proper.enterprise.platform.core.convert.annotation.POJOConverters;
import com.proper.enterprise.platform.core.convert.enums.ConvertType;
import com.proper.enterprise.platform.core.convert.handler.FromHandler;
import com.proper.enterprise.platform.core.convert.handler.TargetHandler;
import com.proper.enterprise.platform.core.convert.handler.TransFormHandler;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConvertElement<T> {

    public ConvertElement(Object source, Class<T> classType, ConvertType type) {
        init(source, classType, type);
    }

    public ConvertElement(Object source, String className, ConvertType type) {
        init(source, BeanUtil.getClassType(className), type);
    }

    private void init(Object source, Class<T> classType, ConvertType type) {
        boolean isTypeIllegal = null == type || !ConvertType.TARGET_TYPE.equals(type) && !ConvertType.FROM_TYPE.equals(type);
        if (isTypeIllegal) {
            throw new FatalBeanException("type is illegality when convert init");
        }
        if (null == classType) {
            throw new FatalBeanException("classType is null when convert init");
        }
        this.setClassType(classType);
        this.setType(type);
        this.annotationFields = new ArrayList<>();
        if (ConvertType.FROM_TYPE.equals(type)) {
            this.setMe(BeanUtil.newInstance(getMGenericityType()));
            this.setFrom(source);
            this.setTarget(BeanUtil.newInstance(getTGenericityType()));
        }
        if (ConvertType.TARGET_TYPE.equals(type)) {
            this.setMe(source);
            this.setFrom(source);
            this.setTarget(BeanUtil.newInstance(getTGenericityType()));
        }
        this.setAnnotationFields(BeanUtil.getAllFields(getMGenericityType()));
    }

    /**
     * me
     */
    private Object me;
    /**
     * 从f转到m
     */
    private Object from;
    /**
     * 从m转到target
     */
    private T target;

    private Class<T> classType;
    /**
     * 注解字段集合
     */
    private List<Field> annotationFields;
    /**
     * 转换类型
     * from
     * target
     */
    private ConvertType type;
    /**
     * 当前pojoConverter
     */
    private POJOConverter currentPojoConverter;
    /**
     * 当前正在转换的字段
     */
    private Field currentField;
    /**
     * 当前字段获得的值
     */
    private Object value;
    /**
     * 当前读方法
     */
    private Method readMethod;
    /**
     * 当前写方法
     */
    private Method writeMethod;


    public T convert() {
        if (null == getFrom()) {
            return null;
        }
        BeanUtils.copyProperties(this.from, this.target);
        for (Field field : getAnnotationFields()) {
            this.setCurrentField(field);
            handlePOJOConverters();
            handlePOJOConverter();
        }
        return getTarget();
    }

    public void handleValue() {
        //获得当前get值 若null无需继续
        this.setValue(readValue());
        if (null == this.getValue()) {
            setNull();
            return;
        }
        writeValue();
    }

    public Object readValue() {
        Object sourceValue;
        if (null == getReadMethod()) {
            return null;
        }
        try {
            sourceValue = getReadMethod().invoke(from);
            if (null == sourceValue) {
                return null;
            }
        } catch (Throwable ex) {
            throw new FatalBeanException("Could getValue'" + getReadMethod().getName() + "' from source", ex);
        }
        return sourceValue;
    }


    public void writeValue() {
        if (null == getWriteMethod()) {
            return;
        }
        try {
            if (value instanceof Collection) {
                //若写入的时候发现sourceValue类型可以递归  则将sourceValue和写入类型继续做递归转换
                Collection collectionValue = (Collection) getValue();
                Collection result = new ArrayList<>(collectionValue.size());
                for (Object o : collectionValue) {
                    result.add(new ConvertElement<>(o, BeanUtil.getCollectionActualType(getWriteField()), type).convert());
                }
                getWriteMethod().invoke(getTarget(), result);
                return;
            }
            if (value instanceof IBase) {
                //若写入的时候发现sourceValue类型可以递归  则将sourceValue和写入类型继续做递归转换
                getWriteMethod().invoke(getTarget(), new ConvertElement<>(getValue(), getWriteField().getType(), type).convert());
                return;
            }
            getWriteMethod().invoke(getTarget(), getValue());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new FatalBeanException("can't setValue targetField:" + getWriteMethod().getName(), e);
        }
    }


    protected void handlePOJOConverters() {
        POJOConverters pojoConverters = getCurrentField().getAnnotation(POJOConverters.class);
        if (null == pojoConverters) {
            return;
        }
        for (POJOConverter pojoConverter : pojoConverters.value()) {
            if (null == pojoConverter) {
                continue;
            }

            this.setCurrentPojoConverter(pojoConverter);
            transForm();
        }
    }

    protected void handlePOJOConverter() {
        POJOConverter pojoConverter = getCurrentField().getAnnotation(POJOConverter.class);
        if (null == pojoConverter) {
            return;
        }
        this.setCurrentPojoConverter(pojoConverter);
        transForm();
    }

    private void transForm() {
        validateFieldName(getCurrentPojoConverter());
        if (classTypeEqual()) {
            return;
        }
        if (!annotationClassTypeMatch()) {
            return;
        }
        //初始化get set方法
        this.setReadMethod(buildReadMethod());
        this.setWriteMethod(buildWriteMethod());
        if (null == getWriteMethod()) {
            return;
        }
        //如果有特殊处理则走特殊处理
        if (handleSpecial()) {
            return;
        }
        handleValue();
    }

    private boolean handleSpecial() {
        //获得特殊处理的处理器  若from拿fromHandleBy否则拿targetHandleBy
        Class<? extends TransFormHandler> transFormHandlerClass = ConvertType.FROM_TYPE.equals(type)
                ? getCurrentPojoConverter().fromHandleBy()
                : getCurrentPojoConverter().targetHandleBy();
        if (transFormHandlerClass != FromHandler.class && transFormHandlerClass != TargetHandler.class) {
            TransFormHandler transFormHandler = BeanUtil.newInstance(transFormHandlerClass);
            if (null != transFormHandler) {
                transFormHandler.transform(target, from);
                return true;
            }
        }
        return false;
    }

    protected Field getWriteField() {
        return ConvertType.FROM_TYPE.equals(type) ? getCurrentField() : BeanUtil.getField(target.getClass(), getCurrentPojoConverter().fieldName());
    }

    protected Field getReadField() {
        Field readField = ConvertType.FROM_TYPE.equals(type)
                ? BeanUtil.getField(from.getClass(), getCurrentPojoConverter().fieldName())
                : getCurrentField();
        if (null == readField) {
            throw new FatalBeanException("can't find readField:" + (ConvertType.FROM_TYPE.equals(type)
                    ? getCurrentPojoConverter().fieldName()
                    : getCurrentField().getName()));
        }
        return readField;
    }

    private Method buildWriteMethod() {
        //获得写PD  若from拿自己PD 若target拿对面PD
        PropertyDescriptor writePd = null;
        if (ConvertType.FROM_TYPE.equals(type)) {
            writePd = BeanUtils.getPropertyDescriptor(target.getClass(), getCurrentField().getName());
            if (null == writePd) {
                throw new FatalBeanException("can't find fieldName:" + getCurrentField().getName() + "from" + target.getClass());
            }
        }
        if (ConvertType.TARGET_TYPE.equals(type)) {
            writePd = BeanUtils.getPropertyDescriptor(target.getClass(), getCurrentPojoConverter().fieldName());
            if (null == writePd) {
                throw new FatalBeanException("can't find fieldName:" + getCurrentPojoConverter().fieldName() + "from" + target.getClass());
            }
        }
        if (null == writePd) {
            return null;
        }
        Method targetWriteMethod = writePd.getWriteMethod();
        if (null == targetWriteMethod) {
            return null;
        }
        targetWriteMethod.setAccessible(true);
        return targetWriteMethod;
    }

    private Method buildReadMethod() {
        PropertyDescriptor sourcePd = null;
        if (ConvertType.FROM_TYPE.equals(type)) {
            sourcePd = BeanUtils.getPropertyDescriptor(from.getClass(), getCurrentPojoConverter().fieldName());
            if (null == sourcePd) {
                throw new FatalBeanException("can't find fieldName:" + getCurrentPojoConverter().fieldName());
            }
        }
        if (ConvertType.TARGET_TYPE.equals(type)) {
            sourcePd = BeanUtils.getPropertyDescriptor(from.getClass(), getCurrentField().getName());
            if (null == sourcePd) {
                throw new FatalBeanException("can't find fieldName:" + getCurrentField().getName());
            }
        }
        if (null == sourcePd) {
            return null;
        }
        Method sourceReadMethod = sourcePd.getReadMethod();
        if (null == sourceReadMethod) {
            return null;
        }
        sourceReadMethod.setAccessible(true);
        return sourceReadMethod;
    }

    private boolean classTypeEqual() {
        //若From且source==classType则无需转换  若target且target==classType无需转换
        return BeanUtil.classTypeEqual(from.getClass(), target.getClass());
    }

    private boolean annotationClassTypeMatch() {
        //若from  则判断当前的类型与注解from类型是否一致 className优先
        if (ConvertType.FROM_TYPE.equals(type)) {
            Class fromClassType = BeanUtil.getClassType(getCurrentPojoConverter().fromClassName());
            if (null == fromClassType) {
                fromClassType = getCurrentPojoConverter().fromBy();
            }
            return BeanUtil.classTypeEqual(getFrom().getClass(), fromClassType);
        }
        if (ConvertType.TARGET_TYPE.equals(type)) {
            Class targetClassType = BeanUtil.getClassType(getCurrentPojoConverter().targetClassName());
            if (null == targetClassType) {
                targetClassType = getCurrentPojoConverter().targetBy();
            }
            return BeanUtil.classTypeEqual(getTarget().getClass(), targetClassType);
        }
        return false;
    }

    private void validateFieldName(POJOConverter pojoConverter) {
        if (StringUtil.isEmpty(pojoConverter.fieldName())) {
            throw new FatalBeanException("fieldName can't empty");
        }
    }

    private Class getMGenericityType() {
        return ConvertType.FROM_TYPE.equals(type) ? getClassType() : this.getMe().getClass();
    }

    private Class<T> getTGenericityType() {
        return getClassType();
    }


    private void setNull() {
        try {
            getWriteMethod().invoke(getTarget(), new Object[]{null});
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new FatalBeanException("can't setValue targetField:" + getWriteMethod().getName(), e);
        }
    }

    private Object getMe() {
        return me;
    }

    private void setMe(Object me) {
        this.me = me;
    }

    protected Object getFrom() {
        return from;
    }

    private void setFrom(Object from) {
        this.from = from;
    }

    protected T getTarget() {
        return target;
    }

    private void setTarget(T target) {
        this.target = target;
    }

    protected List<Field> getAnnotationFields() {
        return annotationFields;
    }

    private void setAnnotationFields(List<Field> annotationFields) {
        this.annotationFields = annotationFields;
    }

    public ConvertType getType() {
        return type;
    }

    public void setType(ConvertType type) {
        this.type = type;
    }

    private POJOConverter getCurrentPojoConverter() {
        return currentPojoConverter;
    }

    private void setCurrentPojoConverter(POJOConverter currentPojoConverter) {
        this.currentPojoConverter = currentPojoConverter;
    }

    private Field getCurrentField() {
        return currentField;
    }

    protected void setCurrentField(Field currentField) {
        this.currentField = currentField;
    }

    protected void setValue(Object value) {
        this.value = value;
    }

    protected Object getValue() {
        return value;
    }

    private Method getReadMethod() {
        return readMethod;
    }

    private void setReadMethod(Method readMethod) {
        this.readMethod = readMethod;
    }

    protected Method getWriteMethod() {
        return writeMethod;
    }

    private void setWriteMethod(Method writeMethod) {
        this.writeMethod = writeMethod;
    }

    private Class<T> getClassType() {
        return classType;
    }

    private void setClassType(Class<T> classType) {
        this.classType = classType;
    }
}
