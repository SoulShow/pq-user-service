package com.pq.user.auth;

import com.pq.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

/**
 * 登录验证拦截器
 *
 * @author yujl
 */
public class LoginAuthenticationHandler implements HandlerInterceptor {

    private final static String TOKEN_NAME = "_TK";
    private final static String UID = "_UID";
    private final static String UNAME = "_UNAME";
    private final static String RN = "_RN";

    private String defaultClientKey = "127.0.0.1";

    private Set<String> unLoginURLs = new HashSet<String>();

    private String loginURL;

    private String logoutURL;

    @Autowired
    private UserService userService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        // 检测到不需要登陆的URI直接返回true
        if (unLoginURLs.contains(request.getRequestURI())) {
            return true;
        }
        // 获取cookie中的信息
        AuthCookies authCookies = getAuthCookies(request);

        // 验证是否登录
        try {
            userService.validate(authCookies);
        } catch (Exception e) {
            response.sendRedirect(getLoginURL(request));
            return false;
        }

        // 设置当前登录用户至 ThreadLocal
        setCurrentUserToLocal(authCookies);

        request.setAttribute("_user", LoginContextHolder.getCurrent());
        request.setAttribute("_logout_url", this.logoutURL);

        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView == null) {
            modelAndView = new ModelAndView();
        }
        modelAndView.addObject("loginUser", LoginContextHolder.getCurrent());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        LoginContextHolder.deleteCurrent();
    }


    private void setCurrentUserToLocal(AuthCookies cookies) throws UnsupportedEncodingException {
        if (!LoginContextHolder.hasCurrent()) {
            LoginUser loginUser = new LoginUser();
            loginUser.setName(URLDecoder.decode(cookies.getUserName(), "UTF-8"));
            loginUser.setToken(cookies.getToken());
            loginUser.setUserId(cookies.getUserId());
            LoginContextHolder.setCurrent(loginUser);
        }
    }

    private AuthCookies getAuthCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String uid = getCookieValueByName(cookies, UID);
        String name = getCookieValueByName(cookies, UNAME);
        String rn = getCookieValueByName(cookies, RN);
        String token = getCookieValueByName(request.getCookies(), TOKEN_NAME);

        AuthCookies authCookies = new AuthCookies();
        authCookies.setUserId(uid);
        authCookies.setUserName(name);
        authCookies.setRandNumber(rn);
        authCookies.setToken(token);
        authCookies.setClientUnique(defaultClientKey);
        return authCookies;
    }


    private String getCookieValueByName(Cookie[] cookies, String name) {
        if (cookies == null || cookies.length <= 0) {
            return "";
        }
        for (Cookie cookie : cookies) {
            if (StringUtils.equals(name, cookie.getName())) {
                return cookie.getValue();
            }
        }
        return "";
    }

    public String getLogoutURL() {
        return logoutURL;
    }

    public void setLogoutURL(String logoutURL) {
        this.logoutURL = logoutURL;
    }

    public Set<String> getUnLoginURLs() {
        return unLoginURLs;
    }

    public void setUnLoginURLs(Set<String> unLoginURLs) {
        this.unLoginURLs = unLoginURLs;
    }

    public String getLoginURL() {
        return loginURL;
    }

    private String getLoginURL(HttpServletRequest req) throws UnsupportedEncodingException {
        StringBuffer fullLoginUrl = new StringBuffer();
        if (this.loginURL == null) {
            StringBuffer url = new StringBuffer();
            fullLoginUrl.append(url.append(req.getScheme()).append("://").append(req.getServerName()).append("/login"));
        } else {
            fullLoginUrl.append(loginURL);
        }

        fullLoginUrl.append("?next=").append(getCurrentURL(req));

        return fullLoginUrl.toString();
    }

    private String getCurrentURL(HttpServletRequest req) throws UnsupportedEncodingException {
        String scheme = req.getScheme();
        String serverName = req.getServerName();
        int serverPort = req.getServerPort();
        String contextPath = req.getContextPath();
        String servletPath = req.getServletPath();
        String pathInfo = req.getPathInfo();
        String queryString = req.getQueryString();

        // Reconstruct original requesting URL
        StringBuffer url = new StringBuffer();
        url.append(scheme).append("://").append(serverName);

        if ((serverPort != 80) && (serverPort != 443)) {
            url.append(":").append(serverPort);
        }
        url.append(contextPath).append(servletPath);
        if (pathInfo != null) {
            url.append(pathInfo);
        }
        if (queryString != null) {
            url.append("?").append(queryString);
        }
        return URLEncoder.encode(url.toString(), "UTF-8");
    }

    public void setLoginURL(String loginURL) {
        this.loginURL = loginURL;
    }
}
