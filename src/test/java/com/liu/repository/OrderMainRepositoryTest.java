package com.liu.repository;

import com.liu.dataobject.OrderMain;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMainRepositoryTest {

    private final String OPENID = "101101001";

    @Autowired
    private OrderMainRepository repository;

    @Test
    public void saveTest() {
        OrderMain orderMain = new OrderMain();
        orderMain.setOrderId("331441");
        orderMain.setBuyerName("Jax");
        orderMain.setBuyerPhone("13189927192");
        orderMain.setBuyerAddress("some place");
        orderMain.setBuyerOpenId(OPENID);
        orderMain.setOrderAmount(new BigDecimal(24.55));

        OrderMain result = repository.save(orderMain);
        Assert.assertNotNull(result);
    }

    @Test
    public void findByBuyerOpenId() {
        PageRequest request = PageRequest.of(1,3);

        Page<OrderMain> result = repository.findByBuyerOpenId(OPENID, request);
        Assert.assertNotEquals(0, result.getTotalElements());
//        System.out.println(result.getTotalElements());

    }

}