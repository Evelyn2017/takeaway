package com.liu.enums;

import lombok.Getter;

@Getter
public enum PayStatusEnum {

    WAIT(0, "waiting for paying"),
    SUCCESS(1, "pay success")
    ;

    private Integer code;
    private String message;

    PayStatusEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }
}
