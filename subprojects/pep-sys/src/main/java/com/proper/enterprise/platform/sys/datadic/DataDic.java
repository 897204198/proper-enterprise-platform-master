package com.proper.enterprise.platform.sys.datadic;

/**
 * 数据字典
 * 表示系统内编码和名称的对应关系
 * 数据字典按照类别进行归类，通常是 key-value 的扁平结构
 */
public interface DataDic extends DataDicLite {

    /**
     * 获得名称
     * @return 名称
     */
    String getName();

    /**
     * 设置名称
     * @param name 名称
     */
    void setName(String name);

    /**
     * 获得顺序
     * @return
     */
    int getOrder();

    /**
     * 设置顺序
     * @param order 顺序
     */
    void setOrder(int order);

    /**
     * 是否为默认项目
     * @return 是或否
     */
    boolean isDefault();

    /**
     * 设置是否为默认项目
     * @param dft 是或否
     */
    void setDefault(boolean dft);

}
