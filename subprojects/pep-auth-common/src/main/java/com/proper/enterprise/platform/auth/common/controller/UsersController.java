package com.proper.enterprise.platform.auth.common.controller;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.common.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashSet;

@RestController
@RequestMapping(path = "/auth/users")
public class UsersController {

    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody UserEntity userEntity) {
        return userService.save(userEntity);
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<User> get(@PathVariable String userId) {
        User user = userService.get(userId);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Collection<User>> retrieve(@RequestParam(name = "name", required = false) String name) {
        User user = userService.getByUsername(name);
        Collection<User> result = new HashSet<>();
        result.add(user);
        if (user != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<User> update(@PathVariable String userId, @RequestBody UserEntity userEntity) {
        User user = userService.get(userId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.save(userEntity), HttpStatus.OK);
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String userId, HttpServletResponse response) {
        User user = userService.get(userId);
        if (user == null) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        } else {
            userService.delete(user);
            response.setStatus(HttpStatus.NO_CONTENT.value());
        }
    }

}
