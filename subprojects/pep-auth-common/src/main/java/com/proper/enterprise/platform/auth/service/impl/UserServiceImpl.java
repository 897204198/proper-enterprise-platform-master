package com.proper.enterprise.platform.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proper.enterprise.platform.api.auth.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.dto.UserDTO;
import com.proper.enterprise.platform.auth.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    UserRepository repo;

    @Override
    public User getUserByUsername(String username) {
        return new UserDTO(repo.findByLoginName(username));
    }

}
