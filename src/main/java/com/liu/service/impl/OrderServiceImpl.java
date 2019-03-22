package com.liu.service.impl;

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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import utils.KeyUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
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
        List<CartDTO> cartDTOList = new ArrayList<>();
        orderDTO.getOrderDetailList().stream().map(
                e->new CartDTO(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productInfoService.decreaseStock(cartDTOList);

        return orderDTO;
    }

    @Override
    public OrderDTO findOne(String orderId) {
        return null;
    }

    @Override
    public Page<OrderDTO> findList(String buyerOpenID, Pageable pageable) {
        return null;
    }

    @Override
    public OrderDTO cancel(OrderDTO orderDTO) {
        return null;
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
