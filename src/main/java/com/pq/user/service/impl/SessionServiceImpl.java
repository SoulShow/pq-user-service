package com.pq.user.service.impl;

import com.pq.common.constants.CacheKeyConstants;
import com.pq.user.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author liutao
 */
@Service
public class SessionServiceImpl implements SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public void addUserSession(String userId, String sessionId) {
        redisTemplate.opsForValue().set(CacheKeyConstants.USER_SESSION_MAP_KEY_PREFIX + userId, sessionId, 30, TimeUnit.DAYS);
    }

    @Override
    public void deleteUserSession(String userId, String sessionId) {
        redisTemplate.delete(CacheKeyConstants.USER_SESSION_MAP_KEY_PREFIX + userId);
        sessionRepository.delete(sessionId);
    }


    @Override
    public void deleteUserSession(String userId) {
        // 删除映射
//        redisTemplate.delete(CacheKeyConstants.USER_SESSION_MAP_KEY_PREFIX + userId);

        String sessionId = (String) redisTemplate.opsForValue().get(CacheKeyConstants.USER_SESSION_MAP_KEY_PREFIX + userId);
        sessionRepository.delete(sessionId);

    }
}
