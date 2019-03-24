package com.liu.service.impl;

import com.liu.converter.OrderMain2OrderDTO;
import com.liu.dataobject.OrderDetail;
import com.liu.dataobject.OrderMain;
import com.liu.dataobject.ProductInfo;
import com.liu.dto.CartDTO;
import com.liu.dto.OrderDTO;
import com.liu.enums.OrderStatusEnum;
import com.liu.enums.PayStatusEnum;
import com.liu.enums.ResultEnum;
import com.liu.exception.SellException;
import com.liu.repository.OrderDetailRepository;
import com.liu.repository.OrderMainRepository;
import com.liu.service.OrderService;
import com.liu.service.ProductInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import utils.KeyUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private ProductInfoService productInfoService;
    private OrderDetailRepository orderDetailRepository;
    private OrderMainRepository orderMainRepository;

    @Autowired
    public void setOrderMainRepository(OrderMainRepository orderMainRepository) {
        this.orderMainRepository = orderMainRepository;
    }

    @Autowired
    public void setOrderDetailRepository(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    @Autowired
    public void setProductInfoService(ProductInfoService productInfoService) {
        this.productInfoService = productInfoService;
    }


    @Override
    public OrderDTO create(OrderDTO orderDTO) {
        String orderId = KeyUtil.genUniqueKey();
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);


        //查询商品 计算总价
        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
            ProductInfo productInfo = productInfoService.findOne(orderDetail.getProductId());
            if (productInfo == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            //计算订单总价
            orderAmount = productInfo.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity())
                            .add(orderAmount));

            //订单详情入库
            orderDetail.setDetailId(KeyUtil.genUniqueKey());
            orderDetail.setOrderId(orderId);
            BeanUtils.copyProperties(productInfo, orderDetail);
            orderDetailRepository.save(orderDetail);
        }

        //写入order_main
        OrderMain orderMain = new OrderMain();
        BeanUtils.copyProperties(orderDTO, orderMain);
        orderMain.setOrderId(orderId);
        orderMain.setOrderAmount(orderAmount);
        orderMain.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMain.setPayStatus(PayStatusEnum.WAIT.getCode());

        orderMainRepository.save(orderMain);

        //扣库存
//        List<CartDTO> cartDTOList = new ArrayList<>();
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(
                e->new CartDTO(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        System.out.println(cartDTOList.toString());
        productInfoService.decreaseStock(cartDTOList);

        return orderDTO;
    }

    @Override
    public OrderDTO findOne(String orderId) {
        OrderMain orderMain = orderMainRepository.findById(orderId).orElse(null);
        if (orderMain == null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)) {
            throw new SellException(ResultEnum.ORDER_DETAIL_NOT_EXIST);
        }

        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMain, orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);

        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(String buyerOpenID, Pageable pageable) {
        Page<OrderMain> orderMainPage = orderMainRepository.findByBuyerOpenId(buyerOpenID, pageable);

        List<OrderDTO> orderDTOList = OrderMain2OrderDTO.convert(orderMainPage.getContent());

        return new PageImpl<>(orderDTOList, pageable, orderMainPage.getTotalElements());
    }

    @Override
    public OrderDTO cancel(OrderDTO orderDTO) {

        OrderMain orderMain = new OrderMain();
        //判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【取消订单】 订单状态不正确， orderId= {}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO, orderMain);
        OrderMain updateResult = orderMainRepository.save(orderMain);
        if (updateResult == null) {
            log.error("【取消订单】 更新失败，orderMain={}", orderMain);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        //返回库存
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("【取消订单】 订单中无商品详情, orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }

        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
                .map(e-> new CartDTO(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productInfoService.increaseStock(cartDTOList);

        //如果已经支付，需要退款
        if (orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())) {
            //TODO
        }

        return orderDTO;
    }

    @Override
    public OrderDTO finish(OrderDTO orderDTO) {
        return null;
    }

    @Override
    public OrderDTO paid(OrderDTO orderDTO) {
        return null;
    }
}
