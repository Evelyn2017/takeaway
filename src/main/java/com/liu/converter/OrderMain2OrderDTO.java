package com.liu.converter;

import com.liu.dataobject.OrderMain;
import com.liu.dto.OrderDTO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMain2OrderDTO {
    public static OrderDTO convert(OrderMain orderMain) {
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMain, orderDTO);

        return orderDTO;
    }

    public static List<OrderDTO> convert(List<OrderMain> orderMainList){
        return orderMainList.stream().map(e->convert(e)).collect(Collectors.toList());

    }
}
