package com.proper.enterprise.platform.workflow.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.api.auth.service.RoleService;
import com.proper.enterprise.platform.api.auth.service.UserGroupService;
import com.proper.enterprise.platform.api.auth.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.flowable.app.model.common.RemoteGroup;
import org.flowable.app.model.common.RemoteRole;
import org.flowable.app.model.common.RemoteToken;
import org.flowable.app.model.common.RemoteUser;
import org.flowable.app.service.idm.RemoteIdmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Service
@Primary
public class PEPRemoteIdmServiceImpl implements RemoteIdmService {

    private static final Logger LOGGER = LoggerFactory.getLogger(org.flowable.app.service.idm.RemoteIdmServiceImpl.class);


    @Autowired
    protected Environment environment;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected UserGroupService userGroupService;
    @Autowired
    protected UserService userService;
    @Autowired
    private RoleService roleService;

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
        User user = userService.get(userId);
        RemoteUser remoteUser = RemoteUserConvert.convert(user);
        JsonNode json = null;
        try {
            json = objectMapper.readTree(objectMapper.writeValueAsString(remoteUser));
        } catch (JsonProcessingException e) {
            LOGGER.error("JsonProcessingException", e);
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        }
        if (json != null) {
            return parseUserInfo(json);
        }
        return null;
    }

    @Override
    public List<RemoteUser> findUsersByNameFilter(String filter) {
        Collection<? extends User> users = userService.getUsersByOrCondition(filter, EnableEnum.ENABLE);
        List<RemoteUser> remoteUsers = RemoteUserConvert.convert(users);
        JsonNode json = null;
        try {
            json = objectMapper.readTree(objectMapper.writeValueAsString(remoteUsers));
        } catch (JsonProcessingException e) {
            LOGGER.error("JsonProcessingException", e);
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        }
        if (json != null) {
            return parseUsersInfo(json);
        }
        return new ArrayList<>();
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
        Collection<? extends UserGroup> userGroups = userGroupService.getGroups(filter, null, null);
        List<RemoteGroup> remoteGroups = RemoteGroupConvert.convert(userGroups);
        JsonNode json = null;
        try {
            json = objectMapper.readTree(objectMapper.writeValueAsString(remoteGroups));
        } catch (JsonProcessingException e) {
            LOGGER.error("JsonProcessingException", e);
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        }
        if (json != null) {
            return parseGroupsInfo(json);
        }
        return new ArrayList<>();
    }

    @Override
    public List<RemoteRole> getRolesByNameFilter(String filter) {
        String roleNameFilter = filter;
        if (StringUtils.isEmpty(roleNameFilter)) {
            roleNameFilter = "%";
        } else {
            roleNameFilter = "%" + roleNameFilter + "%";
        }
        Collection roles = roleService.findRolesLike(roleNameFilter, null, null, EnableEnum.ENABLE);
        Iterator iterator = roles.iterator();
        List<RemoteRole> list = new ArrayList<>();
        while (iterator.hasNext()) {
            Role role = (Role) iterator.next();
            list.add(RemoteRoleConvert.convert(role));
        }
        return list;
    }

    private static class RemoteRoleConvert {
        public static RemoteRole convert(Role role) {
            RemoteRole remoteRole = new RemoteRole();
            if (null == role || StringUtils.isEmpty(role.getId())) {
                return null;
            }
            remoteRole.setId(role.getId());
            remoteRole.setName(role.getName());
            return remoteRole;
        }
    }

    private static class RemoteGroupConvert {
        public static RemoteGroup convert(UserGroup userGroup) {
            RemoteGroup remoteGroup = new RemoteGroup();
            if (null == userGroup || StringUtils.isEmpty(userGroup.getId())) {
                return null;
            }
            remoteGroup.setId(userGroup.getId());
            remoteGroup.setName(userGroup.getName());
            return remoteGroup;
        }


        public static List<RemoteGroup> convert(Collection<? extends UserGroup> userGroups) {
            List<RemoteGroup> remoteGroups = new ArrayList<>();
            for (UserGroup userGroup : userGroups) {
                RemoteGroup remoteGroup = convert(userGroup);
                if (null == remoteGroup) {
                    continue;
                }
                remoteGroups.add(remoteGroup);
            }
            return remoteGroups;
        }
    }

    private static class RemoteUserConvert {
        public static RemoteUser convert(User user) {
            if (null == user || StringUtils.isEmpty(user.getId())) {
                return null;
            }
            RemoteUser remoteUser = new RemoteUser();
            remoteUser.setFirstName(user.getUsername());
            remoteUser.setFullName(user.getUsername());
            remoteUser.setLastName("");
            remoteUser.setGroups(RemoteGroupConvert.convert(user.getUserGroups()));
            remoteUser.setId(user.getId());
            remoteUser.setEmail(user.getEmail());
            return remoteUser;
        }


        public static List<RemoteUser> convert(Collection<? extends User> users) {
            List<RemoteUser> remoteUsers = new ArrayList<>();
            for (User user : users) {
                RemoteUser remoteUser = convert(user);
                if (null == remoteUser) {
                    continue;
                }
                remoteUsers.add(remoteUser);
            }
            return remoteUsers;
        }
    }

    private List<RemoteUser> parseUsersInfo(JsonNode json) {
        List<RemoteUser> result = new ArrayList<>();
        if (json != null && json.isArray()) {
            ArrayNode array = (ArrayNode) json;
            for (JsonNode userJson : array) {
                result.add(parseUserInfo(userJson));
            }
        }
        return result;
    }

    private RemoteUser parseUserInfo(JsonNode json) {
        RemoteUser user = new RemoteUser();
        user.setId(json.get("id").asText());
        user.setFirstName(json.get("firstName").asText());
        user.setLastName(json.get("lastName").asText());
        user.setEmail(json.get("email").asText());
        user.setFullName(json.get("fullName").asText());

        String groupsKey = "groups";
        String privilegesKey = "privileges";

        if (json.has(groupsKey)) {
            for (JsonNode groupNode : json.get(groupsKey)) {
                user.getGroups().add(new RemoteGroup(groupNode.get("id").asText(), groupNode.get("name").asText()));
            }
        }

        if (json.has(privilegesKey)) {
            for (JsonNode privilegeNode : json.get(privilegesKey)) {
                user.getPrivileges().add(privilegeNode.asText());
            }
        }

        return user;
    }

    private List<RemoteGroup> parseGroupsInfo(JsonNode json) {
        List<RemoteGroup> result = new ArrayList<>();
        if (json != null && json.isArray()) {
            ArrayNode array = (ArrayNode) json;
            for (JsonNode userJson : array) {
                result.add(parseGroupInfo(userJson));
            }
        }
        return result;
    }

    private RemoteGroup parseGroupInfo(JsonNode json) {
        RemoteGroup group = new RemoteGroup();
        group.setId(json.get("id").asText());
        group.setName(json.get("name").asText());
        return group;
    }
}

