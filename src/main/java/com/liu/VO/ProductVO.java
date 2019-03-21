package com.liu.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ProductVO {

    @JsonProperty("name")
    private String category_name;

    @JsonProperty("type")
    private Integer category_type;

    @JsonProperty("foods")
    private List<ProductInfoVO> productInfoVOList;


}
