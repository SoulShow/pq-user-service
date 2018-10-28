package com.pq.user.auth;


/**
 * 存储用户信息
 */
public final class LoginContextHolder {

    private static ThreadLocal<LoginUser> userLocal = new ThreadLocal<LoginUser>();

    public static LoginUser getCurrent() {
        return userLocal.get();
    }

    public static String getCurrentUserId() {
        return userLocal.get().getUserId();
    }

    public static boolean hasCurrent() {
        return getCurrent() != null;
    }

    protected static void setCurrent(LoginUser webUser) {
        userLocal.set(webUser);
    }

    public static void deleteCurrent() {
        userLocal.remove();
    }
}
