/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.activiti.app.security;

import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.core.PEPApplicationContext;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.UserEntityImpl;

/**
 * Utility class to integrate PEP authc and authz into Activiti.
 */
public final class SecurityUtils {

    private static User assumeUser;

    private SecurityUtils() {
    }

    private static UserService getUserService() {
        return PEPApplicationContext.getBean(UserService.class);
    }

    /**
     * Get the login of the current user.
     */
    public static String getCurrentUserId() {
        return getUserService().getCurrentUser().getId();
    }

    /**
     * @return the {@link User} object associated with the current logged in user.
     */
    public static User getCurrentUserObject() {
        if (assumeUser != null) {
            return assumeUser;
        }

        com.proper.enterprise.platform.api.auth.model.User curUser = getUserService().getCurrentUser();
        User user = new UserEntityImpl();
        user.setId(curUser.getId());
        user.setPassword(curUser.getPassword());
        user.setEmail(curUser.getEmail());
        user.setLastName(curUser.getUsername());

        return user;
    }

    public static ActivitiAppUser getCurrentActivitiAppUser() {
        ActivitiAppUser user = null;
        return user;
    }

    public static boolean currentUserHasCapability(String capability) {
        return true;
    }

    public static void assumeUser(User user) {
        assumeUser = user;
    }

    public static void clearAssumeUser() {
        assumeUser = null;
    }

}
