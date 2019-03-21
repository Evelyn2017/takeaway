package com.liu.VO;


import lombok.Data;

@Data
public class ResultVO<T> {

    /**error code*/
    private Integer code;

    /**hint message*/
    private String message;

    private T data;
}
