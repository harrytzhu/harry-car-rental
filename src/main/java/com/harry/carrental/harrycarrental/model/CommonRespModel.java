package com.harry.carrental.harrycarrental.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by harryzhu on 2022/7/24
 */
@AllArgsConstructor
@Data
public class CommonRespModel<T> {
    private Integer status;

    private String msg;

    private T data;
}
