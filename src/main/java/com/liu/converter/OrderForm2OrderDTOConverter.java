package com.liu.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liu.dataobject.OrderDetail;
import com.liu.dto.OrderDTO;
import com.liu.enums.ResultEnum;
import com.liu.exception.SellException;
import com.liu.form.OrderForm;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OrderForm2OrderDTOConverter {
    public static OrderDTO convert(OrderForm orderForm) {
        Gson  gson = new Gson();
        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setBuyerName(orderForm.getName());
        orderForm.setPhone(orderForm.getPhone());
        orderDTO.setBuyerAddress(orderForm.getAddress());
        orderDTO.setBuyerOpenId(orderForm.getOpenId());

        List<OrderDetail> orderDetailList = new ArrayList<>();

        try {
            gson.fromJson(orderForm.getItems(), new TypeToken<List<OrderDetail>>(){}.getType());
        } catch(Exception e) {
            log.error("【转换对象】错误,string={}",orderForm.getItems());
            throw new SellException(ResultEnum.PARAM_ERROR);
        }

        orderDTO.setOrderDetailList(orderDetailList);

        return orderDTO;
    }
}
