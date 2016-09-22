package com.proper.enterprise.platform.auth.service.impl;

import com.proper.enterprise.platform.api.auth.service.PasswordEncryptService;
import com.proper.enterprise.platform.core.utils.digest.MD5;
import org.springframework.stereotype.Service;

@Service
public class Md5PasswordEncryptServiceImpl implements PasswordEncryptService {

    @Override
    public String encrypt(String password) {
        return MD5.md5Hex(password);
    }

}
