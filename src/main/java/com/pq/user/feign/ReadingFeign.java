package com.pq.user.feign;


import com.pq.user.dto.DynamicReadingDto;
import com.pq.user.utils.UserResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 机构服务
 * @author liutao
 */
@FeignClient("service-reading")
public interface ReadingFeign {


    /**
     * 获取分享阅读信息
     * @param readingId
     * @return
     */
    @RequestMapping(value = "/reading/dynamic", method = RequestMethod.GET)
    UserResult<DynamicReadingDto> getDynamicReading(@RequestParam("readingRecordId") Long readingId);

   ;
}

