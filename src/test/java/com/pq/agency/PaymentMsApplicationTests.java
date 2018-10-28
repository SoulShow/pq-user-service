package com.pq.agency;

import com.github.prontera.service.HystrixService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PaymentMsApplicationTests {
    @Autowired
    private HystrixService  hystrixService;
    @Test
    public void contextLoads() {

            String str = hystrixService.getOrderPageList();
            System.out.print(str);

    }

}
