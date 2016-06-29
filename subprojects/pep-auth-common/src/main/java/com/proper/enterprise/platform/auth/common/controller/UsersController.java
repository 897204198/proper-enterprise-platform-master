package com.proper.enterprise.platform.auth.common.controller;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.common.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/auth/users", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UsersController {

    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
    public ResponseEntity<User> retrieve(@RequestParam(name = "name", required = false) String name) {
        User user = userService.getByUsername(name);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<User> update(@PathVariable String userId, @RequestBody UserEntity userEntity) {
        User user = userService.get(userId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.save(userEntity), HttpStatus.OK);
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.DELETE)
    public HttpStatus delete(@PathVariable String userId) {
        User user = userService.get(userId);
        if (user == null) {
            return HttpStatus.NOT_FOUND;
        }
        userService.delete(user);
        return HttpStatus.NO_CONTENT;
    }

}
