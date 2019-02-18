package com.pq.user.feign;


import com.pq.user.dto.AgencyUserDto;
import com.pq.user.dto.UserDto;
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

    /**
     * 用户班级id
     * @param userId
     * @return
     */
    @RequestMapping(value = "/agency/user/classId", method = RequestMethod.GET)
    UserResult<List<Long>> getAgencyUserClassId(@RequestParam("userId") String userId);

    /**
     * 班级全部用户
     * @param classId
     * @return
     */
    @RequestMapping(value = "/agency/user/class/users", method = RequestMethod.GET)
    UserResult<List<UserDto>> getAllAgencyClassUser(@RequestParam("classId") Long classId);

}
