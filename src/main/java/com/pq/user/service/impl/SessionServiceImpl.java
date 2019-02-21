package com.pq.user.service.impl;

import com.pq.common.constants.CacheKeyConstants;
import com.pq.user.service.SessionService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @author liutao
 */
@Service
public class SessionServiceImpl implements SessionService {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(SessionServiceImpl.class);


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
        Set<String> sessionIds = getSessionIdsByUserId(userId);

        if (sessionIds != null) {
            for (String sessionId : sessionIds) {
                sessionRepository.delete(sessionId);
            }
        }
        redisTemplate.delete(CacheKeyConstants.USER_SESSION_MAP_KEY_PREFIX + userId);

        String token = (String) redisTemplate.opsForValue().get(CacheKeyConstants.USER_SESSION_MAP_KEY_PREFIX + userId);
        logger.info("*********"+token);
    }
    @Override
    public Set<String> getSessionIdsByUserId(String userId) {
        return redisTemplate.opsForSet().members(CacheKeyConstants.USER_SESSION_MAP_KEY_PREFIX + userId);
    }
}
