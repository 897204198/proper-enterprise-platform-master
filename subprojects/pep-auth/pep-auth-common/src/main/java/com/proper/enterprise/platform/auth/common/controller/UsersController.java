package com.proper.enterprise.platform.auth.common.controller;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.common.entity.UserEntity;
import com.proper.enterprise.platform.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/auth/users")
public class UsersController extends BaseController {

    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<User> create(@RequestBody UserEntity userEntity) {
        return responseOfPost(userService.save(userEntity));
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<User> get(@PathVariable String userId) {
        return responseOfGet(userService.get(userId));
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<User> update(@PathVariable String userId, @RequestBody UserEntity userEntity) {
        User user = userService.get(userId);
        if (user != null) {
            user = userService.save(userEntity);
        }
        return responseOfPut(user);
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String userId) {
        User user = userService.get(userId);
        if (user != null) {
            userService.delete(user);
        }
        return responseOfDelete(user != null);
    }

}
