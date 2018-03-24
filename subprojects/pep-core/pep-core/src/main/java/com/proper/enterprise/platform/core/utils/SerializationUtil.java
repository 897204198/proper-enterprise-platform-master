package com.proper.enterprise.platform.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;

/**
 * 输入输出流工具类
 */
public class SerializationUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(SerializationUtil.class);

    /**
     * 私有化工具类的构造函数，避免对工具类的实例化
     */
    private SerializationUtil() {

    }

    /**
     * 深拷贝方法
     * 将传递过来的对象，通过序列化反序列化的方式，获得一个深拷贝的对象。
     * 处理后的对象完全与原对象无关（内部引用不会指向相同内存地址）
     * 可以按需要进行任何操作而不会影响原对象
     *
     * @param object 需要深拷贝的源对象(该对象及其内部引用对象必须实现Serializable标签)
     *
     * @return 源对象的一个拷贝对象
     *
     * @throws  IOException 关闭输出流时，可能会抛出IO异常
     */
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T deepClone(T object) throws IOException {
        if (object == null) {
            return null;
        }
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        Object cloneObject = null;
        try {
            out = new ObjectOutputStream(byteOut);
            out.writeObject(object);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            cloneObject = deserializeObject(byteIn);
        } catch (Exception e) {
            LOGGER.error("invoke method deepClone fail", e);
        } finally {
            if (null != out) {
                out.close();
            }
        }
        return (T) cloneObject;
    }

    /**
     * 对象反序列化
     * 将传递过来的输入流，通过反序列化，生成对象。
     *
     * 覆盖了 ObjectInputStream 中的 resolveClass 方法，
     * 为了解决使用 spring-loaded 时，latestUserDefinedLoader() 获得到的类加载器无法正确加载 Class 的问题
     * 详见 https://github.com/spring-projects/spring-loaded/issues/107
     *
     * @param binaryInput 输入流
     *
     * @return 反序列化对象
     *
     * @throws  IOException 关闭输入流时，可能会抛出IO异常
     */
    public static Object deserializeObject(InputStream binaryInput) throws IOException {
        Object object = null;
        ObjectInputStream in = null;
        if (null != binaryInput) {
            try {
                if (!(binaryInput instanceof ByteArrayInputStream && ((ByteArrayInputStream) binaryInput).available() == 0)) {
                    in = new ObjectInputStream(binaryInput) {
                        @Override
                        protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                            String name = desc.getName();
                            try {
                                return Class.forName(name, false, Thread.currentThread().getContextClassLoader());
                            } catch (ClassNotFoundException ex) {
                                return super.resolveClass(desc);
                            }
                        }
                    };
                    object = in.readObject();
                }
            } catch (Exception e) {
                LOGGER.error("invoke method deserializationObject fail", e);
            } finally {
                if (null != in) {
                    in.close();
                }
            }
        }
        return object;
    }
}
