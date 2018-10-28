package com.pq.user.exception;


import com.pq.common.exception.ErrorCode;

public class UserErrorCode extends ErrorCode {

    private final static String PRE = "1";

    public UserErrorCode(String errorCode, String errorMsg) {
        super(PRE+errorCode, errorMsg);
    }
}

