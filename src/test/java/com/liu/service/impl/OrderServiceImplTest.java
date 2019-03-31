package com.liu.service.impl;

import com.liu.dataobject.OrderDetail;
import com.liu.dto.OrderDTO;
import com.liu.enums.OrderStatusEnum;
import com.liu.enums.PayStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderServiceImplTest {

    @Autowired
    private OrderServiceImpl orderService;
    private final String BUYER_OPENID = "00101011";
    private final String ORDER_ID = "1553392709942726211";

    @Test
    public void createTest() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName("ttvs");
        orderDTO.setBuyerAddress("TOKYO AKIHARA");
        orderDTO.setBuyerPhone("32120182");
        orderDTO.setBuyerOpenId(BUYER_OPENID);

        List<OrderDetail> orderDetailList = new ArrayList<>();
        OrderDetail o1 = new OrderDetail();
        o1.setProductId("125521");
        o1.setProductQuantity(3);

        orderDetailList.add(o1);

        orderDTO.setOrderDetailList(orderDetailList);
        OrderDTO result = orderService.create(orderDTO);
        log.info("【创建订单 result={}】", result);
    }

    @Test
    public void findOne() {
        OrderDTO result = orderService.findOne(ORDER_ID);
        log.info("【查询单个订单】result = {}", result);
        Assert.assertEquals(ORDER_ID, result.getOrderId());
    }

    @Test
    public void findList() {
        PageRequest request = PageRequest.of(0, 2);
        Page<OrderDTO> orderDTOPage = orderService.findList(BUYER_OPENID, request);
        Assert.assertNotEquals(0, orderDTOPage.getTotalElements());
    }

    @Test
    public void cancel() {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        OrderDTO result = orderService.cancel(orderDTO);
        Assert.assertEquals(OrderStatusEnum.CANCEL.getCode(), result.getOrderStatus());
    }

    @Test
    public void finish() {
        OrderDTO orderDTO = orderService.findOne("1553335518703399363");
        OrderDTO result = orderService.finish(orderDTO);
        Assert.assertEquals(OrderStatusEnum.FINISH.getCode(), result.getOrderStatus());
    }

    @Test
    public void paid() {
        OrderDTO orderDTO = orderService.findOne("1553336482407945860");
        OrderDTO result = orderService.paid(orderDTO);
        Assert.assertEquals(PayStatusEnum.SUCCESS.getCode(), result.getPayStatus());
    }

    @Test
    public void listTest() {
        PageRequest request = PageRequest.of(0, 2);
        Page<OrderDTO> order = orderService.findList(request);
        Assert.assertNotEquals(0, order.getTotalElements());
    }
}