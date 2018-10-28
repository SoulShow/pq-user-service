package com.pq.user.auth;

import com.ep.user.dto.UserDto;
import com.ep.user.service.UserService;
import com.ep.user.utils.ConstantsUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;


/**
 * 用户登录状态验证拦截器
 *
 * @author yujl
 */
public class AuthenticationHandler implements HandlerInterceptor {

    private final static Logger LOGGER = LoggerFactory.getLogger(AuthenticationHandler.class);

    private long loginTimeOut = 30 * 60 * 1000;

    private Set<String> unLoginURI = new HashSet<String>();

    // 登录地址
    private String loginURI = "/login";


    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        StringBuffer accessUrl = request.getRequestURL();
        String accessUri = request.getRequestURI();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Auth access url: " + accessUrl);
        }

        HttpSession session = request.getSession();


        String userId = (String) session.getAttribute(ConstantsUser.SESSION_USER_ID_KEY);

        boolean isLogin = false;

        if (userId != null) {
            UserDto userDto = userService.getUserDtoByUserId(userId);
            LoginUser loginUser = new LoginUser();
//            loginUser.setName(userDto.getName());
            loginUser.setToken("");
            loginUser.setUserId(userId);
            LoginContextHolder.setCurrent(loginUser);
            isLogin = true;
        }
        if (unLoginURI.contains(accessUri)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Without auth access uri: " + accessUri);
            }
            return true;
        }

        if (isLogin && accessUri.equals(loginURI)) {
            // 获取域名
//            String tempContextUrl = accessUrl
//                    .delete(accessUrl.length() - request.getRequestURI().length(), accessUrl.length())
//                    .append(request.getServletContext().getContextPath()).toString();
//            //response.sendRedirect(tempContextUrl);
//            request.setAttribute("isLogin", true);
            if (request.getMethod() == RequestMethod.GET.name()) {
                String tempContextUrl = accessUrl.delete(accessUrl.length() - request.getRequestURI().length(), accessUrl.length())
                        .append(request.getServletContext().getContextPath()).toString();
                response.sendRedirect(tempContextUrl);
            }
            return true;
        } else if (!isLogin && accessUri.equals(loginURI)) {
            return true;
        }

        if (!isLogin) {
            // 获取域名
            String tempContextUrl = accessUrl
                    .delete(accessUrl.length() - request.getRequestURI().length(), accessUrl.length())
                    .append(request.getServletContext().getContextPath()).toString();

            String nextUrl = request.getRequestURL().toString();
            if (request.getQueryString() != null) {
                nextUrl += "?" + request.getQueryString();
            }
            if (nextUrl.contains("logout")) {
                response.sendRedirect(tempContextUrl + loginURI);
            } else {
                response.sendRedirect(tempContextUrl + loginURI + "?next=" + URLEncoder.encode(nextUrl, "utf-8"));
            }
            return false;
        }


        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView == null) {
            modelAndView = new ModelAndView();
        }
        modelAndView.addObject("loginUser", LoginContextHolder.getCurrent());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LoginContextHolder.deleteCurrent();
    }


    public long getLoginTimeOut() {
        return loginTimeOut;
    }

    public void setLoginTimeOut(long loginTimeOut) {
        this.loginTimeOut = loginTimeOut;
    }

    public Set<String> getUnLoginURI() {
        return unLoginURI;
    }

    public void setUnLoginURI(Set<String> unLoginURI) {
        this.unLoginURI = unLoginURI;
    }

    public String getLoginURI() {
        return loginURI;
    }

    public void setLoginURI(String loginURI) {
        this.loginURI = loginURI;
    }
}
