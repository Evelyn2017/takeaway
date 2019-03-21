package com.liu.repository;


import com.liu.dataobject.OrderDetail;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDetailRepositoryTest {

    @Autowired
    private OrderDetailRepository repository;

    @Test
    public void saveTest() {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setDetailId("123456667");
        orderDetail.setOrderId("112131");
        orderDetail.setProductIcon("some url");
        orderDetail.setProductId("11221");
        orderDetail.setProductName("ice cream");
        orderDetail.setProductPrice(new BigDecimal(5.99));
        orderDetail.setProductQuantity(5);

        OrderDetail result = repository.save(orderDetail);
        Assert.assertNotNull(result);
    }

    @Test
    public void findByOrderId() {
        List<OrderDetail> orderDetailList = repository.findByOrderId("112131");
        Assert.assertNotEquals(0, orderDetailList.size());

    }

}