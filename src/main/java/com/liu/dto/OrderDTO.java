package com.liu.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.liu.dataobject.OrderDetail;
import com.liu.utils.serializer.Date2LongSerializer;
import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
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

    @JsonSerialize(using= Date2LongSerializer.class)
    private Date createTime;

    @JsonSerialize(using= Date2LongSerializer.class)
    private Date updateTime;
}
