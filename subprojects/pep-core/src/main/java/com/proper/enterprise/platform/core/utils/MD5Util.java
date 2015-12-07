package com.proper.enterprise.platform.core.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

    public static String md5Hex(String data) {
        return DigestUtils.md5Hex(data);
    }

}
