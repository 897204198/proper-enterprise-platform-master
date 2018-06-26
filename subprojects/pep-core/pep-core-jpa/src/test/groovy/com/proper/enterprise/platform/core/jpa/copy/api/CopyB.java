package com.proper.enterprise.platform.core.jpa.copy.api;

public interface CopyB {

    /**
     * 获取Bname
     * @return 字符串
     */
    String getBname();

    /**
     * 赋值bname
     * @param bname bname
     */
    void setBname(String bname);

    /**
     * 获取Ca
     * @return CopyA
     */
    CopyA getCa();

    /**
     * 赋值Ca
     * @param a a
     */
    void setCa(CopyA a);
}
