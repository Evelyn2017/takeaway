package com.liu.service.impl;

import com.liu.dataobject.OrderDetail;
import com.liu.dataobject.ProductInfo;
import com.liu.dto.OrderDTO;
import com.liu.enums.ResultEnum;
import com.liu.exception.SellException;
import com.liu.service.OrderService;
import com.liu.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private ProductInfoService productInfoService;

    @Autowired
    public void setProductInfoService(ProductInfoService productInfoService) {
        this.productInfoService = productInfoService;
    }


    @Override
    public OrderDTO create(OrderDTO orderDTO) {

        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
            ProductInfo productInfo = productInfoService.findOne(orderDetail.getProductId());
            if (productInfo == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
        }
        return null;
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
