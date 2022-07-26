package com.harry.carrental.harrycarrental.vo;

import lombok.Data;

/**
 * Created by harryzhu on 2022/7/25
 */
@Data
public class ActionRequestVO<T> {

    private String actionId;

    private T data;
}
