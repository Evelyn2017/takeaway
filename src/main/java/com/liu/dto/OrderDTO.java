package com.liu.dto;

import com.liu.dataobject.OrderDetail;
import com.liu.enums.OrderStatusEnum;
import com.liu.enums.PayStatusEnum;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDTO {

    private String orderId;

    private String buyerName;

    private String buyerPhone;

    private String buyerAddress;

    @Column(name = "buyer_openid")
    private String buyerOpenId;

    private BigDecimal orderAmount;

    private Integer orderStatus;

    private Integer payStatus;

    private List<OrderDetail> orderDetailList;
}
