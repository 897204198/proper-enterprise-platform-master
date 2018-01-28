package org.flowable.app.service.idm;

import org.flowable.app.model.common.RemoteGroup;
import org.flowable.app.model.common.RemoteToken;
import org.flowable.app.model.common.RemoteUser;
import org.springframework.stereotype.Service;

import java.util.List;

// TODO
// 1. 此空实现需覆盖源码
// 2. 看看是否是必须
@Service
public class RemoteIdmServiceImpl implements RemoteIdmService {

    @Override
    public RemoteUser authenticateUser(String username, String password) {
        return null;
    }

    @Override
    public RemoteToken getToken(String tokenValue) {
        return null;
    }

    @Override
    public RemoteUser getUser(String userId) {
        return null;
    }

    @Override
    public List<RemoteUser> findUsersByNameFilter(String filter) {
        return null;
    }

    @Override
    public List<RemoteUser> findUsersByGroup(String groupId) {
        return null;
    }

    @Override
    public RemoteGroup getGroup(String groupId) {
        return null;
    }

    @Override
    public List<RemoteGroup> findGroupsByNameFilter(String filter) {
        return null;
    }

}
