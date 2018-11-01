package com.pq.user.utils;

/**
 *
 * @author liken
 * @date 15/3/14
 */
public class UserResult<T> {

    private T data;

    private String status = "00000";

    private String message = "成功";


    public UserResult(){
    }

    public UserResult(T data, String status, String message){
        this.data = data;
        this.status = status;
        this.message = message;
    }

    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }

    /**
     * 通用成功结果对象
     * @return
     */
    public static <T> UserResult<T> success(T value){
        UserResult<T> res = new UserResult<T>();
        res.setData(value);
        return res;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
