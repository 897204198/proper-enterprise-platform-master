package com.proper.enterprise.platform.push.api.openapi.exceptions;

import com.proper.enterprise.platform.core.exception.ErrMsgException;

/**
 * 推送相关的异常封装
 *
 * @author shen
 */
public class PushException extends ErrMsgException {

    private static final long serialVersionUID = 0;

    public PushException(String message) {
        super(message);
    }

}
