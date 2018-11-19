package com.pq.user.feign;


import com.pq.user.dto.AgencyUserDto;
import com.pq.user.utils.UserResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 机构服务
 * @author liutao
 */
@FeignClient("service-agency")
public interface AgencyFeign {
    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    @RequestMapping(value = "/agency/user/student", method = RequestMethod.GET)
    UserResult<List<AgencyUserDto>> getAgencyUserStudent(@RequestParam("userId") String userId);

}
