package com.proper.enterprise.platform.core.api;

import java.io.Serializable;

/**
 * 数据模型及实体基类，包含数据表公共字段的 getter 和 setter 方法
 *
 * 公共字段包括：
 *  - id                唯一标识
 *  - createUserId      创建用户 id
 *  - createTime        创建时间
 *  - lastModifyUserId  最后修改用户 id
 *  - lastModifyTime    最后修改时间
 */
public interface IBase extends Serializable {

    String getId();

    void setId(String id);

    String getCreateUserId();

    void setCreateUserId(String createUserId);

    String getCreateTime();

    void setCreateTime(String createTime);

    String getLastModifyUserId();

    void setLastModifyUserId(String lastModifyUserId);

    String getLastModifyTime();

    void setLastModifyTime(String lastModifyTime);

}
