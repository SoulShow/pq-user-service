package com.pq.user.service.impl;

import org.springframework.stereotype.Service;

/**
 * @author liutao
 */
@Service
public class BaseService {

    public String myCallBack(Throwable e) {
        return "Execute raw fallback: access service fail , reason = " + e;

    }
    


}
