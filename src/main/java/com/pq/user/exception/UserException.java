package com.pq.user.exception;


import com.pq.common.exception.BaseException;
import com.pq.common.exception.ErrorCode;

/**
 * @author liutao
 */
public class UserException extends BaseException {

    public UserException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }


    /**
     * 抛UserException异常
     * @param errorCode
     */
    public static void raise(ErrorCode errorCode){
        throw new UserException(errorCode);
    }


    /**
     * 抛UserException异常
     * @param errorCode
     * @param cause 异常
     */
    public static void raise(ErrorCode errorCode, Throwable cause){
        throw new UserException(errorCode, cause);
    }
}
