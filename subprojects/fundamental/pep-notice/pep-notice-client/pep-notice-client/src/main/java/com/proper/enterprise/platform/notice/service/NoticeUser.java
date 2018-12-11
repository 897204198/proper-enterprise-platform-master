package com.proper.enterprise.platform.notice.service;

import com.proper.enterprise.platform.api.auth.model.User;

import java.util.Collection;
import java.util.List;

public interface NoticeUser {
    /**
     * 获得用户信息
     *
     * @param ids ids
     * @return 用户信息
     */
    Collection<? extends User> getUsersByIds(List<String> ids);
}
